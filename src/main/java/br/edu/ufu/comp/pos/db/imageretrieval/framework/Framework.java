package br.edu.ufu.comp.pos.db.imageretrieval.framework;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import br.edu.ufu.comp.pos.db.imageretrieval.dataset.Dataset;
import br.edu.ufu.comp.pos.db.imageretrieval.dataset.image.Image;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.ClusterTree;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.Histogram;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.Index;

public class Framework {

    final static Logger logger = Logger.getLogger(Framework.class);

    public Result run(Dataset dataset, ClusterTree tree, int K) throws IOException {

	Result result = new Result();
	
	logger.info("Building tree with test set...");
	dataset.scanTrainSetSifts((sift) -> tree.insertEntry(sift));

	tree.finishBuild();
	logger.info("Finish tree build: " + tree.getWordsSize());

	Index index = new Index(tree);

	dataset.scanTrainSet((img) -> index.put(img));

	logger.info("Calc mAP...");
	List<Double> averagePrecision = new ArrayList<Double>();

	for (String clazz : dataset.getTestClasses()) {
	    logger.debug("Queries for class " + clazz);
	    List<Double> precisionList = new ArrayList<Double>();
	    dataset.scanTestSet(clazz, (query) -> {
		precisionList.add(precision(dataset, index, query, K));
	    });

	    if (!precisionList.isEmpty()) { // for developer testing
		double currentAveragePrecision = precisionList.stream().mapToDouble(a -> a).average().getAsDouble();
		logger.info("Average precision for " + clazz + " is " + currentAveragePrecision);
		averagePrecision.add(currentAveragePrecision);
	    }
	}

	logger.info("Vocabulary size: " + tree.getWordsSize());
	double map = averagePrecision.stream().mapToDouble(a -> a).average().getAsDouble();
	logger.info("mAP: " + map);

	result.setMap(map);
	result.setVocabularySize(tree.getWordsSize());
	return result;
    }

    private double precision(Dataset dataset, Index index, Image query, int K) {
	
	StringBuilder log = new StringBuilder();

	log.append("Quering ").append(query.getImage().getName()).append(": ");

	int queryAssert = 0;
	List<Histogram> results = index.findTop(query, K);
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
