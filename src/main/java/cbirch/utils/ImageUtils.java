package cbirch.utils;


import lombok.SneakyThrows;
import magick.ImageInfo;
import magick.MagickImage;
import org.apache.commons.io.FilenameUtils;

import java.io.File;


/**
 * sudo apt-get install libjmagick6-java libjmagick6-jni
 */

/**
 * sudo ln -s /usr/lib/jni/libJMagick.so /usr/lib/libJMagick.so
 */
public class ImageUtils {

    @SneakyThrows
    public static void convert( File origin, File destFolder, String format ) {

        File target = new File(destFolder, FilenameUtils.removeExtension(origin.getName()) + "." + format);

        BinaryUtils.execute("/usr/bin/convert", origin.getAbsolutePath(), target.getAbsolutePath());

//        ImageInfo originImageInfo = new ImageInfo( origin.getAbsolutePath() ); // Get BMP file
//        MagickImage magick_converter = new MagickImage( originImageInfo ); // Create
//
//        String outFile = new File( destFolder, origin.getName() ).getAbsolutePath();
//
//        outFile = outFile.substring( 0, outFile.lastIndexOf( '.' ) ) + "." + format;
//        magick_converter.setFileName( outFile ); // set output file format
//        boolean success = magick_converter.writeImage(originImageInfo);// do the conversion
//        if (!success){
//            throw new IllegalStateException("Erro ao converter arquivo");
//        }

    }


    public static String getExtension(File image ) {
        return getExtension(image.getAbsolutePath());
    }


    public static String getExtension( String image ) {
        return image.substring(image.indexOf('.')).substring(1);
    }
}
