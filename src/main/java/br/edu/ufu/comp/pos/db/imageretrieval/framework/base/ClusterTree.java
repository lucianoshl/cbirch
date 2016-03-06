package br.edu.ufu.comp.pos.db.imageretrieval.framework.base;

import java.util.List;

import br.edu.ufu.comp.pos.db.imageretrieval.clustering.birch.cftree.CFEntry;
import br.edu.ufu.comp.pos.db.imageretrieval.dataset.image.Image;

public interface ClusterTree {

    boolean insertEntry(double[] sift);

    void optimize();

    void index(Image img);

    void finishBuild();

    List<Histogram> findTopK(Image query, int i);

    int getWordsSize();
    
    int calcWordsSize();

    CFEntry findClosestCluster(double[] sift);

}
