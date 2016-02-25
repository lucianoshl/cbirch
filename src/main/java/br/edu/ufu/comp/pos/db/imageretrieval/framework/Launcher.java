package br.edu.ufu.comp.pos.db.imageretrieval.framework;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.edu.ufu.comp.pos.db.imageretrieval.dataset.Dataset;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.ClusterTree;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.DatasetFactory;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.TreeFactory;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.tree.BirchTree;
import br.edu.ufu.comp.pos.db.imageretrieval.pojo.ImageHits;
import br.edu.ufu.comp.pos.db.imageretrieval.pojo.QueryResult;
import br.edu.ufu.comp.pos.db.imageretrieval.pojo.ResultReport;


public class Launcher {

    public static void main( String[] args )
        throws IOException {
        args = args[0].split( " " );
        
        if ( args.length == 0 ) {
            String.format( "tree name is required" );
        }
        
        Dataset dataset = new DatasetFactory().create( args );
        BirchTree tree = new TreeFactory().create( args );

        System.out.println(  );
        
        ResultReport result = new Launcher().run( dataset, tree );
        result.save( dataset.getResultFolder() );
    }


    public ResultReport run( Dataset dataset, ClusterTree tree )
        throws IOException {

        ResultReport result = new ResultReport();

        dataset.scanSifts( ( sift ) -> tree.insertEntry( sift ) );

        tree.optimize();

        dataset.scan( ( img ) -> tree.index( img ) );

        tree.finishBuild();

        List< QueryResult > queryResults = new ArrayList< QueryResult >();
        result.setQueryResults( queryResults );
        dataset.scan( ( queryImage ) -> {
            result.addImage( queryImage );
            List< ImageHits > queryResult = tree.queryImage( queryImage );
            queryResults.add( new QueryResult( queryImage, queryResult ) );
        } );

        return result;
    }
}
