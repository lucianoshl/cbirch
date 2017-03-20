package cbirch.utils;


import lombok.SneakyThrows;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;


/**
 * Created by lucianos on 3/13/17.
 */
public class BinaryUtils {

    @SneakyThrows
    public static void execute( String... s ) {

        String command = StringUtils.join( s, " " );

        ProcessBuilder processBuilder = new ProcessBuilder( s);
        Process start = processBuilder.start();
        start.waitFor();
        String error = IOUtils.toString( start.getErrorStream() );
        if ( StringUtils.isNotBlank( error ) ) {
            throw new IllegalStateException( error );
        }

    }
}
