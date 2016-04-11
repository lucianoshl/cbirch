package br.edu.ufu.comp.pos.db.imageretrieval.dataset.image;

import java.io.File;

import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.sift.Sift;

public class OxfordImage extends Image {

    public OxfordImage(File binaryFile, File image, long offset, long size, Sift siftReader) {
        super(binaryFile, image, offset, size, siftReader);
    }

}
