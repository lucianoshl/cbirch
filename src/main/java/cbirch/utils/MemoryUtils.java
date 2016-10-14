package cbirch.utils;


import java.text.NumberFormat;


/**
 * Created by void on 10/14/16.
 */
public class MemoryUtils {

    public static void logMemory( String moment ) {

        Runtime runtime = Runtime.getRuntime();

        NumberFormat format = NumberFormat.getInstance();

        StringBuilder sb = new StringBuilder();
        long maxMemory = runtime.maxMemory();
        long allocatedMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();

        sb.append( moment + "\n" );
        sb.append( "free memory: " + format.format( freeMemory / 1024 ) + "\n" );
        sb.append( "allocated memory: " + format.format( allocatedMemory / 1024 ) + "\n" );
        sb.append( "max memory: " + format.format( maxMemory / 1024 ) + "\n" );
        sb.append( "total free memory: " + format.format( ( freeMemory + ( maxMemory - allocatedMemory ) ) / 1024 ) + "\n" );
        logger.info( sb.toString() );
    }
}
