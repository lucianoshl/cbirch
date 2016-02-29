package br.edu.ufu.comp.pos.db.imageretrieval.dataset;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.function.Consumer;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;

import br.edu.ufu.comp.pos.db.imageretrieval.pojo.Image;


public class Dataset {

    private File datasetFiles;

    private File queriesFiles;

    private File fileOrderPath;

    private File resultFolder;


    public Dataset( String datasetsFolder, String datasetName ) {
        File databaseBasePath = new File( datasetsFolder + "/formated/" + datasetName );
        this.datasetFiles = new File( databaseBasePath, "dataset" );
        this.queriesFiles = new File( databaseBasePath, "queries" );
        this.resultFolder = new File( databaseBasePath, "results" );
        this.fileOrderPath = new File( databaseBasePath, "file_order.txt" );
    }


    public void scanSifts( Consumer< double[] > c )
        throws IOException {

        this.scan( ( image ) -> image.scan( c ) );
    }


    public void scan( Consumer< Image > c )
        throws IOException {

        String[] order = FileUtils.readFileToString( this.fileOrderPath ).split( "\n" );

        System.out.println( "Scanning DB:" );
        for ( int i = 0; i < order.length; i++ ) {
            File image = new File( datasetFiles, order[ i ] );
            File sift = new File( datasetFiles, order[ i ].replaceAll( ".jpg", ".sift" ) );
            c.accept( new Image( i, image, sift ) );
            System.out.println( ( ( i + 1 ) / Double.valueOf( order.length ) ) * 100.0 + "%" );
        }
        System.out.println( "End scanning." );
    }


    public File getResultFolder() {

        return resultFolder;
    }

}
