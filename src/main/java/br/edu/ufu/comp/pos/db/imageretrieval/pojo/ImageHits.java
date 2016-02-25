package br.edu.ufu.comp.pos.db.imageretrieval.pojo;


public class ImageHits {

    private final Integer hits;

    private final int image;


    public ImageHits( Image image, Integer hits ) {
        this.image = image.getId();
        this.hits = hits;
    }


    public Integer getHits() {

        return hits;
    }


    public int getImage() {

        return image;
    }

}
