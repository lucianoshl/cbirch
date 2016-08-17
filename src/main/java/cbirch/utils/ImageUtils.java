package cbirch.utils;


import lombok.SneakyThrows;
import magick.ImageInfo;
import magick.MagickImage;

import java.io.File;


/**
 * sudo apt-get install libjmagick6-java libjmagick6-jni sudo ln -s
 * /usr/lib/jni/libJMagick.so /usr/lib/libJMagick.so
 */
public class ImageUtils {

    @SneakyThrows
    public static void convert( File origin, File dest, String format ) {

        String inputfileName = origin.getAbsolutePath(); // Input BMP file
        ImageInfo info = new ImageInfo( inputfileName ); // Get BMP file
        MagickImage magick_converter = new MagickImage( info ); // Create

        String outFile = new File( dest, origin.getName() ).getAbsolutePath();

        outFile = outFile.substring( 0, outFile.lastIndexOf( '.' ) ) + ".pgm";
        magick_converter.setFileName( outFile ); // set output file format
        magick_converter.writeImage( info ); // do the conversion
    }


    public static String getExtension(File image ) {
        return getExtension(image.getAbsolutePath());
    }


    public static String getExtension( String image ) {
        return image.substring(image.indexOf('.')).substring(1);
    }
}
