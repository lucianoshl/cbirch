package org.apache.commons.math3.ml.distance;

import org.junit.Test;

import junit.framework.TestCase;

public class CosineDistanceTest {

    CosineDistance cosineDistance = new CosineDistance();

    @Test
    public void test1() {
	double dist = cosineDistance.compute(new double[] { 1, 1 }, new double[] { 1, 1 });
	TestCase.assertEquals(1, Math.round(dist));
    }

    @Test
    public void test2() {
	double dist = cosineDistance.compute(new double[] { 1, 1 }, new double[] { -1, -1 });
	TestCase.assertEquals(-1, Math.round(dist));
    }
}
