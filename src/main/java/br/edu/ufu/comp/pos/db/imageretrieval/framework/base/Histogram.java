package br.edu.ufu.comp.pos.db.imageretrieval.framework.base;

import java.io.File;
import java.util.Arrays;

import org.apache.commons.math3.ml.distance.CosineDistance;
import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.impl.config.persistence.CacheManagerPersistenceConfiguration;

import br.edu.ufu.comp.pos.db.imageretrieval.dataset.image.Image;

public class Histogram {

    private static int GENERATOR = 0;
    public static int CACHE_HITS = 0;
    private int uuid = ++GENERATOR;

    static protected Cache<Integer, double[]> histogramCache;
    static protected CacheManager cacheManager;

    private Histogram normalized;

    static {

	cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
		.with(new CacheManagerPersistenceConfiguration(new File("/tmp/cache" + Math.random())))
		.withCache("histogramCache",
			CacheConfigurationBuilder.newCacheConfigurationBuilder(Integer.class, double[].class)
				.withResourcePools(ResourcePoolsBuilder.newResourcePoolsBuilder()//
					.heap(1000, EntryUnit.ENTRIES).offheap(1, MemoryUnit.GB)//
					.disk(5, MemoryUnit.GB, false))
				.build())
		.build(true);

	histogramCache = cacheManager.getCache("histogramCache", Integer.class, double[].class);

    }

    private static DistanceMeasure distanceMeasure = new CosineDistance();

    private Image image;

    private double maxOcurrence;

    Histogram(Image img, double[] content) {
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
	    content[tree.findClosestCluster(sift).getSubclusterID()]++;
	});
	return new Histogram(img, content);
    }

    public double[] getContent() {
	++CACHE_HITS;
	return histogramCache.get(uuid);
    }

    private void setContent(double[] content) {
	++CACHE_HITS;
	histogramCache.put(uuid, content);
    }

}
