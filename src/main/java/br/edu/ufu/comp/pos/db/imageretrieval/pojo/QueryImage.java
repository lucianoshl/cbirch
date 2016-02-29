package br.edu.ufu.comp.pos.db.imageretrieval.pojo;


import java.util.List;


public class QueryImage {

    private final Image image;

    private final List< String > similar;


    public QueryImage( Image image, List< String > m ) {
        super();
        this.image = image;
        this.similar = m;
    }


    public Image getImage() {

        return image;
    }


    public List< String > getSimilar() {

        return similar;
    }

}
