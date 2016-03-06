package br.edu.ufu.comp.pos.db.imageretrieval.framework.base;

import java.util.List;

import org.apache.log4j.Logger;

import br.edu.ufu.comp.pos.db.imageretrieval.dataset.image.Image;

public class Index {

    final static Logger logger = Logger.getLogger(Index.class);

    private ClusterTree tree;
    private Histograms histograms;

    public Index(ClusterTree tree) {
	this.tree = tree;
	this.histograms = new Histograms(tree.getWordsSize());
    }

    public void put(Image img) {
	histograms.add(Histogram.create(img, tree));
    }

    public List<Histogram> findTop(Image query, int k) {
	logger.debug("Find top"+k+" for " + query.getImage().getName());
	
	return this.histograms.getSimilar(Histogram.create(query, tree), k);
    }

}
