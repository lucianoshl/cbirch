package br.edu.ufu.comp.pos.db.imageretrieval.framework.base.factory;


import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.edu.ufu.comp.pos.db.imageretrieval.commons.Utils;
import br.edu.ufu.comp.pos.db.imageretrieval.dataset.Dataset;
import br.edu.ufu.comp.pos.db.imageretrieval.dataset.GeneratedDataset;
import br.edu.ufu.comp.pos.db.imageretrieval.dataset.OxfordDataset;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.Result;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.sift.Sift;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.sift.SiftScaled;


public class DatasetFactory {

    final static Logger logger = LoggerFactory.getLogger( DatasetFactory.class );


    public Dataset create( String[] args ) {

        String workspace = System.getenv().get( "DATASET_WORKSPACE" );

        if ( args.length < 2 ) {
            throw new IllegalArgumentException( "A normalização do dataset" );
        }

        if ( args.length < 3 ) {
            throw new IllegalArgumentException( "Informe o dataset" );
        }
        String datasetName = args[ 2 ];

        Sift siftReader = null;
        if ( args[ 1 ].equals( "normalized" ) ) {
            siftReader = new SiftScaled();
            Result.extraInfo( "Dataset normalized", true );
        } else if ( args[ 1 ].equals( "non-normalized" ) ) {
            siftReader = new Sift();
            Result.extraInfo( "Dataset normalized", false );
        } else {
            new IllegalStateException( "invalid normalized parameter: normalized or non-normalized" );
        }

        File datasetPath = Utils.getDatesetPath( workspace, datasetName );

        Result.extraInfo( "Dataset workspace", workspace );
        Result.extraInfo( "Dataset name", datasetName );
        Result.extraInfo( "Dataset path", datasetPath );

        Result.instance.setDatasetPath( datasetPath );
        Dataset dataset = null;

        if ( new File( datasetPath, "README2.txt" ).exists() ) {
            Result.extraInfo( "Dataset class", OxfordDataset.class );
            dataset = OxfordDataset.createFromBase( workspace, datasetName );
            // ((OxfordDataset) dataset).setScanLimit(15);
        } else if ( new File( datasetPath, "train.sift" ).exists() ) {
            dataset = new GeneratedDataset( datasetName );

        } else {
            throw new UnsupportedOperationException( "unsupported dataset in " + datasetPath.getAbsolutePath() );
        }

        Result.extraInfo( "Dataset features", dataset.getFeaturesSize() );

        dataset.setSiftReader( siftReader );
        return dataset;

    }

}
