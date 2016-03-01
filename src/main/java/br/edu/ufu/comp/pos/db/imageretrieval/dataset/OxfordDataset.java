package br.edu.ufu.comp.pos.db.imageretrieval.dataset;


import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.function.Consumer;

import br.edu.ufu.comp.pos.db.imageretrieval.commons.Utils;
import br.edu.ufu.comp.pos.db.imageretrieval.dataset.image.OxfordImage;


public class OxfordDataset extends Dataset {

    private File binaryFile;

    private File imageFolder;

    private File rangeSwiftInBinary;

    private File gtFilesFolder;

    private File datasetFolder;

    private File orderInBinaryFile;

    private Set< String > queryFiles;

    protected void scan( Consumer< OxfordImage > c, boolean queryImages ) {

        this.scanAllImages( ( image ) -> {
            if ( isQueryFile( image ) == queryImages ) {
                c.accept( image );
            }
        } );
    }


    @Override
    public void trainSet( Consumer< OxfordImage > c ) {

        this.scan( c, false );
    }

    public void testSet( Consumer< OxfordImage > c ) {

        this.scan( c, true );
    }


    private boolean isQueryFile( OxfordImage image ) {

        return this.queryFiles.contains( image.getImage().getName() );
    }


    private void fillQueryFiles() {

        this.queryFiles = new HashSet< String >();
        File[] listFiles = this.gtFilesFolder.listFiles();
        for ( File file : listFiles ) {
            if ( file.getName().contains( "query.txt" ) ) {
                String queryFile = Utils.readFileToString( file ).split( " " )[ 0 ].replace( "oxc1_", "" ) + ".jpg";
                queryFiles.add( queryFile );
            }
        }
    }


    protected void scanAllImages( Consumer< OxfordImage > c ) {

        Map< String, Long > aux = new HashMap< String, Long >();
        aux.put( "aux", 0l );
        scanOrderFile( ( fileName ) -> {
            long siftSize = getImageSiftSize( fileName );
            c.accept( new OxfordImage( binaryFile, new File( imageFolder, fileName ), aux.get( "aux" ), siftSize ) );
            aux.put( "aux", aux.get( "aux" ) + siftSize * 128 );
        } );

    }


    private long getImageSiftSize( String fileName ) {

        File siftSizeFile = new File( rangeSwiftInBinary, "oxc1_" + fileName.replace( ".jpg", ".txt" ) );
        Scanner createScanner = Utils.createScanner( siftSizeFile );
        createScanner.nextLine();
        long result = createScanner.nextLong();
        createScanner.close();
        return result;
    }


    private void scanOrderFile( Consumer< String > c ) {

        Scanner scanner = Utils.createScanner( orderInBinaryFile );
        int i = 0;
        while ( scanner.hasNextLine() ) {
            String fileName = scanner.nextLine().replace( "oxc1_", "" ) + ".jpg";
            c.accept( fileName );
            i++;
            if (i == 30){
                break;
            }
        }
        scanner.close();
    }


    public static OxfordDataset createFromBase( String workspace, String datasetName ) {

        return new OxfordDataset( workspace, datasetName, "feat_oxc1_hesaff_sift.bin",
            "word_oxc1_hesaff_sift_16M_1M", "images", "gt_files", "README2.txt" );
    }


    public OxfordDataset(
        String workspace,
        String datasetName,
        String binaryFile,
        String siftSizeFolderDescriptor,
        String imagesFolderPath,
        String gtFiles,
        String orderInBinaryFile ) {

        // this.workspace = workspace;
        this.datasetFolder = Utils.getDatesetPath( workspace, datasetName );
        this.binaryFile = new File( datasetFolder, binaryFile );
        this.rangeSwiftInBinary = new File( datasetFolder, siftSizeFolderDescriptor );
        this.imageFolder = new File( datasetFolder, imagesFolderPath );
        this.gtFilesFolder = new File( datasetFolder, gtFiles );
        this.orderInBinaryFile = new File( this.datasetFolder, orderInBinaryFile );
        this.fillQueryFiles();

    }

}
