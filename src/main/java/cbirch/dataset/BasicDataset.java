package cbirch.dataset;


import br.edu.ufu.comp.pos.db.imageretrieval.commons.Utils;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.map.MapCalculator;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.map.OxfordMapCalculator;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.sift.Sift;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.sift.SiftScaled;
import lombok.Setter;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static java.util.Arrays.asList;


/**
 * Created by void on 9/11/16.
 */
public class BasicDataset extends Dataset {

    final Logger logger = LoggerFactory.getLogger( BasicDataset.class );

    private static final OxfordMapCalculator MAP_CALCULATOR = OxfordMapCalculator.builder()//
        .ignore( asList( "junk" ) )//
        .positive( asList( "ok", "good" ) )//
        .negative( asList( "absent" ) )//
        .build();

    private final File siftPositions;

    private final File siftBinary;

    private final File gtFolder;


    public BasicDataset( String datasetName ) {
        File base = Utils.getDatesetPath( System.getenv().get( "cbirch_workspace" ), datasetName );
        this.siftPositions = new File( base, "sift.positions" );
        this.siftBinary = new File( base, "sift.binary" );
        this.gtFolder = new File( base, "gt_files" );
    }

    @Override
    @SneakyThrows
    public void scanAllFeatures( BiConsumer< double[], Integer > lambda ) {

        int totalSifts = getTotalFeatures();

        RandomAccessFile randomAccessFile = new RandomAccessFile( this.siftBinary, "r" );

        List<Integer> siftOrder = new ArrayList<>();

        for (int i = 0; i < totalSifts; i++) {
            siftOrder.add(i);
        }

        siftOrder = siftOrderReader.apply(siftOrder);

        Sift sift = new SiftScaled();

        logger.debug( "Reading binary file: start" );
        byte[] buffer = new byte[ 128 ];

        for (int i = 0; i < siftOrder.size(); i++) {
            randomAccessFile.seek(siftOrder.get(i) * buffer.length);
            randomAccessFile.read( buffer );
            double[] extracted = sift.extract( buffer );
            lambda.accept( extracted, i );
            logger.trace( Arrays.toString( extracted ) );
            logger.trace( String.format( "%s/%s", i + 1, extracted.length ) );
        }

        logger.debug( "Reading binary file: end" );
    }


    @Override
    public int getTotalFeatures() {

        return (int) ( this.siftBinary.length() / 128 );
    }


    @Override
    @SneakyThrows
    public void scanAllImages( BiConsumer< Image, Integer > lambda ) {

        List< String > lines = FileUtils.readLines( this.siftPositions );
        for ( int i = 0; i < lines.size(); i++ ) {
            String line = lines.get( i );
            String[] information = line.split( "\t" );

            String imageName = information[ 0 ];
            long startPosition = Long.valueOf( information[ 1 ] );
            long endPosition = Long.valueOf( information[ 2 ] );
            int totalSifts = Integer.valueOf( information[ 3 ] );

            Image image = new Image( i ,imageName, startPosition, endPosition, totalSifts, siftBinary );
            lambda.accept( image, i );
        }
    }


    @Override
    public String[] getTestClasses() {

        File[] files = this.gtFolder.listFiles();
        Set< String > result = new HashSet<>();
        for ( File file : files ) {
            if ( file.getName().contains( "outlier" ) ) {
                continue;
            }
            String className = file.getName().replace( "-good.txt", "" ).replace( "-junk.txt", "" ).replace( "-ok.txt", "" ).replace( "-queries.txt", "" );
            result.add( className );
        }

        return result.toArray( new String[ result.size() ] );
    }


    @Override
    @SneakyThrows
    public Image[] getQueries( String testClass ) {

        String[] imageNamesArray = FileUtils.readFileToString( new File( this.gtFolder, testClass + "-queries.txt" ) ).split( "\n" );
        HashSet< String > imageNames = new HashSet<>( Arrays.asList( imageNamesArray ) );

        List< Image > result = new ArrayList<>();

        this.scanAllImages( ( img, position ) -> {
            if ( imageNames.contains( img.getImageName() ) ) {
                result.add( img );
            }
        } );

        return result.toArray( new Image[ result.size() ] );
    }


    @Override
    @SneakyThrows
    public String quality( String clazz, Image query, Image result ) {

        String[] goods = FileUtils.readFileToString( new File( this.gtFolder, clazz + "-good.txt" ) ).split( "\n" );
        if ( Arrays.asList( goods ).contains( result.getImageName() ) ) {
            return "good";
        }
        String[] oks = FileUtils.readFileToString( new File( this.gtFolder, clazz + "-ok.txt" ) ).split( "\n" );
        if ( Arrays.asList( oks ).contains( result.getImageName() ) ) {
            return "ok";
        }

        String[] junks = FileUtils.readFileToString( new File( this.gtFolder, clazz + "-junk.txt" ) ).split( "\n" );
        if ( Arrays.asList( junks ).contains( result.getImageName() ) ) {
            return "junk";
        }

        return "absent";
    }


    @Override
    public MapCalculator getMapCalculator() {

        return MAP_CALCULATOR;
    }


    @Override
    @SneakyThrows
    public int getTotalImages() {
        return FileUtils.readLines(this.siftPositions).size();
    }
}
