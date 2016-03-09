package br.edu.ufu.comp.pos.db.imageretrieval.framework.base;

public class Sift {

    static double MAX_VALUE = 127;
    static double MIN_VALUE = 0;

    public static double[] scale(byte[] buffer) {
        double[] result = new double[buffer.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = buffer[i] / MAX_VALUE;
        }
        return result;

    }

}