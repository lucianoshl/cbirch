package br.edu.ufu.comp.pos.db.imageretrieval.pojo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.function.Consumer;

public class Image {

	private int id;
	private File image;
	private File sift;

	public Image(int id, File image, File sift) {
		this.id = id;
		this.image = image;
		this.sift = sift;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public File getImage() {
		return image;
	}

	public void setImage(File image) {
		this.image = image;
	}

	public File getSift() {
		return sift;
	}

	public void setSift(File sift) {
		this.sift = sift;
	}

	public void scan(Consumer<double[]> c) {
		try {
			byte[] buffer = new byte[128];
			FileInputStream binFileReader = new FileInputStream(sift);
			while (binFileReader.read(buffer) != -1) {
				c.accept(convertToDouble(buffer));
			}
			binFileReader.close();
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	private static double[] convertToDouble(byte[] buffer) {
		double[] result = new double[buffer.length];
		for (int i = 0; i < buffer.length; i++) {
			result[i] = buffer[i];
		}
		return result;
	}
	
	@Override
	public String toString() {
		return image.getName();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		if (id != other.id)
			return false;
		return true;
	}
	
	

}
