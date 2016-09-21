package cbirch.dataset;


import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.sift.SiftScaled;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.function.BiConsumer;
import java.util.function.Consumer;


/**
 * Created by void on 9/12/16.
 */
public class Image {

    @Getter
    private final String imageName;

    private final long startPosition;

    private final long endPosition;

    private final int totalSifts;

    private final File siftBinary;


    public Image( String imageName, long startPosition, long endPosition, int totalSifts, File siftBinary ) {
        this.imageName = imageName;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.totalSifts = totalSifts;
        this.siftBinary = siftBinary;
    }


    public void scan( Consumer< double[] > lambda ) {

        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile( this.siftBinary, "r" );
            randomAccessFile.seek( this.startPosition );

            byte[] buffer = new byte[ 128 ];
            for ( int i = 0; i < this.totalSifts; i++ ) {
                randomAccessFile.read( buffer );
                lambda.accept( new SiftScaled().extract( buffer ) );
            }
            randomAccessFile.close();
        } catch ( IOException e ) {
            throw new IllegalStateException( e );
        }
    }


    @Override
    public boolean equals( Object o ) {

        if ( this == o )
            return true;
        if ( o == null || getClass() != o.getClass() )
            return false;

        Image image = (Image) o;

        return imageName.equals( image.imageName );

    }


    @Override
    public int hashCode() {

        return imageName.hashCode();
    }

    @Override
    public String toString() {
        return imageName;
    }
}
