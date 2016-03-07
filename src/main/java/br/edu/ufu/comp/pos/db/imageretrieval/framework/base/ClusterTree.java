package br.edu.ufu.comp.pos.db.imageretrieval.framework.base;

import br.edu.ufu.comp.pos.db.imageretrieval.clustering.birch.cftree.CFEntry;

public interface ClusterTree {

    boolean insertEntry(double[] sift);

    void finishBuild();

    int getEntriesAmount();

    CFEntry findClosestCluster(double[] sift);

}
