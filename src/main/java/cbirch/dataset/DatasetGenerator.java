package cbirch.dataset;


import cbirch.sift.SiftExtractor;
import cbirch.utils.ImageUtils;
import lombok.SneakyThrows;
import magick.ImageInfo;
import magick.MagickImage;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Arrays;

import static cbirch.utils.ImageUtils.convert;
import static cbirch.utils.ImageUtils.getExtension;


/**
 * Created by lucianos on 8/17/16.
 */
public class DatasetGenerator {

    final static Logger logger = LoggerFactory.getLogger( DatasetGenerator.class );

    private final SiftExtractor extractor;

    private final File workspaceFolder;

    private final File datasetFolder;

    private final File imagesFolder;

    private final File targetImageFolder;


    public DatasetGenerator( SiftExtractor extractor, String rawName ) {
        this.extractor = extractor;
        this.workspaceFolder = new File( System.getProperty( "cbirch_workspace" ) );
        String dsname = String.format( "%s-%s", rawName, extractor.identifier() );
        this.datasetFolder = new File( new File( this.workspaceFolder, "datasets" ), dsname );
        this.imagesFolder = new File( new File( this.workspaceFolder, "raw-datasets" ), rawName );
        this.targetImageFolder = new File( this.datasetFolder, "images" );
    }


    public void generate() {

        this.copyImages();
        this.extractSift();
    }


    @SneakyThrows
    private void extractSift() {

        logger.info( "SIFT extract start" );
        String[] images = targetImageFolder.list();
        Arrays.sort( images );

        File binary = recreate( new File( this.datasetFolder, "sift.binary" ) );
        File positionFile = recreate( new File( this.datasetFolder, "sift.positions" ) );

        int currentPosition = 0;
        for ( String image : images ) {
            logger.info( "processing " + image );
            int[] sift = extractor.extract( new File( targetImageFolder, image ) );
            register( currentPosition, currentPosition + sift.length, sift, image, binary, positionFile );
            currentPosition += sift.length;
        }

        logger.info( "SIFT extract end" );
    }


    @SneakyThrows
    private void register( int position, int offset, int[] sift, String image, File binary, File positionFile ) {

        FileUtils.writeLines( positionFile, Arrays.asList( String.format( "%s\t%s\t%s\t%s", image, position, offset, sift.length / 128 ) ), true );
        FileUtils.writeByteArrayToFile( binary, convertToByte( sift ), true );
    }


    private byte[] convertToByte( int[] sift ) {

        byte[] result = new byte[ sift.length ];
        for ( int i = 0; i < result.length; i++ ) {
            result[ i ] = (byte) sift[ i ];
        }
        return result;
    }


    @SneakyThrows
    private File recreate( File file ) {

        if ( file.exists() ) {
            file.delete();
            file.createNewFile();
        }
        return file;
    }


    @SneakyThrows
    private void copyImages() {

        if ( !targetImageFolder.exists() ) {
            targetImageFolder.mkdirs();
        }

        logger.info( "Copying images: start" );
        String[] list = imagesFolder.list();
        Arrays.sort( list );

        if ( targetImageFolder.list().length != 0 && targetImageFolder.list().length == list.length ) {
            logger.info( "Images already exists... skipping copy" );
        } else {
            for ( String file : list ) {
                logger.info( String.format( "Processing %s start", file ) );

                if ( extractor.supportedTypes().contains( getExtension( file ) ) ) {

                    throw new UnsupportedOperationException( "just copy" );
                } else {

                    logger.info( "converting..." );
                    convert( new File( imagesFolder, file ), targetImageFolder, extractor.supportedTypes().get( 0 ) );
                }

                logger.info( String.format( "Processing %s end", file ) );

            }
        }

        logger.info( "Copying images: end" );
    }

}
