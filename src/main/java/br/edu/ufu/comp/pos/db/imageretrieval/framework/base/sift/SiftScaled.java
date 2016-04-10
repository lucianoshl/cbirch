package br.edu.ufu.comp.pos.db.imageretrieval.framework.base.sift;

public class SiftScaled extends Sift {
    static double MAX_VALUE = 255;
    static double MIN_VALUE = 0;

    public double[] extract(byte[] buffer) {
        double[] result = new double[buffer.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = Byte.toUnsignedInt(buffer[i]) / MAX_VALUE;
        }
        return result;

    }

    public static byte[] removeScale(double[] scaled) {
        byte[] result = new byte[scaled.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = (byte) (scaled[i] * MAX_VALUE);
        }
        return result;

    }

}
