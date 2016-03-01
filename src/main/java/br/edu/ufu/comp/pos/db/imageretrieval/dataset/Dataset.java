package br.edu.ufu.comp.pos.db.imageretrieval.dataset;


import java.util.function.Consumer;

import br.edu.ufu.comp.pos.db.imageretrieval.dataset.image.OxfordImage;


public abstract class Dataset {

    private long testSetSize;
    
    private long current;


    public abstract void trainSet( Consumer< OxfordImage > c );


    public abstract void testSet( Consumer< OxfordImage > c );


    public void scanTrainSet( Consumer< OxfordImage > c ) {

        this.trainSet( c );
    }


    public void scanTestSet( Consumer< OxfordImage > c ) {
        current = 0;
        this.testSet( (img) -> {
            current = current + 1;
            c.accept( img );
            System.out.println( String.format( "%.2f%% %s",(current/Double.valueOf( getTestSetSize() ))*100,img.getImage().getName()) );
        });
    }


    public long getTestSetSize() {

        testSetSize = 0;
        if ( testSetSize == 0 ) {
            testSet( ( i ) -> this.testSetSize = testSetSize + 1 );
        }
        return this.testSetSize;
    }


    public void scanTestSetSifts( Consumer< double[] > c ) {

        this.scanTestSet( ( image ) -> image.scan( c ) );
    }

}