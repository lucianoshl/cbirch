package br.edu.ufu.comp.pos.db.imageretrieval.pojo;

import br.edu.ufu.comp.pos.db.imageretrieval.dataset.image.OxfordImage;

public class ImageHits {

    private final Integer hits;

    private final String image;


    public ImageHits( OxfordImage image, Integer hits ) {
        this.image = image.getImage().getName();
        this.hits = hits;
    }


    public Integer getHits() {

        return hits;
    }


    public String getImage() {

        return image;
    }

}
