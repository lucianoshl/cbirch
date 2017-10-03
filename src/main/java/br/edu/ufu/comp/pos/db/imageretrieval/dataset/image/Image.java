package br.edu.ufu.comp.pos.db.imageretrieval.dataset.image;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.function.Consumer;

import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.sift.Sift;
import lombok.Getter;

public class Image {

	public final File binaryFile;

	@Getter
	public final File image;

	@Getter
	public final long offset;

	@Getter
	public final long size;

	private Sift siftReader;

	public Image(File binaryFile, File image, long offset, long size, Sift siftReader) {
		super();
		this.binaryFile = binaryFile;
		this.image = image;
		this.offset = offset;
		this.size = size;
		this.siftReader = siftReader;
	}

	public void scan(Consumer<double[]> c) {

		try {
			RandomAccessFile randomAccessFile = new RandomAccessFile(this.binaryFile, "r");
			randomAccessFile.seek(this.offset);

			byte[] buffer = new byte[128];
			for (int i = 0; i < size; i++) {
				randomAccessFile.read(buffer);
				double[] result = siftReader.extract(buffer);
//				System.out.println(Arrays.toString(result));
				c.accept(result);
			}
			randomAccessFile.close();
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((image == null) ? 0 : image.getName().hashCode());
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
		Image other = (Image) obj;
		if (image == null) {
			if (other.image != null)
				return false;
		} else if (!image.getName().equals(other.image.getName()))
			return false;
		return true;
	}

}
