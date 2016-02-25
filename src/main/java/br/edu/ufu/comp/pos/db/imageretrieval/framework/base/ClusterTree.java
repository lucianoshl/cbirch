package br.edu.ufu.comp.pos.db.imageretrieval.framework.base;

import java.util.List;

import br.edu.ufu.comp.pos.db.imageretrieval.pojo.Image;
import br.edu.ufu.comp.pos.db.imageretrieval.pojo.ImageHits;

public interface ClusterTree {

    public void insertEntry( double[] sift );

    public void optimize();

    public void index( Image img );

    public void finishBuild();

    public void findTopK( Image query, int i );

    public List< ImageHits > queryImage( Image queryImage );

}
