package br.edu.ufu.comp.pos.db.imageretrieval.framework.base.histogram;

import java.util.Arrays;

import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.histogram.cache.*;
import org.apache.commons.math3.ml.distance.CosineSimilarity;
import org.apache.commons.math3.ml.distance.DistanceMeasure;

import br.edu.ufu.comp.pos.db.imageretrieval.clustering.commons.ClusterTree;
import br.edu.ufu.comp.pos.db.imageretrieval.dataset.image.Image;

public class Histogram {

	private static DistanceMeasure distanceMeasure = new CosineSimilarity();

	private static int GENERATOR = 0;
	private int uuid = ++GENERATOR;

	public static final HistogramCache cache = new HistogramEhCache();
//	private static final HistogramCache cache = new HistogramHybridCache( 15l * 1024l * 1024l * 1024l);
//	HistogramCache cache = new HistogramDiskCache();
//	HistogramCache cache = new HistogramMemoryCache();



	private Histogram normalized;

	private Image image;

	private double maxOcurrence;

	public Histogram(Image img, double[] content) {
		this.image = img;
		setContent(content);
		this.maxOcurrence = 0;
		for (double d : content) {
			if (maxOcurrence < d) {
				maxOcurrence = d;
			}
		}
	}

	public double distance(Histogram histogram) {
		return Histogram.distanceMeasure.compute(histogram.getContent(), this.getContent());
	}

	public Image getImage() {

		return image;
	}

	public Histogram normalize(Histograms histograms) {
		if (normalized == null) {
			double[] content = getContent();
			double[] result = new double[content.length];
			for (int i = 0; i < content.length; i++) {
				if (content[i] != 0) {
					result[i] = content[i] * tf(i, content) * histograms.idf(i);
				}
			}
			normalized = new Histogram(this.getImage(), result);
		}

		return normalized;
	}

	private double tf(int word, double[] content) {

		return content[word] / maxOcurrence;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(getContent());
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
		if (!Arrays.equals(getContent(), other.getContent()))
			return false;
		return true;
	}

	public static Histogram create(Image img, ClusterTree tree) {

		double[] content = new double[tree.getEntriesAmount()];
		img.scan((sift) -> {
			content[tree.findClosestCluster(sift).getId()]++;
		});
		return new Histogram(img, content);
	}

	public double[] getContent() {
		return cache.get(uuid);
	}

	private void setContent(double[] content) {
		cache.put(uuid, content);
	}

}
