package br.edu.ufu.comp.pos.db.imageretrieval.framework;

import br.edu.ufu.comp.pos.db.imageretrieval.dataset.image.Image;
import lombok.Getter;

@Getter
public class QueryResultItem {

	private String image;
	private String classification;

	public QueryResultItem(Image image, String classification) {
		this.image = image.getImage().getAbsolutePath();
		this.classification = classification;
	}
}
