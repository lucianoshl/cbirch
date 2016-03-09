package br.edu.ufu.comp.pos.db.imageretrieval.framework.base.histogram.cache;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.expiry.Expirations;
import org.ehcache.impl.config.persistence.CacheManagerPersistenceConfiguration;

public class HistogramEhCache implements HistogramCache {

    private Cache<Integer, double[]> histogramCache;
    private CacheManager cacheManager;

    public HistogramEhCache() {
	cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
		.with(new CacheManagerPersistenceConfiguration(this.cacheLocation()))
		.withCache("histogramCache",
			CacheConfigurationBuilder.newCacheConfigurationBuilder(Integer.class, double[].class)
				.withResourcePools(ResourcePoolsBuilder.newResourcePoolsBuilder()//
					.heap(1, EntryUnit.ENTRIES)//
					.disk(5, MemoryUnit.GB, false))
				.withExpiry(Expirations.noExpiration()).build())
		.build(true);

	histogramCache = cacheManager.getCache("histogramCache", Integer.class, double[].class);
    }

    @Override
    public double[] get(int uuid) {
	return histogramCache.get(uuid);
    }

    @Override
    public void put(int uuid, double[] content) {
	histogramCache.put(uuid, content);

    }

}
