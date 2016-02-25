package br.edu.ufu.comp.pos.db.imageretrieval.framework.base;


import java.io.File;

import br.edu.ufu.comp.pos.db.imageretrieval.dataset.Dataset;
import br.edu.ufu.comp.pos.db.imageretrieval.dataset.Oxford;


public class DatasetFactory {

    public Dataset create( String[] args ) {

        String workspace = args[ 1 ];
        String datasetName = args[ 2 ];

        String formattedPath = workspace + "/formated/" + datasetName;
        String rawPath = workspace + "/raw/" + datasetName;

        boolean isFormatted = new File( formattedPath ).exists();
        boolean isRaw = new File( rawPath ).exists();

        if ( isFormatted ) {
            System.out.println( "Dataset Location: " + formattedPath );
            return new Dataset( workspace, datasetName );
        } else if ( isRaw ) {
            return resolveDatasetByName( workspace, datasetName );
        } else {
            throw new IllegalStateException(
                String.format( "Dataset not found formatted=%s raw=%s", formattedPath, rawPath ) );
        }

    }


    protected Dataset resolveDatasetByName( String workspace, String datasetName ) {

        if ( datasetName.equals( "oxford" ) ) {
            return Oxford.createFromBase( workspace, datasetName );
        } else {
            throw new UnsupportedOperationException();
        }
    }

}
