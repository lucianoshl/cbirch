package br.edu.ufu.comp.pos.db.imageretrieval.commons;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.FileAppender;

public class CustomFileAppender extends FileAppender {

    @Override
    public void setFile(String fileName) {
	if (fileName.indexOf("%timestamp") >= 0) {
	    Date d = new Date();
	    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-SS");
	    fileName = fileName.replaceAll("%timestamp", format.format(d));
	}
	super.setFile(fileName);
    }
}