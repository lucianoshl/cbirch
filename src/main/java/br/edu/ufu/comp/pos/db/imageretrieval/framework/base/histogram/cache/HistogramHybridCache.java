package br.edu.ufu.comp.pos.db.imageretrieval.framework.base.histogram.cache;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class HistogramHybridCache implements HistogramCache {

    private HistogramMemoryCache memory;
    private HistogramDiskCache disk;
    private long memoryCacheSize;

//    private TreeMap<Integer,Long> hits = new TreeMap<>();

    public HistogramHybridCache(long memory) {
        if (memory == 0l){
            throw new IllegalStateException("Hybrid cache size = 0");
        }
        this.memoryCacheSize = memory;
        this.memory = new HistogramMemoryCache();
        this.disk = new HistogramDiskCache();
    }

    @Override
    public double[] get(int uuid) {
//        Long currentHits = hits.get(uuid);
//        if (currentHits == null){
//            currentHits = 0l;
//        }
//        hits.put(uuid, currentHits + 1l);

        if (memory.inCache(uuid)) {
            return memory.get(uuid);
        } else {
            return disk.get(uuid);
        }
    }

    @Override
    public void put(int uuid, double[] content) {
        if (memory.memoryUsage() <= memoryCacheSize) {
            memory.put(uuid, content);
        } else {
            disk.put(uuid, content);
        }

    }

    @Override
    public boolean inCache(int uuid) {
        return memory.inCache(uuid) || disk.inCache(uuid);
    }

    @Override
    public int size() {
        return memory.size() + disk.size();
    }

}
