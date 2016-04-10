package br.edu.ufu.comp.pos.db.imageretrieval.dataset.image;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.function.Consumer;

import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.sift.Sift;
import lombok.Getter;

public class OxfordImage extends Image {

    public final File binaryFile;

    @Getter
    public final File image;

    @Getter
    public final long offset;

    @Getter
    public final long size;

    private Sift siftReader;

    public OxfordImage(File binaryFile, File image, long offset, long size, Sift siftReader) {
        super();
        this.binaryFile = binaryFile;
        this.image = image;
        this.offset = offset;
        this.size = size;
        this.siftReader = siftReader;
    }

    @Override
    public void scan(Consumer<double[]> c) {

        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(this.binaryFile, "r");
            randomAccessFile.seek(this.offset);

            byte[] buffer = new byte[128];
            for (int i = 0; i < size; i++) {
                randomAccessFile.read(buffer);
                c.accept(siftReader.extract(buffer));
            }
            randomAccessFile.close();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

}
