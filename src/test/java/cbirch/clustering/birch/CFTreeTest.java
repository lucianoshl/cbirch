package cbirch.clustering.birch;


import cbirch.clustering.ClusteringMethod;
import cbirch.clustering.TreeNode;
import cbirch.clustering.hkm.KmeansTree;
import cbirch.clustering.hkm.KmeansTreeNode;
import cbirch.dataset.BasicDataset;
import cbirch.dataset.Dataset;
import org.apache.commons.math3.ml.distance.EuclideanDistance;
import org.apache.commons.math3.ml.distance.ManhattanDistance;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;


/**
 * Created by void on 9/11/16.
 */
public class CFTreeTest {

    @Test
    public void test() {
        Dataset basicDataset = new BasicDataset( "leedsbutterfly-pgm-lowe" );
        CFTree cfTree = new CFTree(100,0,new EuclideanDistance(),500l,true);

        TreeNode tree = cfTree.build( basicDataset );

        CFNode mostLeftLeaf = cfTree.leafListStart.nextLeaf;
        CFEntry firstEntry = mostLeftLeaf.getEntries().get(0);
        double[] firstSift = firstEntry.getCenter();

        CFEntry closestCluster = (CFEntry) cfTree.findClosestCluster(firstSift);



        do {
            double[] center = closestCluster.getCenter();
            System.out.println(closestCluster.getId());
            closestCluster = closestCluster.getParent().getParent();
            System.out.println(Arrays.toString(center));

        } while(closestCluster != null);


//        Assert.assertTrue(Arrays.equals(closestCluster.getCenter(),firstSift));

    }

    @Test
    public void test1(){
        CFTree cfTree = new CFTree(2,0,new EuclideanDistance(),100l,true);

        cfTree.insertEntry(new double[]{1d,1d});
        cfTree.insertEntry(new double[]{2d,2d});
        cfTree.insertEntry(new double[]{3d,3d});
        cfTree.insertEntry(new double[]{4d,4d});
        double[] testing = {5d, 5d};
        cfTree.insertEntry(testing);
        cfTree.insertEntry(new double[]{6d,6d});
        cfTree.insertEntry(new double[]{7d,7d});
        cfTree.insertEntry(new double[]{8d,8d});

        CFEntry closestCluster = (CFEntry) cfTree.findClosestCluster(new double[]{5.1d,5.1d});
        System.out.print(Arrays.toString(closestCluster.sumX));


    }

}