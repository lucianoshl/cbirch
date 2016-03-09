package br.edu.ufu.comp.pos.db.imageretrieval.dataset.image;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.function.Consumer;

import br.edu.ufu.comp.pos.db.imageretrieval.commons.Utils;
import lombok.Getter;

public class OxfordImage extends Image {

    public final File binaryFile;

    @Getter
    public final File image;

    public final long offset;

    public final long size;

    public OxfordImage(File binaryFile, File image, long offset, long size) {
        super();
        this.binaryFile = binaryFile;
        this.image = image;
        this.offset = offset;
        this.size = size;
    }

    @Override
    public void scan(Consumer<double[]> c) {

        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(this.binaryFile, "r");
            randomAccessFile.seek(this.offset);

            byte[] buffer = new byte[128];
            for (int i = 0; i < size; i++) {
                randomAccessFile.read(buffer);
                c.accept(Utils.convertToDouble(buffer));
            }
            randomAccessFile.close();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

}
