package br.edu.ufu.comp.pos.db.imageretrieval.dataset;


import static java.util.Arrays.asList;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;

import lombok.SneakyThrows;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.edu.ufu.comp.pos.db.imageretrieval.commons.Utils;
import br.edu.ufu.comp.pos.db.imageretrieval.dataset.image.Image;
import br.edu.ufu.comp.pos.db.imageretrieval.dataset.image.OxfordImage;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.map.MapCalculator;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.map.OxfordMapCalculator;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.sift.SiftScaled;


public class OxfordDataset extends Dataset {

    private static final OxfordMapCalculator MAP_CALCULATOR = OxfordMapCalculator.builder()//
        .ignore( asList( "junk" ) )//
        .positive( asList( "ok", "good" ) )//
        .negative( asList( "absent" ) )//
        .build();

    final static Logger logger = LoggerFactory.getLogger( OxfordDataset.class );

    private File binaryFile;

    private File imageFolder;

    private File rangeSwiftInBinary;

    private File gtFilesFolder;

    private File datasetFolder;

    private File orderInBinaryFile;

    private Map< String, File > queryFiles;

    private Map< String, List< String >> queryClass;

    private int scanLimit = -1;


    @SuppressWarnings( "resource" )
    @SneakyThrows
    public static void main( String[] args ) {

        String workspace = System.getenv().get( "DATASET_WORKSPACE" );
        OxfordDataset oxford = OxfordDataset.createFromBase( workspace, "oxford" );
        oxford.scanLimit = 15;

        String target = "oxford-" + oxford.scanLimit;
        File datesetPath = Utils.getDatesetPath( workspace, target );
        FileUtils.deleteQuietly( datesetPath );
        datesetPath.mkdirs();

        File fileOrder = new File( datesetPath, "README2.txt" );
        fileOrder.createNewFile();
        FileWriter writer = new FileWriter( fileOrder );

        oxford.scanOrderFile( ( file ) -> {
            try {
                writer.write( file.replace( ".jpg", "" ) + "\n" );
            } catch ( IOException e ) {
                throw new IllegalStateException( e );
            }
        } );

        File binary = new File( datesetPath, "feat_oxc1_hesaff_sift.bin" );
        binary.createNewFile();

        File position = new File( datesetPath, "word_oxc1_hesaff_sift_16M_1M" );
        position.mkdirs();

        File images = new File( datesetPath, "images" );
        images.mkdirs();

        FileUtils.copyDirectory( oxford.gtFilesFolder, new File( datesetPath, "gt_files" ) );

        oxford.scanTrainSet( ( c ) -> {
            try {
                OxfordImage c2 = (OxfordImage) c;
                FileUtils.copyFile( c2.getImage(), new File( images, c2.getImage().getName() ) );
                File file = new File( position, "oxc1_" + c2.getImage().getName().replace( ".jpg", ".txt" ) );
                file.createNewFile();
                FileWriter fwriter = new FileWriter( file );
                fwriter.write( String.valueOf( c2.getOffset() + "\n" ) );
                fwriter.write( String.valueOf( c2.getSize() ) );
                fwriter.close();

                c.scan( ( d ) -> {
                    try {
                        if ( oxford.siftReader.getClass().equals( SiftScaled.class ) ) {
                            FileUtils.writeByteArrayToFile( binary, SiftScaled.removeScale( d ), true );
                        } else {
                            FileUtils.writeByteArrayToFile( binary, Utils.convertToByte( d ), true );
                        }

                    } catch ( IOException e ) {
                        throw new IllegalStateException( e );
                    }
                } );

            } catch ( Exception e ) {
                throw new IllegalStateException( e );
            }
        } );

        FileUtils.writeByteArrayToFile( binary, new byte[ 12 ], true );

        writer.close();
    }


    @Override
    public void trainSet( Consumer< Image > c ) {

        this.scanAllImages( c );
    }


    public void testSet( String clazz, Consumer< Image > c ) {

        this.scanAllImages( ( image ) -> {
            if ( isQueryFile( clazz, image ) ) {
                c.accept( image );
            }
        } );
    }


    private boolean isQueryFile( String clazz, Image image ) {

        boolean isQuery = this.queryFiles.keySet().contains( image.getImage().getName() );
        boolean isFromClazz = this.queryClass.get( clazz ).contains( image.getImage().getName() );
        return isQuery && isFromClazz;
    }


    private void fillQueryFiles() {

        queryClass = new HashMap< String, List< String >>();

        for ( String testClazz : this.getTestClasses() ) {
            queryClass.put( testClazz, new ArrayList< String >() );
        }

        this.queryFiles = new HashMap< String, File >();
        File[] listFiles = this.gtFilesFolder.listFiles();
        for ( File file : listFiles ) {
            String fileName = file.getName();
            if ( fileName.contains( "query.txt" ) ) {
                String queryFile = Utils.readFileToString( file ).split( " " )[ 0 ].replace( "oxc1_", "" ) + ".jpg";
                String classFile = fileName.split( "_\\d+" )[ 0 ];
                queryFiles.put( queryFile, file );
                queryClass.get( classFile ).add( queryFile );
            }
        }
    }


    protected void scanAllImages( Consumer< Image > c ) {

        Map< String, Long > aux = new HashMap< String, Long >();
        aux.put( "aux", 0l );
        scanOrderFile( ( fileName ) -> {
            long siftSize = getImageSiftSize( fileName );
            c.accept( new OxfordImage( binaryFile, new File( imageFolder, fileName ), aux.get( "aux" ), siftSize, siftReader ) );
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
        int i = 0;
        while ( scanner.hasNextLine() ) {
            String fileName = scanner.nextLine().replace( "oxc1_", "" ) + ".jpg";
            c.accept( fileName );
            i++;
            if ( scanLimit == i ) {
                break;
            }
        }
        scanner.close();
    }


    public static OxfordDataset createFromBase( String workspace, String datasetName ) {

        logger.debug( "Creating class for dataset oxford" );

        return new OxfordDataset( workspace, datasetName, "feat_oxc1_hesaff_sift.bin", "word_oxc1_hesaff_sift_16M_1M", "images", "gt_files", "README2.txt" );
    }


    @SneakyThrows
    public OxfordDataset(
        String workspace,
        String datasetName,
        String binaryFile,
        String siftSizeFolderDescriptor,
        String imagesFolderPath,
        String gtFiles,
        String orderInBinaryFile ) {

        // this.workspace = workspace;
        this.datasetFolder = Utils.getDatesetPath( workspace, datasetName );
        if ( !this.datasetFolder.exists() ) {
            throw new FileNotFoundException( this.datasetFolder.getAbsolutePath() );
        }
        this.binaryFile = new File( datasetFolder, binaryFile );
        this.rangeSwiftInBinary = new File( datasetFolder, siftSizeFolderDescriptor );
        this.imageFolder = new File( datasetFolder, imagesFolderPath );
        this.gtFilesFolder = new File( datasetFolder, gtFiles );
        this.orderInBinaryFile = new File( this.datasetFolder, orderInBinaryFile );
        this.fillQueryFiles();

    }


    @Override
    public String quality( Image query, String imgName ) {

        File queryFile = this.queryFiles.get( query.getImage().getName() );

        String result = "absent";

        result = checkIs( imgName, queryFile, "good", result );
        result = checkIs( imgName, queryFile, "junk", result );
        result = checkIs( imgName, queryFile, "ok", result );

        return result;
    }


    private String checkIs( String imgName, File queryFile, String quality, String result ) {

        List< String > r = Utils.readLines( queryFile.getAbsolutePath().replace( "query.txt", quality + ".txt" ) );
        if ( r.contains( imgName.replaceAll( ".jpg", "" ) ) ) {
            return quality;
        }
        return result;
    }


    @Override
    public String[] getTestClasses() {

        File[] files = this.gtFilesFolder.listFiles();
        HashSet< String > uniq = new HashSet< String >();
        for ( File file : files ) {
            uniq.add( file.getName().split( "_\\d" )[ 0 ] );
        }
        String[] result = uniq.toArray( new String[ uniq.size() ] );
        Arrays.sort( result );
        return result;
    }


    public void setScanLimit( int scanLimit ) {

        this.scanLimit = scanLimit;
    }


    @Override
    public MapCalculator getMapCalculator() {

        return MAP_CALCULATOR;
    }


    @Override
    public long getFeaturesSize() {

        if ( featuresSize == 0 ) {
            scanOrderFile( ( c ) -> {
                long imageSiftSize = this.getImageSiftSize( c );
                this.featuresSize += imageSiftSize;
            } );
        }

        return featuresSize;
    }


    @Override
    public File getSiftTrainFile() {

        return binaryFile;
    }


    @Override
    public File getSiftTestFile() {

        throw new UnsupportedOperationException();
    }

}
