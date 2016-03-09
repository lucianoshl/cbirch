package br.edu.ufu.comp.pos.db.imageretrieval.framework.base.histogram.cache;

public class HistogramHybridCache implements HistogramCache {

    private HistogramMemoryCache memory;
    private HistogramDiskCache disk;
    private long inMemory;

    public HistogramHybridCache(long memory) {
	this.inMemory = memory;
	this.memory = new HistogramMemoryCache();
	this.disk = new HistogramDiskCache();
    }

    @Override
    public double[] get(int uuid) {
	if (memory.inCache(uuid)) {
	    return memory.get(uuid);
	} else {
	    return disk.get(uuid);
	}
    }

    @Override
    public void put(int uuid, double[] content) {
	if (memory.memoryUsage() <= inMemory) {
	    memory.put(uuid, content);
	} else {
	    disk.put(uuid, content);
	}

    }

    @Override
    public boolean inCache(int uuid) {
	return memory.inCache(uuid) || disk.inCache(uuid);
    }

}
