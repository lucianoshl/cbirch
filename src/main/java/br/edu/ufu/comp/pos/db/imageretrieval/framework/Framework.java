package br.edu.ufu.comp.pos.db.imageretrieval.framework;


import br.edu.ufu.comp.pos.db.imageretrieval.clustering.birch.cftree.CFTree;
import br.edu.ufu.comp.pos.db.imageretrieval.clustering.commons.ClusterTree;
import br.edu.ufu.comp.pos.db.imageretrieval.dataset.Dataset;
import br.edu.ufu.comp.pos.db.imageretrieval.dataset.image.Image;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.Index;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.histogram.Histogram;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;


public class Framework {

    final static Logger logger = LoggerFactory.getLogger( Framework.class );


    public Result run( Dataset dataset, ClusterTree tree, int K ) {

        Result result = Result.instance;

        logMemory( "start build tree" );
        result.elapsedTime( "buildTree", ( ) -> {
            logger.info( "Building tree with test set..." );
            tree.build( dataset );
        } );

        int runnings = 3;

        for ( int i = 0; i < runnings; i++ ) {
            result.newExecution();
            tree.setClustersNames();
            executeStep( dataset, tree, K, result );
            if ( i + 1 < runnings ) {
                logMemory( "rebuild" );
                result.elapsedTime( "rebuild", ( ) -> {
                    ( (CFTree) tree ).rebuildTree();
                } );
            }
        }
        return result;
    }


    private void executeStep( Dataset dataset, ClusterTree tree, int K, Result result ) {


        logMemory( "finishRebuild" );
        logger.info( String.format( "Tree build finish. now we have %s leaves", tree.getEntriesAmount() ) );
        System.gc();
        logMemory( "gc after finishRebuild" );

        Index index = new Index( tree );
        logger.info( "Building tree index" );
        result.elapsedTime( "buildIndex", ( ) -> {
            dataset.scanTrainSet( ( img ) -> index.put( img ) );
        } );

        logger.info( "Calc mAP..." );
        List< Double > averagePrecision = new ArrayList< Double >();
        result.elapsedTime( "testingModel", ( ) -> {

            for ( String clazz : dataset.getTestClasses() ) {
                logger.debug( "Queries for class " + clazz );
                List< Double > precisionList = new ArrayList< Double >();
                dataset.scanTestSet( clazz, ( query ) -> {
                    precisionList.add( precision( clazz, dataset, index, query, K ) );
                } );

                if ( !precisionList.isEmpty() ) { // for developer testing
                double currentAveragePrecision = precisionList.stream().mapToDouble( a -> a ).average().getAsDouble();
                logger.info( "Average precision for " + clazz + " is " + currentAveragePrecision );
                averagePrecision.add( currentAveragePrecision );
            }
        }
    }   );

        double map = averagePrecision.stream().mapToDouble( a -> a ).average().getAsDouble();

        result.setMap( map );
        result.setVocabularySize( tree.getEntriesAmount() );
        result.save();
    }


    private double precision( String clazz, Dataset dataset, Index index, Image query, int K ) {

        StringBuilder log = new StringBuilder();

        log.append( "Quering " ).append( query.getImage().getName() ).append( ": " );

        List< Histogram > results = index.findTop( query, K );
        List< String > qualities = new ArrayList< String >();
        for ( int j = 0; j < results.size(); j++ ) {
            String imgName = results.get( j ).getImage().getImage().getName();
            String classification = dataset.quality( query, imgName );
            log.append( "\n\t" ).append( imgName ).append( "=" ).append( classification ).append( " " );
            qualities.add( classification );
            Result.instance.addResult( clazz, query, results.get( j ).getImage(), classification );
        }

        Double result = dataset.getMapCalculator().calc( qualities );
        log.append( "average precision=" ).append( result );
        logger.debug( log.toString() );
        return result;
    }


    public void logMemory( String moment ) {

        Runtime runtime = Runtime.getRuntime();

        NumberFormat format = NumberFormat.getInstance();

        StringBuilder sb = new StringBuilder();
        long maxMemory = runtime.maxMemory();
        long allocatedMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();

        sb.append( moment + "\n" );
        sb.append( "free memory: " + format.format( freeMemory / 1024 ) + "\n" );
        sb.append( "allocated memory: " + format.format( allocatedMemory / 1024 ) + "\n" );
        sb.append( "max memory: " + format.format( maxMemory / 1024 ) + "\n" );
        sb.append( "total free memory: " + format.format( ( freeMemory + ( maxMemory - allocatedMemory ) ) / 1024 ) + "\n" );
        logger.info( sb.toString() );
    }
}
