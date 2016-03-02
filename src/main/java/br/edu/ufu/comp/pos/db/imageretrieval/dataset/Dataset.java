package br.edu.ufu.comp.pos.db.imageretrieval.dataset;

import java.util.function.Consumer;

import br.edu.ufu.comp.pos.db.imageretrieval.dataset.image.OxfordImage;

public abstract class Dataset {

    private long trainSetSize;

    private long current;
    private double percent;
    private double lastPrint;

    protected abstract void trainSet(Consumer<OxfordImage> c);

    protected abstract void testSet(String clazz, Consumer<OxfordImage> c);

    public abstract String[] getTestClasses();

    public void scanTrainSet(Consumer<OxfordImage> c) {
	current = 0;
	this.trainSet((img) -> {
	    current = current + 1;
	    c.accept(img);
	    percent = (current / Double.valueOf(getTrainSetSize())) * 100;
	    System.out.println(String.format("%.2f%%", percent));
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