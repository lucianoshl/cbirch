package br.edu.ufu.comp.pos.db.imageretrieval.framework.base.histogram.cache;

import java.util.HashMap;
import java.util.Map;

public class HistogramMemoryCache implements HistogramCache {

    static Map<Integer, double[]> cache = new HashMap<Integer, double[]>();

    @Override
    public double[] get(int uuid) {
	return cache.get(uuid);
    }

    @Override
    public void put(int uuid, double[] content) {
	cache.put(uuid, content);

    }

}
