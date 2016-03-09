package br.edu.ufu.comp.pos.db.imageretrieval.commons;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;

public class Utils {
    public static double[] convertToDouble(byte[] buffer) {

        double[] result = new double[buffer.length];
        for (int i = 0; i < buffer.length; i++) {
            result[i] = buffer[i];
        }
        return result;
    }

    public static File getDatesetPath(String workspace, String datasetName) {
        return new File(workspace, "datasets" + File.separatorChar + datasetName);
    }

    public static Scanner createScanner(File file) {
        try {
            return new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

    public static String readFileToString(File file) {
        try {
            return FileUtils.readFileToString(file);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static List<String> readLines(String path) {
        try {
            return FileUtils.readLines(new File(path));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

    }
}
