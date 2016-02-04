package br.edu.ufu.comp.pos.db.imageretrieval.pojo;

import java.util.List;

public class QueryResult {

	private final Image query;
	private final List<ImageHits> queryResult;

	public QueryResult(Image queryImage, List<ImageHits> queryResult) {
		this.query = queryImage;
		this.queryResult = queryResult;
	}

	public Image getQuery() {
		return query;
	}

	public List<ImageHits> getQueryResult() {
		return queryResult;
	}

}
