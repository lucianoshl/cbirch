package cbirch.dataset;


import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;


/**
 * Created by void on 9/24/16.
 */
public class OxfordDatasetTest {

    private OxfordDataset oxfordDataset;


    @Before
    public void createDs() {

        this.oxfordDataset = new OxfordDataset();
    }

    //
    // @Test
    // public void scanAllFeatures()
    // throws Exception {
    //
    // final int[] total = { 0 };
    //
    // oxfordDataset.scanAllFeatures( ( sift, i ) -> total[ 0 ]++ );
    // TestCase.assertEquals( total[ 0 ], oxfordDataset.getTotalFeatures() );
    // }


    @Test
    public void getTotalFeatures()
            throws Exception {

        Assert.assertEquals(16334970, oxfordDataset.getTotalFeatures());
    }


    @Test
    public void scanAllImages()
            throws Exception {

        final int[] total = {0};

        oxfordDataset.scanAllImages((image, i) -> total[0]++);

        TestCase.assertEquals(total[0], oxfordDataset.getTotalImages());
    }


    @Test
    public void getTestClasses()
            throws Exception {

        String[] result = oxfordDataset.getTestClasses();
        String[] expected = new String[]{"all_souls", "ashmolean", "balliol", "bodleian", "christ_church", "cornmarket", "hertford", "keble", "magdalen",
                "pitt_rivers", "radcliffe_camera"};

        Arrays.sort(expected);
        Arrays.sort(result);

        TestCase.assertTrue(Arrays.equals(expected, result));
    }


    @Test
    public void getQueries()
            throws Exception {

        String[] testClasses = oxfordDataset.getTestClasses();
        for (int i = 0; i < testClasses.length; i++) {
            Image[] queries = oxfordDataset.getQueries(testClasses[i]);
            System.out.println(Arrays.toString(queries));
        }
    }


    @Test
    public void quality()
            throws Exception {

        String clazz = oxfordDataset.getTestClasses()[0];

        Image query = oxfordDataset.getQueries(clazz)[0];

        Image all_souls_000220 = oxfordDataset.cache().get("all_souls_000220");

        String quality = oxfordDataset.quality(clazz, query, all_souls_000220);

        TestCase.assertEquals("junk", quality);
    }


    @Test
    public void getMapCalculator()
            throws Exception {

        oxfordDataset.getMapCalculator();
    }


    @Test
    public void getTotalImages()
            throws Exception {

        Assert.assertEquals(5062, oxfordDataset.getTotalImages());
    }

    @Test
    public void testQueryQte() {
        String[] testClasses = oxfordDataset.getTestClasses();
        TestCase.assertEquals(11, testClasses.length);
        for (String testClass : testClasses) {
            TestCase.assertEquals(5, oxfordDataset.getQueries(testClass).length);
        }
    }

}