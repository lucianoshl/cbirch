package cbirch.framework;


import cbirch.clustering.ClusteringMethod;
import cbirch.clustering.TreeNode;
import cbirch.dataset.Dataset;
import cbirch.dataset.Image;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.math3.ml.distance.CosineSimilarity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * Created by void on 9/12/16.
 */
public class HistogramIndex extends Index {

    final static Logger logger = LoggerFactory.getLogger( HistogramIndex.class );

    private final Map< Image, File > histograms = new HashMap<>();

    private double[] idf;

    private int totalWords;


    protected HistogramIndex( Dataset dataset ) {
        super( dataset );
    }


    @Override
    public void build( ClusteringMethod clustering ) {

        logger.info( "Building index : start" );
        this.totalWords = clustering.getClusterSize();

        int totalImages = dataset.getTotalImages();
        dataset.scanAllImages( ( image, position ) -> {
            int[] histogram = new int[ totalWords ];
            image.scan( ( sift ) -> {
                TreeNode node = clustering.findClosestCluster( sift );
                Set< Image > images = index.get( node );
                if ( images == null ) {
                    images = new HashSet< Image >();
                    index.put( node, images );
                }
                images.add( image );
                histogram[ node.getId() ] += 1;
            } );
            storeHistogram( image, histogram );
            logger.info( String.format( "%s/%s", totalImages, position + 1 ) );
        } );
        calcAllIdfs();
        logger.info( "Building index : end" );
    }


    @SneakyThrows
    private void storeHistogram( Image image, int[] histogram ) {

        File tempFile = File.createTempFile( image.getImageName(), String.valueOf( System.currentTimeMillis() ) );
        tempFile.deleteOnExit();
        histograms.put( image, tempFile );
        FileUtils.writeByteArrayToFile( tempFile, SerializationUtils.serialize( histogram ) );
    }


    @Override
    @SneakyThrows
    public Image[] find( Image query, int k ) {

        int[] histogram = SerializationUtils.deserialize( FileUtils.readFileToByteArray( histograms.get( query ) ) );

        double[] queryHistogram = normalize( histogram );

        List< Tuple< Image, Double > > sortList = new ArrayList< Tuple< Image, Double > >();
        Set< Image > images = this.histograms.keySet();
        for ( Image image : images ) {
            int[] originHistogram = SerializationUtils.deserialize( FileUtils.readFileToByteArray( histograms.get( image ) ) );
            double[] normalizedHistogram = normalize( originHistogram );
            sortList.add( new Tuple( image, distance( normalizedHistogram, queryHistogram ) ) );
        }

        // Ordenação deve ser decrescente por se for perto de 1 é mais similar
        sortList.sort( ( a, b ) -> {
            return b.getSecond().compareTo( a.getSecond() );
        } );

        List< Image > listResult = sortList.subList( 0, k ).stream().map( ( a ) -> a.getFirst() ).collect( Collectors.toList() );
        return listResult.toArray( new Image[ listResult.size() ] );
    }


    private double distance( double[] normalizedHistogram, double[] query ) {

        return new CosineSimilarity().compute( normalizedHistogram, query );
    }


    public double[] normalize( int[] content ) {

        double[] result = new double[ content.length ];
        for ( int i = 0; i < content.length; i++ ) {
            if ( content[ i ] != 0 ) {
                result[ i ] = content[ i ] * tf( i, content ) * idf( i );
                // result[i] = content[i];
            }
        }
        return result;
    }


    public double idf( int word ) {

        return idf[ word ];

    }


    @SneakyThrows
    private void calcAllIdfs() {

        idf = new double[ this.totalWords ];

        Collection< File > documents = histograms.values();
        int documentsSize = documents.size();

        Set< TreeNode > treeNodes = this.index.keySet();
        for ( TreeNode treeNode : treeNodes ) {
            idf[ treeNode.getId() ] = this.index.get( treeNode ).size();
        }

        int emptyNode = 0;
        int nodeSize = this.index.keySet().size();
        for ( int i = 0; i < idf.length; i++ ) {
            // inverse document frequency
            if ( idf[ i ] != 0 ) {
                idf[ i ] = Math.log( Double.valueOf( documentsSize ) / idf[ i ] );
            } else {
                emptyNode++;
            }
        }

        logger.info( String.format( "We have %s empty nodes and %s not empty. Total = %s", emptyNode, this.totalWords - emptyNode, this.totalWords ) );

    }


    private double tf( int word, int[] content ) {

        int maxOcurrence = 0;
        for ( int i : content ) {
            if ( i > maxOcurrence ) {
                maxOcurrence = i;
            }
        }

        return content[ word ] / Double.valueOf( maxOcurrence );
    }
}
