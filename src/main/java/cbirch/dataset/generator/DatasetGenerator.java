package cbirch.dataset.generator;


import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.Arrays;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import cbirch.sift.SiftExtractor;
import cbirch.utils.ImageUtils;
import cbirch.utils.Workspace;


/**
 * Created by lucianos on 3/13/17.
 */
@RequiredArgsConstructor
public abstract class DatasetGenerator {

    protected final SiftExtractor extractor;

    protected File datasetFolder;

    private File imagesFolder;

    private File imagesFeaturesFolder;

    private File gtFilesFolder;

    private File datasetProperties;

    private File imagesPositions;

    private File features;

    private File tmpFolder;


    public void extract( File folder ) {

        this.createFolderStructure();
        this.copyOriginImages( folder );
        this.convertImages();

        this.extractSift();

        this.finish();
    }

    @SneakyThrows
    private void extractSift() {

        FileOutputStream fos = new FileOutputStream(this.features);

        int max_value = 0;

        String[] files = this.tmpFolder.list();
        Arrays.sort(files);
        for (String fileName : files) {
            System.out.println(fileName);
            File file = new File(tmpFolder, fileName);
            int[] extract = extractor.extract(file);
            for (int i : extract) {
                if (i > max_value){
                    max_value = i;
                }
                if (i > 128){
                    System.out.print(1);
                }
            }
            byte[] binary = new byte[extract.length];
            for (int i = 0; i < binary.length; i++) {
                binary[i] = (byte)extract[i];
            }
            fos.write(binary);
            fos.flush();
//            FileUtils.writeByteArrayToFile(this.features,binary,true);
        }

        fos.close();
    }


    @SneakyThrows
    private void finish() {

        FileUtils.deleteDirectory( this.tmpFolder );
    }


    @SneakyThrows
    private void convertImages() {

        this.tmpFolder = new File( System.getProperty( "java.io.tmpdir" ), "cbirch" + System.nanoTime() );
        this.tmpFolder.mkdirs();

        String convertExtension = extractor.supportedTypes().get( 0 );

        String[] images = this.imagesFolder.list();
        for ( String image : images ) {
            File originImage = new File( imagesFolder, image );
            if ( extractor.supportedTypes().contains( FilenameUtils.getExtension( image ) ) ) {
                FileUtils.copyFile( originImage, new File( this.tmpFolder, image ) );
            } else {
                ImageUtils.convert( originImage, this.tmpFolder, convertExtension );
            }
        }
        System.out.println();
    }


    @SneakyThrows
    private void copyOriginImages( File folder ) {

        String[] list = folder.list();
        for ( String image : list ) {
            FileUtils.copyFile( new File( folder, image ), new File( this.imagesFolder, image ) );
        }
    }


    @SneakyThrows
    protected void createFolderStructure() {

        File resolve = Workspace.resolve();
        this.datasetFolder = new File( resolve, "datasets/" + this.datasetName() );
        if ( !this.datasetFolder.exists() ) {
            this.datasetFolder.mkdirs();
        }

        this.imagesFolder = new File( this.datasetFolder, "images" );
        this.imagesFeaturesFolder = new File( this.datasetFolder, "images-features" );
        this.gtFilesFolder = new File( this.datasetFolder, "gt_files" );

        this.imagesFolder.mkdirs();
        this.imagesFeaturesFolder.mkdirs();
        this.gtFilesFolder.mkdirs();

        this.datasetProperties = new File( this.datasetFolder, "dataset.properties" );
        this.imagesPositions = new File( this.datasetFolder, "positions.csv" );
        this.features = new File( this.datasetFolder, "features.bin" );

        this.datasetProperties.createNewFile();
        this.imagesPositions.createNewFile();
        this.features.createNewFile();

    }


    protected abstract String datasetName();

}
