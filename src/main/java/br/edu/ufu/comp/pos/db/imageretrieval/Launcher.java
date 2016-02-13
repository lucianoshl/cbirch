package br.edu.ufu.comp.pos.db.imageretrieval;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.context.MapValueResolver;
import com.google.gson.GsonBuilder;

import br.edu.ufu.comp.pos.db.imageretrieval.clustering.birch.cftree.CFTree;
import br.edu.ufu.comp.pos.db.imageretrieval.dataset.Dataset;
import br.edu.ufu.comp.pos.db.imageretrieval.pojo.ImageHits;
import br.edu.ufu.comp.pos.db.imageretrieval.pojo.QueryResult;
import br.edu.ufu.comp.pos.db.imageretrieval.pojo.ResultReport;

public class Launcher {
	// -Xmx4096m
	// -javaagent:/home/lucianos/birch-experiment/src/main/resources/SizeOf.jar
	public static void main(String[] args) throws IOException {

		if (args.length != 5) {
			throw new IllegalArgumentException(String.format("five parameters is required, i found %s", args.length));
		}

		int b = Integer.valueOf(args[0]);
		int t = Integer.valueOf(args[1]);
		int memory = Integer.valueOf(args[2]);
		String datasetsFolder = args[3];
		String datasetName = args[4];

		runExperiment(b, t, memory, datasetsFolder, datasetName);
	}

	private static void runExperiment(int b, double t, int memory, String datasetsFolder, String datasetName)
			throws FileNotFoundException, IOException {

		ResultReport result = new ResultReport();
		result.setStartAt(new Date());

		Dataset dataset = new Dataset(datasetsFolder, datasetName);

		CFTree tree = new CFTree(b, t, 1, true);
		tree.setPeriodicMemLimitCheck(50000);
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
				List<ImageHits> queryResult = tree.queryImage(queryImage);
				queryResults.add(new QueryResult(queryImage, queryResult));
			});
		});

		result.setBranchingFactor(b);
		result.setThreshold(t);
		result.setMemory(tree.getMemoryLimit());
		result.setQueryResults(queryResults);
		result.setFinalThreshold(tree.getThreshold());

		saveReport(result, dataset.getResultFolder());
	}

	protected static void saveReport(ResultReport result, String resultFolderPath) throws IOException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HH:mm:ss");
		String fileName = dateFormat.format(new Date());
		File outJsonFile = generateJsonFile(result, resultFolderPath, fileName);
		File outHtmlFile = createHtmlFile(resultFolderPath, fileName, outJsonFile);

		System.out.println("saved in");
		System.out.println(outHtmlFile);
	}

	private static File createHtmlFile(String resultFolderPath, String fileName, File outJsonFile) throws IOException {
		File outHtmlFile = new File(resultFolderPath, fileName + ".html");

		Map<String, String> map = new HashMap<String, String>();
		map.put("libsPath", Launcher.class.getClassLoader().getResource("templates/libs").getFile());
		map.put("jsonName", outJsonFile.getAbsolutePath());

		Handlebars handlebars = new Handlebars();
		handlebars.setStartDelimiter("<%");
		handlebars.setEndDelimiter("%>");

		Context context = Context.newBuilder(map).resolver(MapValueResolver.INSTANCE).build();

		Template template = handlebars
				.compileInline(IOUtils.toString(Launcher.class.getClassLoader().getResource("templates/results.html")));
		FileUtils.writeStringToFile(outHtmlFile, template.apply(context));

		template.apply(context);

		return outHtmlFile;
	}

	private static File generateJsonFile(ResultReport result, String resultFolderPath, String fileName)
			throws IOException {
		File jsonFolder = new File(resultFolderPath, "json");
		if (!jsonFolder.exists()) {
			jsonFolder.mkdir();
		}
		File outJsonFile = new File(jsonFolder, fileName + ".json");

		Handlebars handlebars = new Handlebars();
		Template template = handlebars
				.compileInline(IOUtils.toString(Launcher.class.getClassLoader().getResource("templates/results.js")));
		FileUtils.writeStringToFile(outJsonFile,
				template.apply(new GsonBuilder().create().toJson(result)));
		return outJsonFile;
	}

}
