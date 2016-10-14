package cbirch.framework;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cbirch.clustering.ClusteringMethod;
import cbirch.clustering.TreeNode;
import cbirch.dataset.Dataset;
import cbirch.dataset.Image;


/**
 * Created by void on 9/21/16.
 */
public class TreeIndex extends Index< List< Tuple< Image, Integer > > > {

    final static Logger logger = LoggerFactory.getLogger( TreeIndex.class );

    private ClusteringMethod tree;


    protected TreeIndex( Dataset dataset ) {
        super( dataset );
    }


    @Override
    public void build( ClusteringMethod clustering ) {

        this.tree = clustering;
        logger.info( "Building index : start" );

        int totalImages = dataset.getTotalImages();
        dataset.scanAllImages( ( image, position ) -> {
            image.scan( ( sift ) -> {
                TreeNode node = clustering.findClosestCluster( sift );
                List< Tuple< Image, Integer > > images = index.get( node );
                if ( images == null ) {
                    images = new ArrayList< Tuple< Image, Integer > >();
                    index.put( node, images );
                }

                // List<Tuple<Image, Integer>> related =
                // images.stream().filter((img) ->
                // image.equals(img)).collect(Collectors.toList());

                List< Image > collect = images.stream().map( ( a ) -> a.getFirst() ).collect( Collectors.toList() );
                int index = collect.indexOf( image );

                if ( !collect.contains( image ) ) {
                    images.add( new Tuple< Image, Integer >( image, 1 ) );
                } else {
                    images.get( index ).setSecond( images.get( index ).getSecond() + 1 );
                }
            } );
            logger.info( String.format( "%s/%s", totalImages, position + 1 ) );
        } );
        logger.info( "Building index : end" );
    }


    @Override
    public Image[] find( Image query, int k ) {

        Set< Image > result = new HashSet<>();

        int[] imageCount = new int[ dataset.getTotalImages() ];

        Map< TreeNode, Integer > queryWordCount = new HashMap<>();



        query.scan( ( sift ) -> {
            TreeNode closestCluster = tree.findClosestCluster( sift );
            Integer counter = queryWordCount.get( closestCluster );
            if ( counter == null ) {
                counter = 0;
            }
            queryWordCount.put( closestCluster, counter + 1 );
        } );

        Set<Image> candidateResult = new HashSet<Image>();
        Set<TreeNode> treeNodes = index.keySet();
        for (TreeNode treeNode : treeNodes) {
            List<Tuple<Image, Integer>> tuples = index.get(treeNode);
            for (Tuple<Image, Integer> tuple : tuples) {
                candidateResult.add(tuple.getFirst());
            }
        }

        List <Image> candidateResultList = new ArrayList<>(candidateResult);

        ArrayList words = new ArrayList(index.keySet());

//        double[][] counters = new double[candidateResultList.size()][words.size()];
//        for (int i = 0; i < counters.length; i++) {
//            Image image = candidateResultList.get(i);
//            for (int j = 0; j < counters[i].length; j++) {
//                List<Tuple<Image, Integer>> imagesInWord = this.index.get(words.get(i));
//                int index = imagesInWord.stream().map((a) -> a.getFirst()).collect(Collectors.toList()).indexOf(image);
//                Tuple<Image, Integer> imageIntegerTuple = imagesInWord.get(index);
//                counters[i][j] = index == -1 ? 0 : imageIntegerTuple.getSecond();
//                double idf = Math.log( Double.valueOf( candidateResultList.size() ) / imagesInWord.size() );
//                double tf = imageIntegerTuple.getSecond() * words.size();
//
//                counters[i][j] = counters[i][j] * idf * tf;
//
//            }
//        }

        List< Tuple< Image, Double > > counters = new ArrayList<>();
        for ( int i = 0; i < candidateResultList.size(); i++ ) {
            Image image = candidateResultList.get( i );
            Tuple< Image, Double > tuple = new Tuple< Image, Double >( image, Double.valueOf(0) );
            double[] counter = new double[words.size()];
            for ( int j = 0; j < counter.length; j++ ) {
                List< Tuple< Image, Integer > > imagesInWord = this.index.get( words.get( i ) );
                int index = imagesInWord.stream().map( ( a ) -> a.getFirst() ).collect( Collectors.toList() ).indexOf( image );
                counter[ j ] = index == -1 ? 0 : imagesInWord.get( index ).getSecond();
                double idf = Math.log( Double.valueOf( candidateResultList.size() ) / imagesInWord.size() );
                double tf = counter[ j ] * words.size();

                counter[ j ] = counter[ j ] * idf * tf;

            }
            tuple.setSecond(computeSumValue(counter));
            counters.add( tuple );
        }

        counters.sort( ( a, b ) -> {
            return b.getSecond().compareTo(a.getSecond());
        } );

        List< Image > realResult = new ArrayList<>();
        for ( int i = 0; i < k; i++ ) {
            realResult.add( counters.get( i ).getFirst() );
        }

        return realResult.toArray( new Image[ realResult.size() ] );
    }

    private double computeSumValue(double[] counter) {
        double result = 0;
//        double[] counter = tuple.getSecond();
        for (int i = 0; i < counter.length; i++) {
            result += counter[i];
        }
        return result;
    }
}
