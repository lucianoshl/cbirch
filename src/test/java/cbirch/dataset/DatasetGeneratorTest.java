package cbirch.dataset;

import cbirch.sift.LoweSiftExtractor;
import org.junit.Test;

/**
 * Created by lucianos on 8/17/16.
 */
public class DatasetGeneratorTest {

    @Test
    public void testGenerate(){
        System.setProperty("cbirch_workspace","/home/lucianos/pesquisa/workspace");
        new DatasetGenerator(new LoweSiftExtractor(),"leeds-butterfly-dataset-full").generate();
    }

}