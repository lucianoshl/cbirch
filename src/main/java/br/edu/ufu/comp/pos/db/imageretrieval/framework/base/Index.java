package br.edu.ufu.comp.pos.db.imageretrieval.framework.base;

import java.util.List;

import br.edu.ufu.comp.pos.db.imageretrieval.dataset.image.Image;

public class Index {

    private ClusterTree tree;
    private Histograms histograms;
    
    

    public Index(ClusterTree tree) {
	this.tree = tree;
	this.histograms = new Histograms();
    }

    public void put(Image img) {
	histograms.add(Histogram.create(img,tree));
    }

    public List<Histogram> findTop(Image query, int k) {
	return this.histograms.getSimilar(Histogram.create(query,tree), k);
    }

}
