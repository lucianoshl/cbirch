package br.edu.ufu.comp.pos.db.imageretrieval.framework.base.histogram.cache;

import java.io.File;
import java.util.Map;

import lombok.SneakyThrows;

public class HistogramDiskCache implements HistogramCache {

    private Map<Integer,File> index;
    private File cacheLocation;
    
    public HistogramDiskCache() {
	this.cacheLocation = this.createCacheLocation();
    }
    
    @Override
    public double[] get(int uuid) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    @SneakyThrows
    public void put(int uuid, double[] content) {
	
	File file = this.index.get(uuid);
	if (file == null){
	    file = new File(cacheLocation,uuid+".bin");
	    file.createNewFile();
	}
	
	// TODO Auto-generated method stub

    }

}
