package br.edu.ufu.comp.pos.db.imageretrieval.dataset;

import java.util.function.Consumer;

import org.apache.log4j.Logger;

import br.edu.ufu.comp.pos.db.imageretrieval.dataset.image.OxfordImage;

public abstract class Dataset {

    final static Logger logger = Logger.getLogger(Dataset.class);

    private long trainSetSize;

    private long current;
    private double percent;

    protected abstract void trainSet(Consumer<OxfordImage> c);

    protected abstract void testSet(String clazz, Consumer<OxfordImage> c);

    public abstract String[] getTestClasses();

    public void scanTrainSet(Consumer<OxfordImage> c) {
	current = 0;
	this.trainSet((img) -> {
	    current = current + 1;
	    c.accept(img);
	    percent = (current / Double.valueOf(getTrainSetSize())) * 100;
	    logger.debug(String.format("%.2f%%", percent));
	});

    }

    public void scanTestSet(String clazz, Consumer<OxfordImage> c) {
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

    public abstract String quality(OxfordImage query, String imgName);

}