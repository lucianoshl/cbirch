package br.edu.ufu.comp.pos.db.imageretrieval.framework.base;


import br.edu.ufu.comp.pos.db.imageretrieval.clustering.birch.cftree.CFEntry;
import br.edu.ufu.comp.pos.db.imageretrieval.commons.Similarity;
import br.edu.ufu.comp.pos.db.imageretrieval.dataset.image.OxfordImage;


public class Histogram {

    private double[] content;

    private OxfordImage image;


    public Histogram( OxfordImage img, int wordsSize ) {
        this.image = img;
        this.content = new double[ wordsSize ];
    }


    public void count( CFEntry closestCluster ) {

        this.content[ closestCluster.getSubclusterID() ]++;
    }


    public double distance( Histogram histogram ) {

        return Similarity.calculateSimilarity( histogram.content, this.content );
    }


    public OxfordImage getImage() {

        return image;
    }

}
