package br.edu.ufu.comp.pos.db.imageretrieval.framework.base.histogram.cache;


import lombok.SneakyThrows;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.io.FileUtils.readFileToByteArray;
import static org.apache.commons.io.FileUtils.writeByteArrayToFile;
import static org.apache.commons.lang3.SerializationUtils.deserialize;
import static org.apache.commons.lang3.SerializationUtils.serialize;

public class HistogramDiskCache implements HistogramCache {

    private Map<Integer, File> index = new HashMap<Integer, File>();
    private File cacheLocation;

    public HistogramDiskCache() {
        this.cacheLocation = this.createCacheLocation();
    }

    @Override
    @SneakyThrows
    public double[] get(int uuid) {
        File file = this.index.get(uuid);
        if (file == null) {
            return null;
        }
        return deserialize(readFileToByteArray(file));
    }

    @Override
    @SneakyThrows
    public void put(int uuid, double[] content) {

        File file = this.index.get(uuid);
        if (file == null) {
            file = new File(cacheLocation, uuid + ".bin");
            file.createNewFile();
            file.deleteOnExit();
            this.index.put(uuid, file);
        }
        writeByteArrayToFile(file, serialize(content));

    }

    @Override
    public boolean inCache(int uuid) {
        return index.containsKey(uuid);
    }

    @Override
    public int size() {
        return index.size();
    }

}
