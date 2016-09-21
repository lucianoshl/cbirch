package cbirch.dataset;


import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.map.MapCalculator;

import java.util.function.BiConsumer;


/**
 * Created by void on 9/11/16.
 */
public interface Dataset {

    double[][] getAllFeatures();

    void scanAllFeatures( BiConsumer< double[],Integer > lambda );

    int getTotalFeatures();

    void scanAllImages( BiConsumer< Image,Integer > lambda );

    String[] getTestClasses();

    Image[] getQueries(String testClass);

    String quality(String clazz, Image query, Image result);

    MapCalculator getMapCalculator();

    int getTotalImages();
}
