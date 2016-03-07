package br.edu.ufu.comp.pos.db.imageretrieval.framework;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.StopWatch;

import com.google.gson.GsonBuilder;

import br.edu.ufu.comp.pos.db.imageretrieval.commons.CustomFileAppender;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

@Getter
@Setter
public class Result {
    public static Result instance = new Result();

    private double map;
    private int vocabularySize;
    private int cacheHits;

    private Map<String, Long> elapsedTime = new HashMap<String, Long>();
    private Map<String, List<Object>> statistics = new HashMap<String, List<Object>>();

    public void elapsedTime(String key, Runnable object) {
	StopWatch stopWatch = new StopWatch();
	stopWatch.start();
	object.run();
	stopWatch.stop();
	elapsedTime.put(key, stopWatch.getTime());
    }

    public static void statistic(String name, Object value) {
	List<Object> list = instance.statistics.get(name);
	if (list == null) {
	    list = new ArrayList<Object>();
	    instance.statistics.put(name, list);
	}
	list.add(value);
    }

    @Override
    public String toString() {
	StringBuilder builder = new StringBuilder();
	builder.append("Vocabulary size: ").append(this.vocabularySize);
	builder.append("mAP: ").append(this.map);
	return builder.toString();
    }

    @SneakyThrows
    public void save() {
	String workspace = System.getenv().get("DATASET_WORKSPACE");
	File resultsDir = new File(workspace, "results");
	if (!resultsDir.exists()) {
	    resultsDir.mkdirs();
	}
	File resultFile = new File(resultsDir, CustomFileAppender.nameFile + ".json");
	FileWriter writer = new FileWriter(resultFile);
	new GsonBuilder().setPrettyPrinting().create().toJson(this, writer);
	writer.close();
    }

}
