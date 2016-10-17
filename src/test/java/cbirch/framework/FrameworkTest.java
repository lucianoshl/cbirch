package cbirch.framework;


import cbirch.clustering.ClusteringMethod;
import cbirch.clustering.TreeNode;
import cbirch.clustering.birch.CFTree;
import cbirch.dataset.BasicDataset;
import cbirch.dataset.Dataset;
import junit.framework.TestCase;
import org.apache.commons.math3.ml.distance.ChebyshevDistance;
import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.apache.commons.math3.ml.distance.EuclideanDistance;
import org.apache.commons.math3.ml.distance.ManhattanDistance;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;


/**
 * Created by void on 9/12/16.
 */
public class FrameworkTest {

    @Test
    public void cityBlock()
        throws Exception {

        runExperiment( new ManhattanDistance(), 0.6510416666666666 );

    }


    @Test
    public void euclidian()
        throws Exception {

        runExperiment( new EuclideanDistance(), 0.7395833333333334 );



    }


    @Test
    public void chessboard()
        throws Exception {

        runExperiment( new ChebyshevDistance(), 0.6614583333333333 );

    }


    private void runExperiment( DistanceMeasure distance, double expected ) {

        Framework framework = new Framework();

        Dataset dataset = new BasicDataset( "leedsbutterfly-pgm-lowe" );
        dataset.setSiftOrderReader((list) -> list);

        ClusteringMethod cfTree = new CFTree( 75, 0, distance, 500l, true );

        double mAP = framework.run( dataset, cfTree, 4 );
        TestCase.assertEquals( expected,mAP  );
    }

}