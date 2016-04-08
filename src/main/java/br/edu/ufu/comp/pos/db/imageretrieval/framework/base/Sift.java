package br.edu.ufu.comp.pos.db.imageretrieval.framework.base;

public class Sift {

    public double[] extract(byte[] buffer) {
        double[] result = new double[buffer.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = Byte.toUnsignedInt(buffer[i]);
        }
        return result;

    }

}
