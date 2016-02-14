package br.edu.ufu.comp.pos.db.imageretrieval.pojo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.time.StopWatch;

import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.context.MapValueResolver;
import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;

import br.edu.ufu.comp.pos.db.imageretrieval.Launcher;
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

	private Map<Integer, String> images = new HashMap<Integer, String>();

	private String datasetBasePath;

	public void benchmark(String label, Mapper a) {
		System.out.println("Start:" + label);
		StopWatch clock = new StopWatch();
		clock.start();
		a.action();
		clock.stop();
		this.benchmarks.put(label, clock.getTime());
		System.out.println("Stop:" + label);
	}

	public void addImage(Image img) {
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
		// jwriter.setHtmlSafe(false);

		putItem(jwriter, gson, "startAt", startAt);
		putItem(jwriter, gson, "endAt", endAt);
		putItem(jwriter, gson, "branchingFactor", branchingFactor);
		putItem(jwriter, gson, "threshold", threshold);
		putItem(jwriter, gson, "finalThreshold", finalThreshold);
		putItem(jwriter, gson, "memory", memory);
		putItem(jwriter, gson, "benchmarks", benchmarks);
		putItem(jwriter, gson, "images", images);
		putItem(jwriter, gson, "datasetBasePath", datasetBasePath);

		jwriter.name("queryResults");
		jwriter.beginArray();
		for (QueryResult queryResult : queryResults) {
			jwriter.beginObject();
			jwriter.name("query").value(queryResult.getQuery());
			jwriter.name("queryResult");
			jwriter.beginArray();
			List<ImageHits> rank = queryResult.getQueryResult();
			for (ImageHits imageHits : rank) {
				gson.toJson(imageHits, imageHits.getClass(), jwriter);
			}
			jwriter.endArray();
			jwriter.endObject();
		}
		jwriter.endArray();

		jwriter.endObject();

	}

	private void putItem(JsonWriter jwriter, Gson gson, String name, Object obj) throws IOException {
		jwriter.name(name);
		gson.toJson(obj, obj.getClass(), jwriter);
	}

	public void save(String resultFolderPath) throws IOException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HH:mm:ss");
		String fileName = dateFormat.format(new Date());
		File outJsonFile = generateJsonFile(this, resultFolderPath, fileName);
		File outHtmlFile = createHtmlFile(resultFolderPath, fileName, outJsonFile);

		System.out.println("saved in");
		System.out.println(outHtmlFile);
		System.out.println(outJsonFile);
	}

	private File createHtmlFile(String resultFolderPath, String fileName, File outJsonFile) throws IOException {
		File outHtmlFile = new File(resultFolderPath, fileName + ".html");

		Map<String, String> map = new HashMap<String, String>();
		map.put("libsPath", Launcher.class.getClassLoader().getResource("templates/libs").getFile());
		map.put("jsonName", outJsonFile.getAbsolutePath());

		Handlebars handlebars = new Handlebars();
		handlebars.setStartDelimiter("<%");
		handlebars.setEndDelimiter("%>");

		Context context = Context.newBuilder(map).resolver(MapValueResolver.INSTANCE).build();

		Template template = handlebars
				.compileInline(IOUtils.toString(Launcher.class.getClassLoader().getResource("templates/results.html")));
		FileUtils.writeStringToFile(outHtmlFile, template.apply(context));

		template.apply(context);

		return outHtmlFile;
	}

	private static File generateJsonFile(ResultReport result, String resultFolderPath, String fileName)
			throws IOException {
		File jsonFolder = new File(resultFolderPath, "json");
		if (!jsonFolder.exists()) {
			jsonFolder.mkdirs();
		}
		File outJsonFile = new File(jsonFolder, fileName + ".json");

		FileWriter stringWriter =  new FileWriter(outJsonFile);
		stringWriter.write("window.result =");
		JsonWriter jwriter = new JsonWriter(stringWriter);
		
		result.writeJson(jwriter);
		stringWriter.write(";");
		
		jwriter.close();
		stringWriter.close();
		return outJsonFile;
	}


}
