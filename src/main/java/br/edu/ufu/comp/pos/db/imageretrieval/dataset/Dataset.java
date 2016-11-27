package br.edu.ufu.comp.pos.db.imageretrieval.dataset;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.function.Consumer;

import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.map.OxfordMapCalculator;
import lombok.Getter;
import lombok.Setter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.edu.ufu.comp.pos.db.imageretrieval.dataset.image.Image;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.map.MapCalculator;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.sift.Sift;

import static java.util.Arrays.asList;

public abstract class Dataset {

    final static Logger logger = LoggerFactory.getLogger( Dataset.class );

    private static final OxfordMapCalculator MAP_CALCULATOR = OxfordMapCalculator.builder()//
            .positive( asList( "ok", "good" ) )//
            .negative( asList( "absent" ) )//
            .build();

    private long trainSetSize;

    private long current;

    private double percent;

    protected long featuresSize;

    @Getter
    @Setter
    protected Sift siftReader = new Sift();

    protected abstract void trainSet(Consumer<Image> c);

    protected abstract void testSet(String clazz, Consumer<Image> c);

    public abstract String[] getTestClasses();

    public abstract File getSiftTrainFile();

    public abstract File getSiftTestFile();

    public void scanTrainSet(Consumer<Image> c) {
        current = 0;
        this.trainSet((img) -> {
            current = current + 1;
            c.accept(img);
            percent = (current / Double.valueOf(getTrainSetSize())) * 100;
            logger.debug(String.format("%.2f%%", percent));

        });

    }

    public abstract void scanSifts(Consumer<double[]> c);



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

    public MapCalculator getMapCalculator() {
        return MAP_CALCULATOR;
    }

    public long getFeaturesSize() {
        if (featuresSize == 0) {
            this.scanTrainSetSifts((c) -> {
                featuresSize++;
            });
        }

        return featuresSize;

    }

}