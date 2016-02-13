package br.edu.ufu.comp.pos.db.imageretrieval.pojo;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.StopWatch;

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

}
