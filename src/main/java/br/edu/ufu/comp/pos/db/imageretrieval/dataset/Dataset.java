package br.edu.ufu.comp.pos.db.imageretrieval.dataset;

import java.util.function.Consumer;

import org.apache.log4j.Logger;

import br.edu.ufu.comp.pos.db.imageretrieval.dataset.image.Image;

public abstract class Dataset {

    final static Logger logger = Logger.getLogger(Dataset.class);

    private long trainSetSize;

    private long current;

    private double percent;
    
    private double lastShow;

    protected abstract void trainSet(Consumer<Image> c);

    protected abstract void testSet(String clazz, Consumer<Image> c);

    public abstract String[] getTestClasses();

    public void scanTrainSet(Consumer<Image> c) {
	current = 0;
	lastShow = 0;
	this.trainSet((img) -> {
	    current = current + 1;
	    c.accept(img);
	    percent = (current / Double.valueOf(getTrainSetSize())) * 100;
//	    if (percent > lastShow){
		logger.debug(String.format("%.2f%%", percent));
//		lastShow +=10;
//	    }
	    
	});

    }

    public void scanTestSet(String clazz, Consumer<Image> c) {
	this.testSet(clazz, c);
    }

    public long getTrainSetSize() {

	trainSetSize = 0;
	if (trainSetSize == 0) {
	    trainSet((i) -> this.trainSetSize = trainSetSize + 1);
	}
	return this.trainSetSize;
    }

    public void scanTrainSetSifts(Consumer<double[]> c) {

	this.scanTrainSet((image) -> image.scan(c));
    }

    public abstract String quality(Image query, String imgName);

}