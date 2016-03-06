package br.edu.ufu.comp.pos.db.imageretrieval.dataset;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.edu.ufu.comp.pos.db.imageretrieval.dataset.image.OxfordImage;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.Framework;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.Result;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.TreeFactory;
import junit.framework.TestCase;

public class OxfordDatasetTest {

    OxfordImage test;
    private String workspace;
    private String dsName;
    private OxfordDataset dataset;

    @Before
    public void before(){
	this.workspace = System.getenv().get("DATASET_WORKSPACE");
	this.dsName = "oxford";

	this.dataset = OxfordDataset.createFromBase(workspace, dsName);
    }
    
    @Test
    public void validateBinaryReader() throws IOException {
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
    public void simple() throws IOException{
	dataset.setScanLimit(200);
	Result result = new Framework().run(dataset, new TreeFactory().createCFTree(100, 3000.0, 1024),4);
	TestCase.assertEquals(0.875, result.getMap());
	dataset.setScanLimit(-1);
    }

}
