package br.edu.ufu.comp.pos.db.imageretrieval.framework.base.histogram.cache;

import java.io.File;

import lombok.SneakyThrows;

public interface HistogramCache {

    double[] get(int uuid);

    void put(int uuid, double[] content);

    @SneakyThrows
    default File cacheLocation() {
	return File.createTempFile("histogram-cache", Long.toString(System.nanoTime()));
    }

}