package br.edu.ufu.comp.pos.db.imageretrieval.framework.base;

import java.util.List;

import br.edu.ufu.comp.pos.db.imageretrieval.dataset.image.OxfordImage;
import br.edu.ufu.comp.pos.db.imageretrieval.pojo.ImageHits;

public interface ClusterTree {

    void insertEntry( double[] sift );

    void optimize();

    void index( OxfordImage img );

    void finishBuild();

    void findTopK( OxfordImage query, int i );

    List< ImageHits > queryImage( OxfordImage queryImage );

}
