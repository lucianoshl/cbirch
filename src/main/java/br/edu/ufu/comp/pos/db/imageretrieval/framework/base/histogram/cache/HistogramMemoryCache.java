package br.edu.ufu.comp.pos.db.imageretrieval.framework.base.histogram.cache;

import java.util.HashMap;
import java.util.Map;

public class HistogramMemoryCache implements HistogramCache {

    // static protected Cache<Integer, double[]> histogramCache;
    // static protected CacheManager cacheManager;

    // static {
    //
    // cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
    // .with(new CacheManagerPersistenceConfiguration(new File("/tmp/cache" +
    // Math.random())))
    // .withCache("histogramCache",
    // CacheConfigurationBuilder.newCacheConfigurationBuilder(Integer.class,
    // double[].class)
    // .withResourcePools(ResourcePoolsBuilder.newResourcePoolsBuilder()//
    // .heap(1000, EntryUnit.ENTRIES)//
    //// .offheap(512, MemoryUnit.MB)//
    // .disk(5, MemoryUnit.GB, false))
    // .build())
    // .build(true);
    //
    // histogramCache = cacheManager.getCache("histogramCache", Integer.class,
    // double[].class);
    //
    // }

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
