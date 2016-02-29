package br.edu.ufu.comp.pos.db.imageretrieval.dataset;


import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.function.Consumer;

import br.edu.ufu.comp.pos.db.imageretrieval.commons.Utils;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.DatasetFactory;


public class OxfordDataset {

    private File binaryFile;

    private File imageFolder;

    private File rangeSwiftInBinary;

    private File gtFilesFolder;

//    private String workspace;

    private File datasetFolder;

    private File orderInBinaryFile;

    private Set< String > queryFiles;

    static OxfordImage test;


    public static void main( String[] args )
        throws IOException {

        OxfordDataset create2 = new DatasetFactory().create2( args );

        File binFile = new File( args[ 1 ], "/datasets/"+args[ 2 ]+"/feat_oxc1_hesaff_sift.bin" ); 

        create2.scan( ( img ) -> test = img, false );

        System.out.println( test );

        RandomAccessFile randomAccessFile = new RandomAccessFile( binFile, "r" );
        randomAccessFile.seek( test.offset + test.size * 128 );
        for ( int i = 0; i < 12; i++ ) {
            randomAccessFile.read();

        }
        if ( randomAccessFile.read() != -1 ) {
            randomAccessFile.close();
            throw new IllegalStateException( "O arquivo binario está sendo parseado de forma incorreta" );
        }
        randomAccessFile.close();
    }


    public void scan( Consumer< OxfordImage > c, boolean queryImages ) {

        this.scanAllImages( ( image ) -> {
            if ( isQueryFile( image ) == queryImages ) {
                c.accept( image );
            }
        } );
    }


    private boolean isQueryFile( OxfordImage image ) {

        return this.queryFiles.contains( image.getImage().getName() );
    }


    private void fillQueryFiles() {

        this.queryFiles = new HashSet< String >();
        File[] listFiles = this.gtFilesFolder.listFiles();
        for ( File file : listFiles ) {
            if ( file.getName().contains( "query.txt" ) ) {
                String queryFile = Utils.readFileToString( file ).split( " " )[ 0 ].replace( "oxc1_", "" ) + ".jpg";
                queryFiles.add( queryFile );
            }
        }
    }


    private void scanAllImages( Consumer< OxfordImage > c ) {

        Map< String, Long > aux = new HashMap< String, Long >();
        aux.put( "aux", 0l );
        scanOrderFile( ( fileName ) -> {
            long siftSize = getImageSiftSize( fileName );
            c.accept( new OxfordImage( binaryFile, new File( imageFolder, fileName ), aux.get( "aux" ), siftSize ) );
            aux.put( "aux", aux.get( "aux" ) + siftSize * 128 );
        } );

    }


    private long getImageSiftSize( String fileName ) {

        File siftSizeFile = new File( rangeSwiftInBinary, "oxc1_" + fileName.replace( ".jpg", ".txt" ) );
        Scanner createScanner = Utils.createScanner( siftSizeFile );
        createScanner.nextLine();
        long result = createScanner.nextLong();
        createScanner.close();
        return result;
    }


    private void scanOrderFile( Consumer< String > c ) {

        Scanner scanner = Utils.createScanner( orderInBinaryFile );
        while ( scanner.hasNextLine() ) {
            c.accept( scanner.nextLine().replace( "oxc1_", "" ) + ".jpg" );
        }
        scanner.close();
    }


    public static OxfordDataset createFromBase( String workspace, String datasetName ) {

        return new OxfordDataset( workspace, datasetName, "feat_oxc1_hesaff_sift.bin", "word_oxc1_hesaff_sift_16M_1M",
            "images", "gt_files", "README2.txt" );
    }


    public OxfordDataset(
        String workspace,
        String datasetName,
        String binaryFile,
        String siftSizeFolderDescriptor,
        String imagesFolderPath,
        String gtFiles,
        String orderInBinaryFile ) {

//        this.workspace = workspace;
        this.datasetFolder = Utils.getDatesetPath( workspace, datasetName );
        this.binaryFile = new File( datasetFolder, binaryFile );
        this.rangeSwiftInBinary = new File( datasetFolder, siftSizeFolderDescriptor );
        this.imageFolder = new File( datasetFolder, imagesFolderPath );
        this.gtFilesFolder = new File( datasetFolder, gtFiles );
        this.orderInBinaryFile = new File( this.datasetFolder, orderInBinaryFile );
        this.fillQueryFiles();

    }

}
