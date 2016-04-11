package br.edu.ufu.comp.pos.db.imageretrieval.dataset;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import br.edu.ufu.comp.pos.db.imageretrieval.dataset.image.Image;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.factory.DatasetFactory;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GeneratedDatasetTest {

    private Dataset dataset;
    private Image image;

    @Before
    public void before() {
        this.dataset = new DatasetFactory().create("birch 1 ukbench-1".split(" "));
    }

    @Test
    public void a_validateBinaryReader() throws IOException {

        File binFile = dataset.getSiftTrainFile();

        dataset.scanTrainSet((c) -> {
            image = c;
        });

        validateFile(binFile);

        binFile = dataset.getSiftTestFile();

        String[] testClasses = dataset.getTestClasses();
        for (String clazz : testClasses) {
            dataset.scanTestSet(clazz, (c) -> {
                image = c;
            });
        }

        validateFile(binFile);
    }

    private void validateFile(File binFile) throws FileNotFoundException, IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile(binFile, "r");

        randomAccessFile.seek(image.offset + image.size * 128);

        for (int i = 0; i < 12; i++) {
            Assert.assertNotEquals(-1, randomAccessFile.read());
        }

        Assert.assertEquals(-1, randomAccessFile.read());
        randomAccessFile.close();
    }

    // @Test
    // public void simple15() throws IOException {
    // validateSource(15, 1536, 0.75);
    // }
    //
    // @Test
    // public void simple200() throws IOException {
    // validateSource(200, 5751, 0.6666666666666666);
    // }
    //
    // @Test
    // public void simple500() throws IOException {
    // validateSource(500, 8809, 0.41435185185185186);
    // }
    //
    // @Test
    // public void simple1000() throws IOException {
    // validateSource(1000, 11958, 0.7055555555555556);
    // }
    //
    // private void validateSource(int limit, int vocabularySize, double map)
    // throws IOException {
    // double threshold = 10;
    // Result result = callExperiment(limit, threshold);
    // TestCase.assertEquals(vocabularySize, result.getVocabularySize());
    // TestCase.assertEquals(map, result.getMap());
    // }
    //
    // private Result callExperiment(int limit, double threshold) {
    // dataset.setScanLimit(limit);
    // Result result = new Framework().run(dataset, new
    // TreeFactory().createCFTree(100, threshold, 1024), 4);
    // dataset.setScanLimit(-1);
    // return result;
    // }

}
