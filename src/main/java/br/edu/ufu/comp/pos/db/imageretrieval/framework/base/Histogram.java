package br.edu.ufu.comp.pos.db.imageretrieval.framework.base;


import br.edu.ufu.comp.pos.db.imageretrieval.clustering.birch.cftree.CFEntry;
import br.edu.ufu.comp.pos.db.imageretrieval.commons.Similarity;
import br.edu.ufu.comp.pos.db.imageretrieval.dataset.image.OxfordImage;


public class Histogram {

    private double[] content;

    private OxfordImage image;
    
    private double maxOcurrence;


    public Histogram( OxfordImage img, int wordsSize ) {
        this.image = img;
        this.content = new double[ wordsSize ];
    }


    public void count( CFEntry closestCluster ) {
    	int wordId = closestCluster.getSubclusterID();
		this.content[ wordId ]++;
    	if (maxOcurrence < this.content[ wordId ]){
    		maxOcurrence = this.content[ wordId ];
    	}
    }


    public double distance( Histogram histogram ) {

        return Similarity.calculateSimilarity( histogram.content, this.content );
    }


    public OxfordImage getImage() {

        return image;
    }


	public Histogram normalize(Histograms histograms) {
		double[] result = new double[content.length];
		for (int i = 0; i < content.length; i++) {
			result[i] = content[i] * tf(i) * histograms.idf(i);
		}
		return null;
	}


	private double tf(int word) {
		return content[word] / maxOcurrence;
	}


	public boolean hasOcurrence(int word) {
		return content[word] > 0.0;
	}

}
