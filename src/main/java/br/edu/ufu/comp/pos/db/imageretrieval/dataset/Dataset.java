package br.edu.ufu.comp.pos.db.imageretrieval.dataset;


import java.util.function.Consumer;

import br.edu.ufu.comp.pos.db.imageretrieval.dataset.image.OxfordImage;


public abstract class Dataset {

    private long trainSetSize;
    
    private long current;


    protected abstract void trainSet( Consumer< OxfordImage > c );


    protected abstract void testSet( Consumer< OxfordImage > c );


    public void scanTrainSet( Consumer< OxfordImage > c ) {
        current = 0;
        this.trainSet( (img) -> {
            current = current + 1;
            c.accept( img );
            System.out.println( String.format( "%.2f%% %s",(current/Double.valueOf( getTrainSetSize() ))*100,img.getImage().getName()) );
        });
        
    }


    public void scanTestSet( Consumer< OxfordImage > c ) {
        this.testSet( c );
    }


    public long getTrainSetSize() {

        trainSetSize = 0;
        if ( trainSetSize == 0 ) {
            trainSet( ( i ) -> this.trainSetSize = trainSetSize + 1 );
        }
        return this.trainSetSize;
    }


    public void scanTrainSetSifts( Consumer< double[] > c ) {

        this.scanTrainSet( ( image ) -> image.scan( c ) );
    }

}