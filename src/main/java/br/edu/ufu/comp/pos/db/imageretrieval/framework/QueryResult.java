package br.edu.ufu.comp.pos.db.imageretrieval.framework;

import java.util.ArrayList;
import java.util.List;

import br.edu.ufu.comp.pos.db.imageretrieval.dataset.image.Image;
import lombok.Getter;

@Getter
public class QueryResult {

	List<QueryResultItem> results = new ArrayList<QueryResultItem>();
	private String query;
	
	public QueryResult(Image query) {
		this.query = query.getImage().getAbsolutePath();
	}

	public void add(QueryResultItem item) {
		results.add(item);
		
	}

}
