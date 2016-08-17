package cbirch.sift;


import java.io.File;
import java.util.Arrays;
import java.util.List;


/**
 * Created by lucianos on 8/17/16.
 */
public interface SiftExtractor {

    public List< String > supportedTypes();

    int[] extract( File image );

    public String identifier();
}
