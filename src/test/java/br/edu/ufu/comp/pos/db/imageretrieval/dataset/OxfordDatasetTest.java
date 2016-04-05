package br.edu.ufu.comp.pos.db.imageretrieval.dataset;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import br.edu.ufu.comp.pos.db.imageretrieval.dataset.image.OxfordImage;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.Framework;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.Result;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.SiftScaled;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.factory.TreeFactory;
import junit.framework.TestCase;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class OxfordDatasetTest {

    OxfordImage test;
    private String workspace;
    private String dsName;
    private OxfordDataset dataset;

    @Before
    public void before() {
        this.workspace = System.getenv().get("DATASET_WORKSPACE");
        this.dsName = "oxford";
        this.dataset = OxfordDataset.createFromBase(workspace, dsName);
        this.dataset.setSiftReader(new SiftScaled());
    }

    @Test
    public void a_validateBinaryReader() throws IOException {
        File binFile = new File(workspace + "/datasets/" + dsName + "/feat_oxc1_hesaff_sift.bin");

        dataset.scanAllImages((img) -> {
            test = (OxfordImage) img;
        });

        RandomAccessFile randomAccessFile = new RandomAccessFile(binFile, "r");
        randomAccessFile.seek(test.offset + test.size * 128);

        for (int i = 0; i < 12; i++) {
            Assert.assertNotEquals(-1, randomAccessFile.read());

        }
        Assert.assertEquals(-1, randomAccessFile.read());
        randomAccessFile.close();
    }

    @Test
    public void simple15() throws IOException {
        validateSource(15, 1313, 1.0);
    }

    @Test
    public void simple200() throws IOException {
        validateSource(200, 4799, 0.875);
    }

    @Test
    public void simple500() throws IOException {
        validateSource(500, 7396, 0.6444444444444445);
    }
    
    @Test
    public void simple1000() throws IOException {
        validateSource(1000, 11958, 0.7055555555555556);
    }
    
    private void validateSource(int limit, int vocabularySize, double map) throws IOException {
        double threshold = 11.683065953654183;
        Result result = callExperiment(limit, threshold);
        TestCase.assertEquals(vocabularySize, result.getVocabularySize());
        TestCase.assertEquals(map, result.getMap());
    }

    private Result callExperiment(int limit, double threshold) {
        dataset.setScanLimit(limit);
        Result result = new Framework().run(dataset, new TreeFactory().createCFTree(100, threshold, 1024), 4);
        dataset.setScanLimit(-1);
        return result;
    }

}
