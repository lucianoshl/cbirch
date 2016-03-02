package br.edu.ufu.comp.pos.db.imageretrieval.framework.base;

import java.util.Arrays;

import org.apache.commons.math3.ml.distance.CosineDistance;
import org.apache.commons.math3.ml.distance.DistanceMeasure;

import br.edu.ufu.comp.pos.db.imageretrieval.clustering.birch.cftree.CFEntry;
import br.edu.ufu.comp.pos.db.imageretrieval.dataset.image.OxfordImage;

public class Histogram {

    private static DistanceMeasure distanceMeasure = new CosineDistance();

    private double[] content;

    private OxfordImage image;

    private double maxOcurrence;

    public Histogram(OxfordImage img, int wordsSize) {
	this.image = img;
	this.content = new double[wordsSize];
    }

    public Histogram(OxfordImage img, double[] content) {
	this.image = img;
	this.content = content;
	this.maxOcurrence = 0;
	for (double d : content) {
	    if (maxOcurrence < d) {
		maxOcurrence = d;
	    }
	}
    }

    public void count(CFEntry closestCluster) {

	int wordId = closestCluster.getSubclusterID();
	this.content[wordId]++;
	if (maxOcurrence < this.content[wordId]) {
	    maxOcurrence = this.content[wordId];
	}
    }

    public double distance(Histogram histogram) {
	return Histogram.distanceMeasure.compute(histogram.content, this.content);
    }

    public OxfordImage getImage() {

	return image;
    }

    public Histogram normalize(Histograms histograms) {

	double[] result = new double[content.length];
	for (int i = 0; i < content.length; i++) {
	    if (content[i] != 0) {
		result[i] = content[i] * tf(i) * histograms.idf(i);
	    }
	}
	return new Histogram(this.getImage(), result);
    }

    private double tf(int word) {

	return content[word] / maxOcurrence;
    }

    public boolean hasOcurrence(int word) {

	return content[word] > 0.0;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + Arrays.hashCode(content);
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	Histogram other = (Histogram) obj;
	if (!Arrays.equals(content, other.content))
	    return false;
	return true;
    }

}
