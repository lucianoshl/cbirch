package cbirch.framework;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cbirch.clustering.ClusteringMethod;
import cbirch.clustering.TreeNode;
import cbirch.dataset.Dataset;
import cbirch.dataset.Image;


/**
 * Created by void on 9/21/16.
 */
public class TreeIndex extends Index {

    final static Logger logger = LoggerFactory.getLogger(TreeIndex.class);
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
                Set< Image > images = index.get( node );
                if ( images == null ) {
                    images = new HashSet< Image >();
                    index.put( node, images );
                }
                images.add( image );
            } );
            logger.info( String.format( "%s/%s", totalImages, position + 1 ) );
        } );
        logger.info( "Building index : end" );
    }


    @Override
    public Image[] find( Image query, int k ) {

        Set<Image> result = new HashSet<>();

//        HashMap<Image, Integer> imageCount = new HashMap<>();
        int[] imageCount = new int[dataset.getTotalImages()];

        query.scan((sift) -> {
            TreeNode closestCluster = tree.findClosestCluster(sift);
            Set<Image> imagesInNode = this.index.get(closestCluster);
            result.addAll(imagesInNode);
            for (Image image : imagesInNode) {
                imageCount[image.getId()]++;
            }
        });

        List<Image> realResult = new ArrayList<>(result);
        realResult.sort((a,b)->{
            return -1;
        });



        return realResult.toArray(new Image[ realResult.size() ]);
    }
}
