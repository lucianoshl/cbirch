package cbirch.dataset;


import br.edu.ufu.comp.pos.db.imageretrieval.commons.Utils;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.map.MapCalculator;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.map.OxfordMapCalculator;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.BiConsumer;

import static java.util.Arrays.asList;


/**
 * Created by void on 9/24/16.
 */
public class OxfordDataset extends BinaryDataset {

    private static final OxfordMapCalculator MAP_CALCULATOR = OxfordMapCalculator.builder()//
        .ignore( asList( "junk" ) )//
        .positive( asList( "ok", "good" ) )//
        .negative( asList( "absent" ) )//
        .build();

    private final File datasetFolder;

    private final File imageFolder;

    private final File orderInBinaryFile;

    private final Map< String, Image > imageIndex = new HashMap<>();


    @SneakyThrows
    public OxfordDataset() {
        super( "oxford", "feat_oxc1_hesaff_sift.bin", "word_oxc1_hesaff_sift_16M_1M" );
        this.datasetFolder = Utils.getDatesetPath( System.getenv().get( "cbirch_workspace" ), "oxford" );
        if ( !this.datasetFolder.exists() ) {
            throw new FileNotFoundException( this.datasetFolder.getAbsolutePath() );
        }
        this.imageFolder = new File( datasetFolder, "images" );
        this.gtFolder = new File( datasetFolder, "gt_files" );
        this.orderInBinaryFile = new File( this.datasetFolder, "README2.txt" );
    }


    @Override
    @SneakyThrows
    public int getTotalFeatures() {

        int total = 0;

        File[] files = this.siftPositions.listFiles();
        for ( File file : files ) {
            BufferedReader reader = new BufferedReader( new FileReader( file ) );
            reader.readLine();
            total += Integer.valueOf( reader.readLine() );
        }

        return total;
    }


    @Override
    @SneakyThrows
    public void scanAllImages( BiConsumer< Image, Integer > lambda ) {

        Map< String, Long > aux = new HashMap< String, Long >();
        final long[] startPosition = { 0 };
        scanOrderFile( ( imageName, i ) -> {
            long siftSize = getImageSiftSize( imageName );
            Image image = new Image( i, imageName, startPosition[ 0 ], startPosition[ 0 ] + siftSize, new Long( siftSize ).intValue(), siftBinary );
            lambda.accept( image, i );
            startPosition[ 0 ] += siftSize;
        } );
    }


    @Override
    public String[] getTestClasses() {

        File[] files = this.gtFolder.listFiles();
        HashSet< String > uniq = new HashSet< String >();
        for ( File file : files ) {
            uniq.add( file.getName().split( "_\\d" )[ 0 ] );
        }
        String[] result = uniq.toArray( new String[ uniq.size() ] );
        Arrays.sort( result );
        return result;
    }


    @Override
    @SneakyThrows
    public Image[] getQueries( String testClass ) {

        List< Image > result = new ArrayList<>();
        File[] files = this.gtFolder.listFiles();
        Arrays.sort( files );
        for ( File file : files ) {
            if ( file.getName().contains( testClass ) && file.getName().contains( "query" ) ) {

                String imageName = FileUtils.readFileToString( file ).split( " " )[ 0 ];

                result.add( (Image) this.cache().get( imageName.replace( "oxc1_", "" ) ) );
            }
        }
        return result.toArray( new Image[ result.size() ] );
    }


    protected Map< String, Image > cache() {

        if ( this.imageIndex.isEmpty() ) {

            this.scanAllImages( ( image, i ) -> {
                imageIndex.put( image.getImageName().split( "\\." )[ 0 ], image );
            } );
        }
        return this.imageIndex;
    }


    @Override
    @SneakyThrows
    public String quality( String clazz, Image query, Image result ) {

//        File queryFile = this.queryFiles.get( query.getImageName() );


        File queryFile = null;
        File[] files = this.gtFolder.listFiles();
        for ( File file : files ) {
            if ( file.getName().contains( "query" ) && FileUtils.readFileToString( file ).contains( query.getImageName().split( "\\." )[ 0 ] ) ) {
                queryFile = file;
                break;
            }
        }

        String quality = "absent";

        quality = checkIs( result, queryFile.getName(), "good", quality );
        quality = checkIs( result, queryFile.getName(), "junk",quality );
        quality = checkIs( result, queryFile.getName(), "ok", quality );

        return quality;
    }


    @Override
    public MapCalculator getMapCalculator() {

        return MAP_CALCULATOR;
    }


    @Override
    public int getTotalImages() {

        return this.imageFolder.listFiles().length;
    }


    private void scanOrderFile( BiConsumer< String, Integer > c ) {

        Scanner scanner = Utils.createScanner( orderInBinaryFile );
        int i = 0;
        while ( scanner.hasNextLine() ) {
            String fileName = scanner.nextLine().replace( "oxc1_", "" ) + ".jpg";
            c.accept( fileName, i );
            i++;
        }
        scanner.close();
    }


    private long getImageSiftSize( String fileName ) {

        File siftSizeFile = new File( siftPositions, "oxc1_" + fileName.replace( ".jpg", ".txt" ) );
        Scanner createScanner = Utils.createScanner( siftSizeFile );
        createScanner.nextLine();
        long result = createScanner.nextLong();
        createScanner.close();
        return result;
    }


    private String checkIs( Image imgName, String queryFile, String quality, String result ) {

        List< String > r = Utils.readLines( new File( this.gtFolder, queryFile.replace( "query.txt", quality + ".txt" ) ).getAbsolutePath() );
        if ( r.contains( imgName.getImageName().replaceAll( ".jpg", "" ) ) ) {
            return quality;
        }
        return result;
    }
}
