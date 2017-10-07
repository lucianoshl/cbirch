package br.edu.ufu.comp.pos.db.imageretrieval.framework.base;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.edu.ufu.comp.pos.db.imageretrieval.clustering.commons.AbstractTreeNode;
import br.edu.ufu.comp.pos.db.imageretrieval.clustering.commons.ClusterTree;
import br.edu.ufu.comp.pos.db.imageretrieval.dataset.image.Image;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.histogram.Histogram;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.histogram.Histograms;

public class Index {

    final static Logger logger = LoggerFactory.getLogger( Index.class );

    private ClusterTree tree;
    private Histograms histograms;

    private Map<AbstractTreeNode, Set<Image>> index = new HashMap<AbstractTreeNode, Set<Image>>();

    public Index(ClusterTree tree) {
        this.tree = tree;
        this.histograms = new Histograms(tree.getEntriesAmount());
    }

    public void put(Image img) {

        double[] histogramContent = new double[tree.getEntriesAmount()];

        img.scan((c) -> {
            AbstractTreeNode treeNode = tree.findClosestCluster(c);
            histogramContent[treeNode.getId()]++;
            putInIndex(treeNode, img);
        });

        histograms.add(new Histogram(img, histogramContent));
    }

    private void putInIndex(AbstractTreeNode treeNode, Image img) {
        Set<Image> list = index.get(treeNode);
        if (list == null) {
            list = new HashSet<Image>();
            index.put(treeNode, list);
        }
        list.add(img);

    }

    public List<Histogram> findTop(Image query, int k) {
        logger.debug("Find top" + k + " for " + query.getImage().getName());

        Set<Image> candidates = new HashSet<Image>();

        query.scan((c) -> {
            AbstractTreeNode treeNode = tree.findClosestCluster(c);
            candidates.addAll(index.get(treeNode));
        });

        logger.debug("candidates number : " + candidates.size());

        List<Histogram> result = this.histograms.getSimilar(Histogram.create(query, tree), candidates, k);
        System.gc();
        return result;
    }

}
