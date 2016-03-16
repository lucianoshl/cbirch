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
    }

    @Test
    public void a_validateBinaryReader() throws IOException {
        File binFile = new File(workspace + "/datasets/" + dsName + "/feat_oxc1_hesaff_sift.bin");

        dataset.scanAllImages((img) -> test = (OxfordImage) img);

        RandomAccessFile randomAccessFile = new RandomAccessFile(binFile, "r");
        randomAccessFile.seek(test.offset + test.size * 128);
        for (int i = 0; i < 12; i++) {
            randomAccessFile.read();

        }
        Assert.assertEquals(-1, randomAccessFile.read());
        randomAccessFile.close();
    }

    @Test
    public void test() throws IOException {
        Result result = this.callExperiment(15, 3000);
        
        
        System.out.println(result.getVocabularySize());
    }
    
    @Test
    public void simple15() throws IOException {
        validateSource(15, 1283, 1.0);
    }

    @Test
    public void simple200() throws IOException {
        validateSource(200, 4987, 0.875);
    }

    @Test
    public void simple500() throws IOException {
        validateSource(500, 7973, 0.725);
    }

    private void validateSource(int limit, int vocabularySize, double map) throws IOException {
        double threshold = 3000d;
        Result result = callExperiment(limit, threshold);
        TestCase.assertEquals(map, result.getMap());
        TestCase.assertEquals(vocabularySize, result.getVocabularySize());
    }

	private Result callExperiment(int limit, double threshold) {
		dataset.setScanLimit(limit);
		Result result = new Framework().run(dataset, new TreeFactory().createCFTree(100, threshold, 1024), 4);
        dataset.setScanLimit(-1);
		return result;
	}

}
