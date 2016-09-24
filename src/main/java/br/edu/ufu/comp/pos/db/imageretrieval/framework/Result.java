package br.edu.ufu.comp.pos.db.imageretrieval.framework;


import br.edu.ufu.comp.pos.db.imageretrieval.dataset.image.Image;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


@Getter
@Setter
public class Result {

    final static Logger logger = LoggerFactory.getLogger( Result.class );

    public static Result instance = new Result();

    private double map;

    private int vocabularySize;

    private Map< String, Double > elapsedTime = new HashMap< String, Double >();

    // private Map< String, List< Object >> statistics = new HashMap< String,
    // List< Object >>();

    private Map< String, String > extraInfo = new HashMap< String, String >();

    // private List< QueryResult > results = new ArrayList< QueryResult >();

    private String error;

    private File datasetPath;

    private int execution = 0;


    public void elapsedTime( String key, Runnable object ) {

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        object.run();
        stopWatch.stop();
        elapsedTime.put( key, new Long( stopWatch.getTime() ).doubleValue() / 1000d / 60d / 60d );
    }


    public static void extraInfo( String name, Object value ) {

        logger.info( name + ": " + value );
        instance.extraInfo.put( name, String.valueOf( value ) );
    }


    @Override
    public String toString() {

        StringBuilder builder = new StringBuilder();
        builder.append( "Vocabulary size: " ).append( this.vocabularySize );
        builder.append( "mAP: " ).append( this.map );
        return builder.toString();
    }


    @SneakyThrows
    public void save() {

        String workspace = System.getenv().get( "DATASET_WORKSPACE" );
        File resultsDir = new File( workspace, "results" );
        if ( !resultsDir.exists() ) {
            resultsDir.mkdirs();
        }

        this.createCSVTable( resultsDir );

        File resultFile = new File( resultsDir, MDC.get( "launcher-date" ) + "-" + execution + ".json" );
        logger.info( "result file saved in " + resultFile.getAbsolutePath() );
        FileWriter writer = new FileWriter( resultFile );
        new GsonBuilder().setPrettyPrinting().create().toJson( this, writer );
        writer.close();

        this.generateMarkdownReport( resultFile.getAbsolutePath() );

    }


    @SneakyThrows
    private void createCSVTable( File resultsDir ) {

        File file = new File( resultsDir, "results.csv" );
        if ( !file.exists() ) {
            file.createNewFile();
            FileUtils.write( file, "id;tree;bf,memory;dataset;execution;vocabulary;map\n", true );
        }

        String line = StringUtils.join( //
            new Object[] { MDC.get( "launcher-date" ),//
                this.extraInfo.get( "Tree" ),//
                this.extraInfo.get( "Branching factor" ),//
                this.extraInfo.get( "Memory" ),//
                this.extraInfo.get( "Dataset name" ),//
                execution,//
                this.getVocabularySize(),//
                this.getMap() },//
            ";" );

        FileUtils.writeLines( file, Arrays.asList( line ), true );
    }


    private void generateMarkdownReport( String absolutePath ) {

        // String script =
        // this.getClass().getClassLoader().getResource("r/report.r").getFile();
        // String template =
        // this.getClass().getClassLoader().getResource("r/report.Rhtml").getFile();
        // String command = "Rscript" + " " + script + " " + absolutePath + " "
        // + template;
        // Process process = Runtime.getRuntime().exec(command);
        // process.waitFor();
        // System.out.println("Report in " + absolutePath.replace("null.json",
        // "null/report.html"));

    }


    public void setError( Exception e ) {

        this.error = ExceptionUtils.getStackTrace( e );

    }


    public void setDatasetPath( File datasetPath ) {

        this.datasetPath = datasetPath;

    }


    public void addResult( String clazz, Image query, Image image, String classification ) {

        // QueryResultItem item = new QueryResultItem(image, classification);
        // QueryResult result = this.findQueryResult(query);
        // result.add(item);
    }


    // private QueryResult findQueryResult( Image query ) {
    //
    // for ( QueryResult queryResult : results ) {
    // if ( queryResult.getQuery().equals( query.getImage().getAbsolutePath() )
    // ) {
    // return queryResult;
    // }
    // }
    //
    // QueryResult result = new QueryResult( query );
    // this.results.add( result );
    // return result;
    // }

    public void newExecution() {

        // this.statistics.get( "words" ).remove( this.statistics.get( "words"
        // ).size() - 1 );
        // this.statistics.get( "threshold" ).remove( this.statistics.get(
        // "threshold" ).size() - 1 );
        // this.statistics.get( "treeMemory" ).remove( this.statistics.get(
        // "treeMemory" ).size() - 1 );
        // this.results.clear();
        this.execution++;
        MDC.put( "log-id", MDC.get( "launcher-date" ) + "-" + execution );
    }

}
