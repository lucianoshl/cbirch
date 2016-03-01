package br.edu.ufu.comp.pos.db.imageretrieval.framework.tree;


import java.util.List;

import br.edu.ufu.comp.pos.db.imageretrieval.clustering.birch.cftree.CFTree;
import br.edu.ufu.comp.pos.db.imageretrieval.dataset.image.OxfordImage;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.ClusterTree;
import br.edu.ufu.comp.pos.db.imageretrieval.pojo.ImageHits;


public class BirchTree implements ClusterTree {

    private CFTree tree;

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
        tree.index( img );
    }

    @Override
    public void finishBuild() {
        tree.finishedInsertingData();
    }

    @Override
    public void findTopK( OxfordImage query, int i ) {

        // TODO Auto-generated method stub
        
    }

    @Override
    public List< ImageHits > queryImage( OxfordImage queryImage ) {

        return tree.queryImage( queryImage );
    }

  

}
