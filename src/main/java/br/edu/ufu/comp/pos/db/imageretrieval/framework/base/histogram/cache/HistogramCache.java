package br.edu.ufu.comp.pos.db.imageretrieval.framework.base.histogram.cache;

public interface HistogramCache {

    double[] get(int uuid);

    void put(int uuid, double[] content);

}