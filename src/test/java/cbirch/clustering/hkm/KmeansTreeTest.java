package cbirch.clustering.hkm;


import br.edu.ufu.comp.pos.db.imageretrieval.dataset.GeneratedDataset;
import cbirch.clustering.ClusteringMethod;
import cbirch.clustering.TreeNode;
import cbirch.dataset.BasicDataset;
import cbirch.dataset.Dataset;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;
import org.junit.Test;
import org.uncommons.maths.random.GaussianGenerator;

import java.util.Random;

import static org.junit.Assert.*;


/**
 * Created by void on 9/11/16.
 */
public class KmeansTreeTest {

    @Test
    public void buildTree() {
        Dataset basicDataset = new BasicDataset( "leedsbutterfly-pgm-lowe" );
        ClusteringMethod kmeansTree = new KmeansTree( 10, 10000 );

        TreeNode tree = kmeansTree.build( basicDataset );
        System.out.println( tree );
    }

}