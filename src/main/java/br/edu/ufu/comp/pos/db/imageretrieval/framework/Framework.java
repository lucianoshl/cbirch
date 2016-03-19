package br.edu.ufu.comp.pos.db.imageretrieval.framework;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import br.edu.ufu.comp.pos.db.imageretrieval.dataset.Dataset;
import br.edu.ufu.comp.pos.db.imageretrieval.dataset.image.Image;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.ClusterTree;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.Index;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.histogram.Histogram;

public class Framework {

    final static Logger logger = Logger.getLogger(Framework.class);

    static int entryInserted = 0;
    
    public Result run(Dataset dataset, ClusterTree tree, int K) {

        Result result = Result.instance;
        
        result.elapsedTime("buildTree", () -> {
            logger.info("Building tree with test set...");
            dataset.scanTrainSet((img) -> {
            	img.scan((sift) -> tree.insertEntry(sift));
            	entryInserted++;
            	if (entryInserted%10 == 0){
            		System.gc();
            	}
            });
            tree.finishBuild();
        });

        Index index = new Index(tree);
        result.elapsedTime("buildIndex", () -> {
            dataset.scanTrainSet((img) -> index.put(img));
            System.gc();
        });

        logger.info("Calc mAP...");
        List<Double> averagePrecision = new ArrayList<Double>();
        result.elapsedTime("testingModel", () -> {

            for (String clazz : dataset.getTestClasses()) {
                logger.debug("Queries for class " + clazz);
                List<Double> precisionList = new ArrayList<Double>();
                dataset.scanTestSet(clazz, (query) -> {
                    precisionList.add(precision(clazz, dataset, index, query, K));
                });

                if (!precisionList.isEmpty()) { // for developer testing
                    double currentAveragePrecision = precisionList.stream().mapToDouble(a -> a).average().getAsDouble();
                    logger.info("Average precision for " + clazz + " is " + currentAveragePrecision);
                    averagePrecision.add(currentAveragePrecision);
                }
            }
        });

        double map = averagePrecision.stream().mapToDouble(a -> a).average().getAsDouble();

        result.setMap(map);
        result.setVocabularySize(tree.getEntriesAmount());
        return result;
    }

    private double precision(String clazz, Dataset dataset, Index index, Image query, int K) {

        StringBuilder log = new StringBuilder();

        log.append("Quering ").append(query.getImage().getName()).append(": ");

        List<Histogram> results = index.findTop(query, K);
        List<String> qualities = new ArrayList<String>();
        for (int j = 0; j < results.size(); j++) {
            String imgName = results.get(j).getImage().getImage().getName();
            String classification = dataset.quality(query, imgName);
            log.append("\n\t").append(imgName).append("=").append(classification).append(" ");
            qualities.add(classification);
            Result.instance.addResult(clazz, query, results.get(j).getImage(), classification);
        }

        Double result = dataset.getMapCalculator().calc(qualities);
        log.append("average precision=").append(result);
        logger.debug(log.toString());
        return result;
    }
}
