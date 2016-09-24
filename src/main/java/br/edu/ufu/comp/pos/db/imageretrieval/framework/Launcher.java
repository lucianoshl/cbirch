package br.edu.ufu.comp.pos.db.imageretrieval.framework;


import br.edu.ufu.comp.pos.db.imageretrieval.clustering.commons.ClusterTree;
import br.edu.ufu.comp.pos.db.imageretrieval.dataset.Dataset;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.factory.DatasetFactory;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.factory.TreeFactory;
import cbirch.clustering.ClusteringMethod;
import cbirch.clustering.birch.CFTree;
import cbirch.dataset.BasicDataset;
import org.apache.commons.math3.ml.distance.EuclideanDistance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Launcher {

    static {
        SimpleDateFormat startLauncherDateFormat = new SimpleDateFormat( "yyyy-MM-dd-hh-mm-ss" );
        String startLauncherDate = startLauncherDateFormat.format( new Date() );
        MDC.put( "launcher-date", startLauncherDate );
        MDC.put( "log-id", MDC.get( "launcher-date" ) + "-0-startup" );
    }

    final static Logger logger = LoggerFactory.getLogger( Launcher.class );


    public static void main( String[] args )
        throws IOException {

        cbirch.framework.Framework framework = new cbirch.framework.Framework();

        cbirch.dataset.Dataset dataset = new BasicDataset( "leedsbutterfly-pgm-lowe" );
        dataset.setSiftOrderReader((list) -> list);

        ClusteringMethod cfTree = new CFTree( 75, 0, new EuclideanDistance(), 4 * 1024, true );

        double mAP = framework.run( dataset, cfTree, 4 );
        logger.info("map = " + mAP);

//        exec( args );
    }


    public static void exec( String[] args )
        throws IOException {

        Result result = new Result();
        try {
            callExperiment( args );
        } catch ( Exception e ) {
            result.setError( e );
            e.printStackTrace();
        } finally {
            // result.save();
        }

    }


    private static void callExperiment( String[] args ) {

        if ( args.length == 0 ) {
            throw new IllegalArgumentException( "tree name is required" );
        }

        Dataset dataset = new DatasetFactory().create( args );

        ClusterTree tree = new TreeFactory().create( args );

        int k = 4;

        new Framework().run( dataset, tree, k );
    }

}
