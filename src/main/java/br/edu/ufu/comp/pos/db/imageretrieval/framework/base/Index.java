package br.edu.ufu.comp.pos.db.imageretrieval.framework.base;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.edu.ufu.comp.pos.db.imageretrieval.dataset.image.Image;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.histogram.Histogram;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.histogram.Histograms;

public class Index {

    final static Logger logger = LoggerFactory.getLogger(Index.class);

    private ClusterTree tree;
    private Histograms histograms;

    public Index(ClusterTree tree) {
        this.tree = tree;
        this.histograms = new Histograms(tree.getEntriesAmount());
    }

    public void put(Image img) {
        histograms.add(Histogram.create(img, tree));
    }

    public List<Histogram> findTop(Image query, int k) {
        logger.debug("Find top" + k + " for " + query.getImage().getName());

        return this.histograms.getSimilar(Histogram.create(query, tree), k);
    }

}
