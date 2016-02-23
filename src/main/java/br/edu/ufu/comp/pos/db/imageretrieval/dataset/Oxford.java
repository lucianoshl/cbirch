package br.edu.ufu.comp.pos.db.imageretrieval.dataset;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;

import org.apache.commons.io.FileUtils;

import br.edu.ufu.comp.pos.db.imageretrieval.pojo.Image;


public class Oxford extends Dataset {

    private File binaryFile;

    private File imageFolder;

    private File outputFolder;

    private File rangeSwiftInBinary;

    private File orderInBinaryFile;


    public static void main( String[] args )
        throws IOException {

        if ( args.length > 0 ) {
            String datasetsFolder = args[ 0 ];
            downloadDataset( datasetsFolder );
            createFromBase( datasetsFolder ).process();
        }
    }


    private static void downloadDataset( String datasetFolder ) throws MalformedURLException, IOException {
        File baseFolder = new File(datasetFolder,"raw/oxford");
        baseFolder.mkdirs();
        String externalPage = "http://www.robots.ox.ac.uk/~vgg/data/oxbuildings/";
        download( baseFolder, externalPage, "README2.txt" );
        download( baseFolder, externalPage, "oxbuild_images.tgz" );
        download( baseFolder, externalPage, "feat_oxc1_hesaff_sift.bin.tgz" );
        download( baseFolder, externalPage, "word_oxc1_hesaff_sift_16M_1M.tgz" );
    }


    protected static void download( File baseFolder, String externalPage, String file )
        throws IOException,
        MalformedURLException {

        URL url = new URL( externalPage
                + file );
        System.out.println( "start download " + url );
        FileUtils.copyURLToFile(url, new File(baseFolder,file));
    }


    private static Oxford createFromBase( String projectBase ) {

        return new Oxford( //
            projectBase + "/raw/oxford/feat_oxc1_hesaff_sift.bin", //
            projectBase + "/raw/oxford/word_oxc1_hesaff_sift_16M_1M", //
            projectBase + "/raw/oxford/images", //
            projectBase + "/raw/oxford/README2.txt", //
            projectBase + "/formated/oxford" );
    }


    public Oxford(
        String binaryFile,
        String siftSizeFolderDescriptor,
        String imagesFolderPath,
        String orderInBinaryFile,
        String outputFolderPath ) {
        super( null, null );
        this.binaryFile = new File( binaryFile );
        this.rangeSwiftInBinary = new File( siftSizeFolderDescriptor );
        this.imageFolder = new File( imagesFolderPath );
        this.orderInBinaryFile = new File( orderInBinaryFile );
        this.outputFolder = new File( outputFolderPath );
    }


    @Override
    public void scan( Consumer< Image > c ) {

        try {
            FileInputStream binFileReader = new FileInputStream( binaryFile );

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


    public void process()
        throws IOException {

        this.scan( ( img -> {

            String imageFileName = String.format( "%05d", img.getId() ) + "_" + img.getImage().getName();
            String siftFileName = imageFileName.replace( ".jpg", "" ) + ".sift";

            File imgOutput = new File( outputFolder, imageFileName );
            File siftOutput = new File( outputFolder, siftFileName );

            try {
                FileUtils.copyFile( img.getImage(), imgOutput );
                FileUtils.copyFile( img.getSift(), siftOutput );
            } catch ( Exception e ) {
                throw new IllegalStateException( e );
            }

        } ) );

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
