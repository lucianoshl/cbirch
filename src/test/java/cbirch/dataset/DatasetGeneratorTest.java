package cbirch.dataset;

import cbirch.sift.LoweSiftExtractor;
import cbirch.sift.OpenCVExtractor;
import org.junit.Test;

/**
 * Created by lucianos on 8/17/16.
 */
public class DatasetGeneratorTest {

    @Test
    public void testGenerate(){
        System.setProperty("cbirch_workspace","/home/void/workspace/mestrado/workspace");
        new DatasetGenerator(new LoweSiftExtractor(),"leedsbutterfly").generate();
    }

}