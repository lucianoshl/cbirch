package cbirch.appender;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.ErrorCode;

public class NewLogForEachRunFileAppender extends FileAppender {

    public static String identifier;

    public void activateOptions() {
        if (fileName != null) {
            try {
                fileName = getNewLogFileName();
                setFile(fileName, fileAppend, bufferedIO, bufferSize);
            } catch (Exception e) {
                errorHandler.error("Error while activating log options", e,
                        ErrorCode.FILE_OPEN_FAILURE);
            }
        }
    }

    private String getNewLogFileName() {
        if (fileName != null) {
            String date = getIdentifier();
            String file = date + "-" + this.getThreshold().toString() + ".log";

            return new File(fileName, file).getAbsolutePath();
        }
        return null;
    }

    public static String getIdentifier() {
        if (identifier == null) {
            identifier = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        }

        return identifier;
    }
}