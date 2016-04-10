package br.edu.ufu.comp.pos.db.imageretrieval.clustering.commons;

import br.edu.ufu.comp.pos.db.imageretrieval.dataset.Dataset;

public interface ClusterTree {

    void finishBuild();

    int getEntriesAmount();

    AbstractTreeNode findClosestCluster(double[] sift);

    void build(Dataset dataset);

}
