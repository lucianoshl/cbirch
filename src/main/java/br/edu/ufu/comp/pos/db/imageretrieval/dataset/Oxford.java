package br.edu.ufu.comp.pos.db.imageretrieval.dataset;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.function.Consumer;

import org.apache.commons.io.FileUtils;

import br.edu.ufu.comp.pos.db.imageretrieval.pojo.Image;


public class Oxford extends Dataset {

    private File binaryFile;

    private File imageFolder;

    private File outputFolder;

    private File rangeSwiftInBinary;

    private File orderInBinaryFile;

    private File gtFilesFolder;


    public static void main( String[] args )
        throws IOException {

        String datasetWorkspace = "/home/lucianos/birch-experiment/datasets";
        String projectName = "oxford";

        FileUtils.deleteDirectory( new File( datasetWorkspace, "raw/" + projectName ) );
        createFromBase( datasetWorkspace, projectName ).process();
    }


    public static Oxford createFromBase( String projectBase, String datasetName ) {

        return new Oxford( projectBase, datasetName, "feat_oxc1_hesaff_sift.bin", "word_oxc1_hesaff_sift_16M_1M",
            "images", "gt_files", "README2.txt" );
    }


    public Oxford(
        String projectBase,
        String datasetName,
        String binaryFile,
        String siftSizeFolderDescriptor,
        String imagesFolderPath,
        String gtFiles,
        String orderInBinaryFile ) {

        super( projectBase, datasetName );

        String rawFolder = projectBase + "/raw/" + datasetName;
        String outputFolder = projectBase + "/formated/" + datasetName;

        this.binaryFile = new File( rawFolder, binaryFile );
        this.rangeSwiftInBinary = new File( rawFolder, siftSizeFolderDescriptor );
        this.imageFolder = new File( rawFolder, imagesFolderPath );
        this.gtFilesFolder = new File( rawFolder, gtFiles );
        this.orderInBinaryFile = new File( rawFolder, orderInBinaryFile );
        this.outputFolder = new File( outputFolder );
        
        
        
    }


    @Override
    public void scan( Consumer< Image > c ) {

        try {
            FileInputStream binFileReader = new FileInputStream( binaryFile );

            int totalImages = FileUtils.readFileToString( orderInBinaryFile ).split( "\n" ).length;

            Scanner fileOrder = new Scanner( orderInBinaryFile );
            int id = 0;
            while ( fileOrder.hasNext() ) {
                String orderElement = fileOrder.next();
                String imageName = orderElement.replace( "oxc1_", "" );
                String imageFileName = imageName + ".jpg";
                byte[] buffer = readFromBinary( binFileReader, orderElement + ".txt" );
                File imgOrigin = new File( imageFolder, imageFileName );
                File siftTmpFile = File.createTempFile( imageFileName, ".sift" );
                FileUtils.writeByteArrayToFile( siftTmpFile, buffer );

                c.accept( new Image( id, imgOrigin, siftTmpFile ) );
                id++;
                System.out.println( ( ( id + 1 ) / Double.valueOf( totalImages ) ) * 100.0 + "%" );
            }
            fileOrder.close();
            binFileReader.close();
        } catch ( Exception e ) {
            throw new IllegalStateException( e );
        }

    }


    private byte[] readFromBinary( FileInputStream binFileReader, String siftMetadataFilePath )
        throws IOException {

        Integer swiftsTotal = readSwiftsNumber( new File( rangeSwiftInBinary, siftMetadataFilePath ) );
        byte[] buffer = new byte[ swiftsTotal * 128 ];
        binFileReader.read( buffer );
        return buffer;
    }


    @SuppressWarnings( "resource" )
    public void process()
        throws IOException {

        File datasetPath = new File( outputFolder, "dataset" );
        File queriesFolder = new File( outputFolder, "queries" );
        queriesFolder.mkdirs();
        datasetPath.mkdirs();

        Map< String, List< String > > testSet = this.getTestSet();
        Set< String > queryFiles = testSet.keySet();

        File orderFile = new File( outputFolder, "file_order.txt" );
        orderFile.createNewFile();
        FileWriter fileOrder = new FileWriter( orderFile );
        this.scan( ( img -> {
            try {
                File savePath = datasetPath;

                String rawName = img.getImage().getName();

                if ( queryFiles.contains( rawName ) ) {
                    savePath = new File( queriesFolder, rawName.replaceAll( "_\\d+.jpg", "" ) );
                    savePath.mkdirs();
                    List< String > similar = testSet.get( rawName );
                    Iterator< String > iterator = similar.iterator();
                    File similarFile = new File( savePath, rawName + ".similar" );
                    similarFile.createNewFile();
                    FileWriter similarWriter = new FileWriter( similarFile );
                    while ( iterator.hasNext() ) {
                        String string = (String) iterator.next();
                        similarWriter.write( string );
                        if ( iterator.hasNext() ) {
                            similarWriter.write( " " );
                        }
                    }
                    similarWriter.close();
                } else {
                    fileOrder.write( rawName + "\n" );
                }

                String siftFileName = rawName.replace( ".jpg", "" ) + ".sift";

                File imgOutput = new File( savePath, rawName );
                File siftOutput = new File( savePath, siftFileName );

                FileUtils.copyFile( img.getImage(), imgOutput );
                FileUtils.copyFile( img.getSift(), siftOutput );
            } catch ( Exception e ) {
                throw new IllegalStateException( e );
            }
        } ) );
        fileOrder.close();

    }


    private Map< String, List< String > > getTestSet()
        throws IOException {

        File queriesFolder = new File( outputFolder, "queries" );
        String[] files = this.gtFilesFolder.list();
        Arrays.sort( files );

        Map< String, List< String > > queriesMap = new HashMap< String, List< String > >();

        for ( int i = 0; i < files.length; i++ ) {
            String good = files[ i++ ];
            String junk = files[ i++ ];
            String ok = files[ i++ ];
            String name = files[ i ];
            String query = FileUtils.readFileToString( new File( gtFilesFolder, name ) ).split( " " )[ 0 ];
            queriesMap.put( query.replace( "oxc1_", "" ) + ".jpg", this.extractRawQueries( queriesFolder, good, ok ) );
        }

        return queriesMap;

    }


    private List< String > extractRawQueries( File queriesFolder, String good, String ok )
        throws IOException {

        List< String > result = new ArrayList< String >();
        readQueryFile( good, result );
        readQueryFile( ok, result );
        return result;
    }


    private void readQueryFile( String good, List< String > result )
        throws FileNotFoundException,
        IOException {

        BufferedReader reader = new BufferedReader( new FileReader( new File( gtFilesFolder, good ) ) );

        String strLine;

        while ( ( strLine = reader.readLine() ) != null ) {
            result.add( strLine + ".jpg" );
        }

        reader.close();
    }


    private Integer readSwiftsNumber( File file ) {

        Scanner fileScanner;
        try {
            fileScanner = new Scanner( file );
        } catch ( FileNotFoundException e ) {
            throw new IllegalStateException( e );
        }
        fileScanner.next();

        Integer number = Integer.valueOf( fileScanner.next() );
        fileScanner.close();
        return number;
    }
}
