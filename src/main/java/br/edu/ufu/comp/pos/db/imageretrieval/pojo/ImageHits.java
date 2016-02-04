package br.edu.ufu.comp.pos.db.imageretrieval.pojo;

public class ImageHits {

	private final Integer hits;
	private final Image image;

	public ImageHits(Image image, Integer hits) {
		this.image = image;
		this.hits = hits;
	}

	public Integer getHits() {
		return hits;
	}

	public Image getImage() {
		return image;
	}

}
