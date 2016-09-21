package cbirch.clustering.hkm;


import cbirch.clustering.ClusteringMethod;
import cbirch.clustering.TreeNode;
import cbirch.dataset.BasicDataset;
import cbirch.dataset.Dataset;
import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.Clusterable;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by void on 9/11/16.
 */
public class KmeansTree implements ClusteringMethod {

    final Logger logger = LoggerFactory.getLogger( KmeansTree.class );

    private final int branchingFactor;

    private final int leaves;


    public KmeansTree( int branchingFactor, int leaves ) {
        this.branchingFactor = branchingFactor;
        this.leaves = leaves;
    }


    public KmeansTreeNode cluster( double[][] doubles ) {

        logger.debug( "Hierarchical k-means: start" );
        logger.debug( String.format( "Running for %s elements and k=%s", doubles.length, branchingFactor ) );

        List< KmeansTreeNode > kmeansTreeNodes = new ArrayList<>();
        for ( double[] aDouble : doubles ) {
            kmeansTreeNodes.add( new KmeansTreeNode( aDouble ) );
        }

        KmeansTreeNode result = generateNode( new KmeansTreeNode( new double[ 128 ] ), kmeansTreeNodes, 1 );
        logger.debug( "Hierarchical k-means: end" );
        return result;

    }


    private KmeansTreeNode generateNode( KmeansTreeNode kmeansTreeNode, List< KmeansTreeNode > elements, int level ) {

        logger.trace( String.format( "Running k-means in %s tree level and k=%s for %s elements", level, branchingFactor, elements.size() ) );

        if ( elements.size() < branchingFactor ) {
            logger.trace( "Elements number is to small for branchingFactor, stopping" );
            return kmeansTreeNode;
        }

        KMeansPlusPlusClusterer< KmeansTreeNode > kmeans = new KMeansPlusPlusClusterer<>(branchingFactor);

        List< CentroidCluster< KmeansTreeNode > > clusters = kmeans.cluster( elements );
        for ( int i = 0; i < branchingFactor; i++ ) {
            CentroidCluster< KmeansTreeNode > cluster = clusters.get( i );
            Clusterable center = cluster.getCenter();
            KmeansTreeNode node;
            if ( Math.pow( branchingFactor, level ) >= leaves ) {
                node = null;
            } else {
                node = generateNode( new KmeansTreeNode( center.getPoint() ), cluster.getPoints(), level + 1 );
                kmeansTreeNode.getChilden().add( node );
            }

        }
        return kmeansTreeNode;
    }


    @Override
    public TreeNode build(Dataset dataset) {
        return cluster( dataset.getAllFeatures() );
    }

    @Override
    public TreeNode findClosestCluster(double[] sift) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getClusterSize() {
        throw new UnsupportedOperationException();
    }
}
