package br.edu.ufu.comp.pos.db.imageretrieval.commons;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.FileAppender;

public class CustomFileAppender extends FileAppender {

    public static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-SS");
    public static String datePart;

    @Override
    public void setFile(String fileName) {
	if (fileName.indexOf("%timestamp") >= 0) {
	    Date d = new Date();
	    datePart = format.format(d);
	    fileName = fileName.replaceAll("%timestamp", datePart);
	    String workspace = System.getenv().get("DATASET_WORKSPACE");
	    fileName = fileName.replaceAll("%workspace", workspace);

	}
	super.setFile(fileName);
    }
}