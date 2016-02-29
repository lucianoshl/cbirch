package br.edu.ufu.comp.pos.db.imageretrieval.dataset;


import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.function.Consumer;

import org.apache.commons.io.filefilter.FileFilterUtils;

import br.edu.ufu.comp.pos.db.imageretrieval.pojo.Image;


public class Dataset {

    private File datasetPath;

    private String resultFolder;


    public Dataset( String datasetsFolder, String datasetName ) {
        this.datasetPath = new File(datasetsFolder + "/formated/" + datasetName);
        this.resultFolder = datasetsFolder + "/results/" + datasetName;
    }


    public void scanSifts( Consumer< double[] > c ) {

        this.scan( ( image ) -> image.scan( c ) );
    }


    public void scan( Consumer< Image > c ) {

        File[] sifts = listFiles( "sift" );
        File[] images = listFiles( "jpg" );

        Arrays.sort( sifts, ( File a, File b ) -> a.getName().compareTo( b.getName() ) );

        System.out.println( "Scanning DB:" );
        for ( int i = 0; i < sifts.length; i++ ) {
            c.accept( new Image( i, images[ i ], sifts[ i ] ) );
            System.out.println( ( ( i + 1 ) / Double.valueOf( sifts.length ) ) * 100.0 + "%" );
        }
        System.out.println( "End scanning." );
    }


    private File[] listFiles( String extension ) {

        FileFilter suffixFileFilter = FileFilterUtils.suffixFileFilter( "." + extension );
        File[] files = datasetPath.listFiles( suffixFileFilter );
        return files;
    }


    public String getResultFolder() {

        return resultFolder;
    }


    public Dataset getTestSet() {

       return null;
        
    }

}
