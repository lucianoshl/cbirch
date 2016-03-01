package br.edu.ufu.comp.pos.db.imageretrieval.framework.tree;


import java.util.ArrayList;
import java.util.List;

import br.edu.ufu.comp.pos.db.imageretrieval.clustering.birch.cftree.CFTree;
import br.edu.ufu.comp.pos.db.imageretrieval.dataset.image.OxfordImage;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.ClusterTree;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.Histogram;
import br.edu.ufu.comp.pos.db.imageretrieval.pojo.ImageHits;


public class BirchTree implements ClusterTree {

    private CFTree tree;
    
    private List<Histogram> histograms = new ArrayList<Histogram>();


    public BirchTree( int branchingFactor, double threshold, int memory ) {
        this.tree = new CFTree( branchingFactor, threshold, 1, true );
        this.tree.setAutomaticRebuild( true );
        this.tree.setMemoryLimitMB( memory );
    }


    @Override
    public void insertEntry( double[] sift ) {

        tree.insertEntry( sift );

    }


    @Override
    public void optimize() {

        tree.rebuildTree();
        tree.rebuildTree();
    }


    @Override
    public void index( OxfordImage img ) {
        histograms.add( getHistogram( img ) );
    }

    @Override
    public Histogram getHistogram( OxfordImage img ) {
        Histogram histogram = new Histogram(img,tree.getWordsSize());
        img.scan( ( sift ) -> histogram.count( tree.findClosestCluster( sift ) ) );
        return histogram;
    }

    @Override
    public void finishBuild() {

        tree.finishedInsertingData();
    }


    @Override
    public List< Histogram > findTopK( OxfordImage queryImage, int k ) {

        Histogram query = getHistogram( queryImage );
        
        this.histograms.sort( (a,b) -> Double.compare(query.distance( a ),query.distance( b )) );
        
        return this.histograms.subList( 0, k );

    }


    @Override
    public List< ImageHits > queryImage( OxfordImage queryImage ) {

        return tree.queryImage( queryImage );
    }

}
