package cbirch.sift;


import java.io.File;
import java.util.Arrays;
import java.util.List;


/**
 * apt-cache search opencv | grep jni
 * sudo apt-get install libopencv2.4-jni
 */
public class OpenCVExtractor implements SiftExtractor {

    @Override
    public List< String > supportedTypes() {

        return Arrays.asList( "pgm" );
    }


    @Override
    public int[] extract( File image ) {
        throw new UnsupportedOperationException();
////        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
//        Mat objectImage = Highgui.imread(image.getAbsolutePath(), Highgui.CV_LOAD_IMAGE_GRAYSCALE);
//
//        MatOfKeyPoint objectKeyPoints = new MatOfKeyPoint();
//        FeatureDetector featureDetector = FeatureDetector.create(FeatureDetector.SIFT);
//        featureDetector.detect(objectImage, objectKeyPoints);
//        KeyPoint[] keypoints = objectKeyPoints.toArray();
//        System.out.println(keypoints);
//
//        return new int[ 0 ];
    }


    @Override
    public String identifier() {

        return "opencv";
    }
}
