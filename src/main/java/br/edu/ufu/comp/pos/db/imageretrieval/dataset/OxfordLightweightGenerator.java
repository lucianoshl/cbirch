package br.edu.ufu.comp.pos.db.imageretrieval.dataset;


import br.edu.ufu.comp.pos.db.imageretrieval.commons.Utils;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.factory.DatasetFactory;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by lucianos on 5/31/16. Esta classe é reponsável por gerar uma versão
 * do dataset oxford apenas com imagens mais relavantes que serão utilizadas
 * para testes de desenvolvimento
 */
public class OxfordLightweightGenerator extends DatasetGenerator {

    public OxfordLightweightGenerator( File file ) {

        super( new GeneratedDataset( "oxford-lightweight" ), file );
    }


    @SneakyThrows
    public static void main( String[] args ) {

        DatasetFactory datasetFactory = new DatasetFactory();
        String datasetName = "oxford";
        Dataset dataset = datasetFactory.create( ( "dummy non-normalized " + datasetName ).split( " " ) );

        File path = Utils.getDatesetPath( System.getenv().get( "DATASET_WORKSPACE" ), datasetName );

        File gtFiles = new File( path, "gt_files" );

        List< String > allFiles = new ArrayList< String >();
        List< String > queries = new ArrayList< String >();
        File[] files = gtFiles.listFiles();
        for ( int i = 0; i < files.length; i++ ) {
            File file = files[ i ];
            String fileName = file.getName();
            if ( fileName.contains( "query" ) ) {
                String content = FileUtils.readFileToString( new File( gtFiles, fileName ) );
                allFiles.add( content.split( " " )[ 0 ].replace( "oxc1_", "" ) );
                queries.add( content.split( " " )[ 0 ].replace( "oxc1_", "" ) );

            } else if ( fileName.contains( "good" ) ) {
                String[] infile = FileUtils.readFileToString( new File( gtFiles, fileName ) ).split( "\n" );
                for ( int j = 0; j < 5 && j < infile.length; j++ ) {
                    allFiles.add( infile[ j ] );
                }
            }
        }

        File tmpFolder = File.createTempFile( "test", "test" );
        tmpFolder.delete();
        tmpFolder.mkdirs();
        tmpFolder.deleteOnExit();

        File images = new File( path, "images" );
        for ( File file : images.listFiles() ) {
            if ( file.getName().endsWith( ".jpg" ) && allFiles.contains( file.getName().replace( ".jpg", "" ) ) ) {
                FileUtils.copyFile( file, new File( tmpFolder, file.getName() ) );
            }
        }

        System.out.println("start generate");

        OxfordLightweightGenerator oxfordLightweightGenerator = new OxfordLightweightGenerator( tmpFolder );
        oxfordLightweightGenerator.generate();

    }


    @Override
    protected void generateGroundtruth() {

    }
}
