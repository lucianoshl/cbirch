package br.edu.ufu.comp.pos.db.imageretrieval.pojo;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JWindow;

import org.apache.commons.lang3.time.StopWatch;

import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;

import br.edu.ufu.comp.pos.db.imageretrieval.commons.Mapper;

public class ResultReport {

	private Date startAt;
	private Date endAt;

	private int branchingFactor;

	private double threshold;
	private double finalThreshold;

	private long memory;
	
	private List<QueryResult> queryResults;
	
	private Map<String, Long> benchmarks = new HashMap<String, Long>();
	
	private Map<Integer,String> images = new HashMap<Integer, String>();
	
	private String datasetBasePath;

	public void benchmark(String label,Mapper a){
		System.out.println("Start:" + label);
		StopWatch clock = new StopWatch();
		clock.start();
		a.action();
		clock.stop();
		this.benchmarks.put(label, clock.getTime());
		System.out.println("Stop:" + label);
	}
	
	public void addImage(Image img){
		this.images.put(img.getId(), img.getImage().getName());
	}


	public String getDatasetBasePath() {
		return datasetBasePath;
	}

	public void setDatasetBasePath(String datasetBasePath) {
		this.datasetBasePath = datasetBasePath;
	}

	public Date getStartAt() {
		return startAt;
	}

	public void setStartAt(Date startAt) {
		this.startAt = startAt;
	}

	public Date getEndAt() {
		return endAt;
	}

	public void setEndAt(Date endAt) {
		this.endAt = endAt;
	}

	public int getBranchingFactor() {
		return branchingFactor;
	}

	public void setBranchingFactor(int branchingFactor) {
		this.branchingFactor = branchingFactor;
	}

	public double getThreshold() {
		return threshold;
	}

	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}

	public long getMemory() {
		return memory;
	}

	public void setMemory(long memory) {
		this.memory = memory;
	}

	public List<QueryResult> getQueryResults() {
		return queryResults;
	}

	public void setQueryResults(List<QueryResult> queryResults) {
		this.queryResults = queryResults;
	}

	public double getFinalThreshold() {
		return finalThreshold;
	}

	public void setFinalThreshold(double finalThreshold) {
		this.finalThreshold = finalThreshold;
	}

	public void writeJson(JsonWriter jwriter) throws IOException {
		Gson gson = new Gson();
		jwriter.beginObject();
//		jwriter.setHtmlSafe(false);
		
		putItem(jwriter, gson, "startAt",startAt);
		putItem(jwriter, gson, "endAt",endAt);
		putItem(jwriter, gson, "branchingFactor",branchingFactor);
		putItem(jwriter, gson, "threshold",threshold);
		putItem(jwriter, gson, "finalThreshold",finalThreshold);
		putItem(jwriter, gson, "memory",memory);
		putItem(jwriter, gson, "benchmarks",benchmarks);
		putItem(jwriter, gson, "images",images);
		putItem(jwriter, gson, "datasetBasePath",datasetBasePath);
		
		
		jwriter.name("queryResults");
		jwriter.beginArray();
		for (QueryResult queryResult : queryResults) {
			jwriter.beginObject();
			jwriter.name("query").value(queryResult.getQuery());
			jwriter.name("queryResult");
			jwriter.beginArray();
			List<ImageHits> rank = queryResult.getQueryResult();
			for (ImageHits imageHits : rank) {
				gson.toJson(imageHits,imageHits.getClass(),jwriter);
			}
			jwriter.endArray();
			jwriter.endObject();
		}
		jwriter.endArray();
		
		
		jwriter.endObject();
		
	}

	private void putItem(JsonWriter jwriter, Gson gson, String name,Object obj) throws IOException {
		jwriter.name(name);
		gson.toJson(obj,obj.getClass(),jwriter);
	}

}
