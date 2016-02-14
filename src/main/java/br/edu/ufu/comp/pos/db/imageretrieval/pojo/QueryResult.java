package br.edu.ufu.comp.pos.db.imageretrieval.pojo;

import java.util.List;

public class QueryResult {

	private final int query;
	private final List<ImageHits> queryResult;

	public QueryResult(Image queryImage, List<ImageHits> queryResult) {
		this.query = queryImage.getId();
		this.queryResult = queryResult.subList(0, 20);
	}

	public int getQuery() {
		return query;
	}

	public List<ImageHits> getQueryResult() {
		return queryResult;
	}

}
