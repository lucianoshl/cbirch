package cbirch.clustering;


import cbirch.clustering.hkm.KmeansTreeNode;
import cbirch.dataset.BasicDataset;
import cbirch.dataset.Dataset;


/**
 * Created by void on 9/11/16.
 */
public interface ClusteringMethod {

    TreeNode build( Dataset dataset );

    TreeNode findClosestCluster(double[] sift);

    int getClusterSize();
}
