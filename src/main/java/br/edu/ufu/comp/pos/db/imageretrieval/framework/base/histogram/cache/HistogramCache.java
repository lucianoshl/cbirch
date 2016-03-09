package br.edu.ufu.comp.pos.db.imageretrieval.framework.base.histogram.cache;

import java.io.File;
import java.nio.file.Files;

import lombok.SneakyThrows;

public interface HistogramCache {

    double[] get(int uuid);

    void put(int uuid, double[] content);
    
    boolean inCache(int uuid);

    @SneakyThrows
    default File createCacheLocation() {
	return Files.createTempDirectory("histogram-cache").toFile();
    }

}