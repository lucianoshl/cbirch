package cbirch.dataset.generator;


import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import cbirch.sift.LoweSiftExtractor;
import cbirch.utils.Workspace;


/**
 * Created by lucianos on 3/13/17.
 */
public class LeedsButterflyDatasetGeneratorTest {

    LeedsButterflyDatasetGenerator generator = new LeedsButterflyDatasetGenerator( new LoweSiftExtractor() );


    @Test
    public void extract() throws IOException {

        FileUtils.deleteDirectory( new File( Workspace.resolve(), "datasets/leedsbutterfly" ) );
        generator.extract( new File( "/home/lucianos/Downloads/leedsbutterfly/images/" ) );

    }
}