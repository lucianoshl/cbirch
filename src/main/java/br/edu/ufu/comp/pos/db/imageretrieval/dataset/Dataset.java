package br.edu.ufu.comp.pos.db.imageretrieval.dataset;


import java.util.function.Consumer;

import br.edu.ufu.comp.pos.db.imageretrieval.dataset.image.OxfordImage;


public abstract class Dataset {

    public abstract void scanTrainSet( Consumer< OxfordImage > c );


    public abstract void scanTestSet( Consumer< OxfordImage > c );


    public void scanTestSetSifts( Consumer< double[] > c  ){
        this.scanTestSet( (image) -> image.scan( c ) );
    }

}