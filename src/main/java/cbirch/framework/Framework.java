package cbirch.framework;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cbirch.clustering.ClusteringMethod;
import cbirch.dataset.Dataset;
import cbirch.dataset.Image;


/**
 * Created by void on 9/12/16.
 */
public class Framework {

    final static Logger logger = LoggerFactory.getLogger( Framework.class );


    public double run(Dataset dataset, ClusteringMethod clustering, int k){
        logger.info("Experiment: start");

        logger.info("Build vocabulary: start");
        clustering.build(dataset);
        logger.info("Build vocabulary: end");

        logger.info("Build index: start");
        Index index = new Index(dataset);
        index.build(clustering);
        logger.info("Build index: end");

        logger.info("Testing model: start");
        String[] testClasses = dataset.getTestClasses();
        List<Double> averagePrecisions = new ArrayList<Double>();
        for (String testClass : testClasses) {
            Image[] queries = dataset.getQueries(testClass);
            logger.info(String.format("Testing %s queries for class %s",queries.length,testClass));
            List<Double> precisions = new ArrayList<Double>();
            for (Image query : queries) {
                Image[] results = index.find(query,k);
                List<String> qualities = new ArrayList<>();
                for (Image result : results) {
                    qualities.add(dataset.quality(testClass,query,result));
                }
                logger.info(String.format("Query %s: %s",query,Arrays.toString(results)));
                precisions.add(dataset.getMapCalculator().calc( qualities ));
            }
            Double averagePrecision = precisions.stream().mapToDouble( a -> a ).average().getAsDouble();
            averagePrecisions.add(averagePrecision);
        }

        double map = averagePrecisions.stream().mapToDouble(a -> a).average().getAsDouble();
        logger.info(String.format("mAP: %s",map));

        logger.info("Testing model: end");

        logger.info("Experiment: end");
        return map;
    }
}
