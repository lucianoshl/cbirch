package br.edu.ufu.comp.pos.db.imageretrieval.framework.base.histogram.cache;

import java.util.HashMap;
import java.util.Map;

import net.sourceforge.sizeof.SizeOf;

public class HistogramMemoryCache implements HistogramCache {

    static Map<Integer, double[]> cache = new HashMap<Integer, double[]>();

    private long memory;

    @Override
    public double[] get(int uuid) {
        return cache.get(uuid);
    }

    @Override
    public void put(int uuid, double[] content) {
        double[] old = cache.get(uuid);
        if (old != null) {
            memory -= SizeOf.sizeOf(old);
        }
        memory += SizeOf.sizeOf(content);
        cache.put(uuid, content);

    }

    @Override
    public boolean inCache(int uuid) {
        return cache.containsKey(uuid);
    }

    public long memoryUsage() {
        return memory;
    }

}
