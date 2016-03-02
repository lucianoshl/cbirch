package br.edu.ufu.comp.pos.db.imageretrieval.dataset;


import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.junit.Assert;
import org.junit.Test;

import br.edu.ufu.comp.pos.db.imageretrieval.dataset.image.OxfordImage;


public class OxfordDatasetTest {

    OxfordImage test;
    
    @Test
    public void validateBinaryReader( )
        throws IOException {

        String workspace = System.getenv().get( "DATASET_WORKSPACE" );
        String dsName = "oxford";

        OxfordDataset create2 = OxfordDataset.createFromBase( workspace, dsName );

        File binFile = new File( workspace + "/datasets/" + dsName + "/feat_oxc1_hesaff_sift.bin" );

        create2.scanAllImages( ( img ) -> test = img );

        RandomAccessFile randomAccessFile = new RandomAccessFile( binFile, "r" );
        randomAccessFile.seek( test.offset + test.size * 128 );
        for ( int i = 0; i < 12; i++ ) {
            randomAccessFile.read();

        }
        Assert.assertEquals( -1 , randomAccessFile.read());
        randomAccessFile.close();
    }
}
