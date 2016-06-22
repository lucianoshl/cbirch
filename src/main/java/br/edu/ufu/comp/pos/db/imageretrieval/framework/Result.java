//package br.edu.ufu.comp.pos.db.imageretrieval.framework;
//
//import java.io.File;
//import java.io.FileWriter;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.commons.lang3.exception.ExceptionUtils;
//import org.apache.commons.lang3.time.StopWatch;
//import org.apache.log4j.Logger;
//
//import com.google.gson.GsonBuilder;
//
//import br.edu.ufu.comp.pos.db.imageretrieval.dataset.image.Image;
//import lombok.Getter;
//import lombok.Setter;
//import lombok.SneakyThrows;
//
//@Getter
//@Setter
//public class Result {
//
//    final static Logger logger = Logger.getLogger(Result.class);
//
//    public static Result instance = new Result();
//
//    private double map;
//    private int cacheHits;
//
//    private int vocabularySize;
//
//    private Map<String, Long> elapsedTime = new HashMap<String, Long>();
//    private Map<String, List<Object>> statistics = new HashMap<String, List<Object>>();
//
//    private Map<String, String> extraInfo = new HashMap<String, String>();
//
//    private List<QueryResult> results = new ArrayList<QueryResult>();
//
//    private String error;
//
//    private File datasetPath;
//
//    public void elapsedTime(String key, Runnable object) {
//        StopWatch stopWatch = new StopWatch();
//        stopWatch.start();
//        object.run();
//        stopWatch.stop();
//        elapsedTime.put(key, stopWatch.getTime());
//    }
//
//    public static void registerBirch(Double threshould, Integer words, long treeSize) {
//        Result.statistic("threshold", threshould);
//        Result.statistic("words", words);
//        Result.statistic("treeMemory", treeSize);
//    }
//
//    public static void statistic(String name, Object value) {
//        List<Object> list = instance.statistics.get(name);
//        if (list == null) {
//            list = new ArrayList<Object>();
//            instance.statistics.put(name, list);
//        }
//        list.add(value);
//    }
//
//    public static void extraInfo(String name, Object value) {
//        logger.info(name + ": " + value);
//        instance.extraInfo.put(name, String.valueOf(value));
//    }
//
//    @Override
//    public String toString() {
//        StringBuilder builder = new StringBuilder();
//        builder.append("Vocabulary size: ").append(this.vocabularySize);
//        builder.append("mAP: ").append(this.map);
//        return builder.toString();
//    }
//
//    @SneakyThrows
//    public void save() {
//        String workspace = System.getenv().get("DATASET_WORKSPACE");
//        File resultsDir = new File(workspace, "results");
//        if (!resultsDir.exists()) {
//            resultsDir.mkdirs();
//        }
//        File resultFile = new File(resultsDir, CustomFileAppender.datePart + ".json");
//        FileWriter writer = new FileWriter(resultFile);
//        new GsonBuilder().setPrettyPrinting().create().toJson(this, writer);
//        writer.close();
//        logger.info("result file saved in " + resultFile.getAbsolutePath());
//
//        this.generateMarkdownReport(resultFile.getAbsolutePath());
//
//    }
//
//    @SneakyThrows
//    private void generateMarkdownReport(String absolutePath) {
////        String script = this.getClass().getClassLoader().getResource("r/report.r").getFile();
////        String template = this.getClass().getClassLoader().getResource("r/report.Rhtml").getFile();
////        String command = "Rscript" + " " + script + " " + absolutePath + " " + template;
////        Process process = Runtime.getRuntime().exec(command);
////        process.waitFor();
////        System.out.println("Report in " + absolutePath.replace("null.json", "null/report.html"));
//
//    }
//
//    public void setError(Exception e) {
//        this.error = ExceptionUtils.getStackTrace(e);
//
//    }
//
//    public void setDatasetPath(File datasetPath) {
//        this.datasetPath = datasetPath;
//
//    }
//
//    public void addResult(String clazz, Image query, Image image, String classification) {
//        QueryResultItem item = new QueryResultItem(image, classification);
//        QueryResult result = this.findQueryResult(query);
//        result.add(item);
//    }
//
//    private QueryResult findQueryResult(Image query) {
//        for (QueryResult queryResult : results) {
//            if (queryResult.getQuery().equals(query.getImage().getAbsolutePath())) {
//                return queryResult;
//            }
//        }
//
//        QueryResult result = new QueryResult(query);
//        this.results.add(result);
//        return result;
//    }
//
//}
