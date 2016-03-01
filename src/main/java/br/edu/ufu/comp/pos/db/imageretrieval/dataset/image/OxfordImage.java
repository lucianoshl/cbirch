package br.edu.ufu.comp.pos.db.imageretrieval.dataset.image;


import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.function.Consumer;

import br.edu.ufu.comp.pos.db.imageretrieval.commons.Utils;


public class OxfordImage {

    public final File binaryFile;

    public final File image;

    public final long offset;

    public final long size;


    public OxfordImage( File binaryFile, File image, long offset, long size ) {
        super();
        this.binaryFile = binaryFile;
        this.image = image;
        this.offset = offset;
        this.size = size;
    }


    public File getImage() {

        return image;
    }


    public void scan( Consumer< double[] > c ) {

        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile( this.binaryFile, "r" );
            randomAccessFile.seek( this.offset );

            byte[] buffer = new byte[ 128 ];
            for ( int i = 0; i < size; i++ ) {
                randomAccessFile.read( buffer );
                c.accept( Utils.convertToDouble( buffer ) );
            }
            randomAccessFile.close();
        } catch ( IOException e ) {
            throw new IllegalStateException( e );
        }
    }


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((image == null) ? 0 : image.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OxfordImage other = (OxfordImage) obj;
		if (image == null) {
			if (other.image != null)
				return false;
		} else if (!image.equals(other.image))
			return false;
		return true;
	}
    
    

}
