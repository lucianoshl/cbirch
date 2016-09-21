package cbirch.dataset;


import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.map.MapCalculator;
import lombok.Setter;
import lombok.SneakyThrows;

import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;


/**
 * Created by void on 9/11/16.
 */
public abstract class Dataset {

    @Setter
    Function< List< Integer >, List< Integer > > siftOrderReader = ( original ) -> {
        Collections.shuffle( original );
        return original;
    };


    public abstract void scanAllFeatures( BiConsumer< double[], Integer > lambda );


    public abstract int getTotalFeatures();


    public abstract void scanAllImages( BiConsumer< Image, Integer > lambda );


    public abstract String[] getTestClasses();


    public abstract Image[] getQueries( String testClass );


    public abstract String quality( String clazz, Image query, Image result );


    public abstract MapCalculator getMapCalculator();


    public abstract int getTotalImages();

    @SneakyThrows
    public double[][] getAllFeatures() {

        double[][] result = new double[ this.getTotalFeatures() ][ 128 ];

        scanAllFeatures( ( sift, position ) -> {
            result[ position ] = sift;
        } );

        return result;
    }
}
