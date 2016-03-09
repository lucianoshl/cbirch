// package br.edu.ufu.comp.pos.db.imageretrieval.framework.tree;
//
// import java.util.List;
//
// import br.edu.ufu.comp.pos.db.imageretrieval.clustering.birch.cftree.CFTree;
// import br.edu.ufu.comp.pos.db.imageretrieval.dataset.image.Image;
// import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.ClusterTree;
// import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.Histogram;
// import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.Histograms;
//
// public class BirchTree extends CFTree implements ClusterTree {
//
// private Histograms histograms = new Histograms();
//
// public BirchTree(int branchingFactor, double threshold, int memory) {
// super(branchingFactor, threshold, 1, true);
// this.setAutomaticRebuild(true);
// this.setMemoryLimitMB(memory);
// }
//
// @Override
// public void optimize() {
//
// this.rebuildTree();
// this.rebuildTree();
// }
//
// @Override
// public void index(Image img) {
// histograms.add(Histograms.getHistogram(img, this));
// }
//
// @Override
// public void finishBuild() {
//
// this.finishedInsertingData();
// }
//
// @Override
// public List<Histogram> findTopK(Image queryImage, int k) {
//
// Histogram query = Histograms.getHistogram(queryImage, this);
//
// return histograms.getSimilar(query, k);
//
// }
//
// }
