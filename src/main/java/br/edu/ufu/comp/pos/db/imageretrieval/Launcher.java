package br.edu.ufu.comp.pos.db.imageretrieval;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.edu.ufu.comp.pos.db.imageretrieval.clustering.birch.cftree.CFTree;
import br.edu.ufu.comp.pos.db.imageretrieval.dataset.Dataset;
import br.edu.ufu.comp.pos.db.imageretrieval.pojo.ImageHits;
import br.edu.ufu.comp.pos.db.imageretrieval.pojo.QueryResult;
import br.edu.ufu.comp.pos.db.imageretrieval.pojo.ResultReport;

public class Launcher {

	public static void main(String[] args) throws IOException {

		if (args.length != 5) {
			throw new IllegalArgumentException(String.format("five parameters is required, i found %s", args.length));
		}

		int branchingFactor = Integer.valueOf(args[0]);
		int threshold = Integer.valueOf(args[1]);
		int memory = Integer.valueOf(args[2]);
		String datasetsFolder = args[3];
		String datasetName = args[4];

		runExperiment(branchingFactor, threshold, memory, datasetsFolder, datasetName);
	}

	private static void runExperiment(int branchingFactor, double threshold, int memory, String datasetsFolder,
			String datasetName) throws FileNotFoundException, IOException {

		Dataset dataset = new Dataset(datasetsFolder, datasetName);

		ResultReport report = executeExperiment(branchingFactor, threshold, memory, dataset);

		logMemory();
		System.out.println("Running garbage colector");
		System.gc();
		logMemory();

		report.save(dataset.getResultFolder());

	}

	private static void logMemory() {
		Runtime runtime = Runtime.getRuntime();

		NumberFormat format = NumberFormat.getInstance();

		long maxMemory = runtime.maxMemory();
		long allocatedMemory = runtime.totalMemory();
		long freeMemory = runtime.freeMemory();

		System.out.println("#######################################");
		System.out.println("free memory: " + format.format(freeMemory / 1024));
		System.out.println("allocated memory: " + format.format(allocatedMemory / 1024));
		System.out.println("max memory: " + format.format(maxMemory / 1024));
		System.out.println("total free memory: " + format.format((freeMemory + (maxMemory - allocatedMemory)) / 1024));
		System.out.println("#######################################");
	}

	private static ResultReport executeExperiment(int b, double t, int memory, Dataset dataset) {
		ResultReport result = new ResultReport();
		result.setStartAt(new Date());

		CFTree tree = new CFTree(b, t, 1, true);
		tree.setAutomaticRebuild(true);
		tree.setMemoryLimitMB(memory);

		result.benchmark("Building tree", () -> {
			dataset.scanSifts((sift) -> tree.insertEntry(sift));
		});

		result.benchmark("Rebuild tree twice", () -> {
			tree.rebuildTree();
			tree.rebuildTree();
		});

		result.benchmark("Building image index in tree", () -> {
			dataset.scan((img) -> tree.index(img));
		});

		List<QueryResult> queryResults = new ArrayList<QueryResult>();
		result.benchmark("Query images in tree", () -> {
			dataset.scan((queryImage) -> {
				result.addImage(queryImage);
				List<ImageHits> queryResult = tree.queryImage(queryImage);
				queryResults.add(new QueryResult(queryImage, queryResult));
			});
		});

		result.setBranchingFactor(b);
		result.setThreshold(t);
		result.setMemory(tree.getMemoryLimit());
		result.setQueryResults(queryResults);
		result.setFinalThreshold(tree.getThreshold());
		result.setDatasetBasePath(dataset.getDatasetPath());
		result.setEndAt(new Date());
		return result;
	}

}
