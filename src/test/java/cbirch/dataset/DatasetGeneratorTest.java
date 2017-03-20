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
        new DatasetGenerator(new LoweSiftExtractor(),"leedsbutterfly").generate();
    }

}