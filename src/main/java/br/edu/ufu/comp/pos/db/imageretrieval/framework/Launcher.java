package br.edu.ufu.comp.pos.db.imageretrieval.framework;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.log4j.Logger;

import br.edu.ufu.comp.pos.db.imageretrieval.dataset.Dataset;
import br.edu.ufu.comp.pos.db.imageretrieval.dataset.image.OxfordImage;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.ClusterTree;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.DatasetFactory;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.Histogram;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.TreeFactory;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.tree.BirchTree;

public class Launcher {

    final static Logger logger = Logger.getLogger(Launcher.class);

    public static void main(String[] args) throws IOException {

	if (args.length == 0) {
	    throw new IllegalArgumentException("tree name is required");
	}

	Dataset dataset = new DatasetFactory().create(args);
	BirchTree tree = new TreeFactory().create(args);

	StopWatch stopWatch = new StopWatch();
	stopWatch.start();
	
	new Launcher().run(dataset, tree, 4);
	logger.info("elapsed time " + stopWatch.getTime());
    }

    public void run(Dataset dataset, ClusterTree tree, int K) throws IOException {

	logger.info("Building tree with test set...");
	dataset.scanTrainSetSifts((sift) -> tree.insertEntry(sift));

	tree.finishBuild();

	dataset.scanTrainSet((img) -> tree.index(img));

	logger.info("Calc mAP...");
	List<Double> averagePrecision = new ArrayList<Double>();

	for (String clazz : dataset.getTestClasses()) {
	    logger.debug("Queries for class " + clazz);
	    List<Double> precisionList = new ArrayList<Double>();
	    dataset.scanTestSet(clazz, (query) -> {
		precisionList.add(precision(dataset, tree, query, K));
	    });

	    if (!precisionList.isEmpty()) { // for developer testing
		double currentAveragePrecision = precisionList.stream().mapToDouble(a -> a).average().getAsDouble();
		logger.info("Average precision for " + clazz + " is " + currentAveragePrecision);
		averagePrecision.add(currentAveragePrecision);
	    }
	}

	logger.info("Vocabulary size: " + tree.calcWordsSize());
	logger.info("mAP: " + averagePrecision.stream().mapToDouble(a -> a).average().getAsDouble());

    }

    private double precision(Dataset dataset, ClusterTree tree, OxfordImage query, int K) {

	StringBuilder log = new StringBuilder();

	log.append("Quering ").append(query.getImage().getName()).append(": ");

	int queryAssert = 0;
	List<Histogram> results = tree.findTopK(query, K);
	for (int j = 0; j < results.size(); j++) {
	    String imgName = results.get(j).getImage().getImage().getName();
	    String classification = dataset.quality(query, imgName);
	    log.append("\n\t").append(imgName).append("=").append(classification).append(" ");
	    if (Arrays.asList("good", "ok", "junk").contains(classification)) {
		queryAssert += 1;
	    }
	}

	Double result = queryAssert / Double.valueOf(K);
	log.append("assert ").append(queryAssert).append("/").append(K).append("=").append(result);
	logger.debug(log.toString());
	return result;
    }
}
