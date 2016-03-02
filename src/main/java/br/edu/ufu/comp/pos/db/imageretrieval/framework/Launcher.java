package br.edu.ufu.comp.pos.db.imageretrieval.framework;


import java.io.IOException;
import java.util.List;

import br.edu.ufu.comp.pos.db.imageretrieval.dataset.Dataset;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.ClusterTree;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.DatasetFactory;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.Histogram;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.TreeFactory;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.tree.BirchTree;


public class Launcher {

    public static void main( String[] args )
        throws IOException {

        if ( args.length == 0 ) {
            throw new IllegalArgumentException( "tree name is required" );
        }

        Dataset dataset = new DatasetFactory().create( args );
        BirchTree tree = new TreeFactory().create( args );
        new Launcher().run( dataset, tree );

    }


    public void run( Dataset dataset, ClusterTree tree )
        throws IOException {

	
	
        dataset.scanTrainSetSifts( ( sift ) -> tree.insertEntry( sift ) );

        // tree.optimize();
        tree.finishBuild();

        dataset.scanTrainSet( ( img ) -> tree.index( img ) );

        String[] testClasses = dataset.getTestClasses();
        for (String clazz : testClasses) {
            System.out.println("=============== START QUERY FOR CLASS " + clazz);
            dataset.scanTestSet(clazz, ( query ) -> {
                System.out.println();
                System.out.println( "\tQuery: " + query.getImage().getName() );
                List< Histogram > results = tree.findTopK( query, 4 );
                for ( int i = 0; i < results.size(); i++ ) {
                    String imgName = results.get( i ).getImage().getImage().getName();
                    String classification = dataset.quality( query, imgName );
                    System.out.println( String.format( "\t\tRank %s: %s %s", i, imgName, classification ) );
                }
            } );
            System.out.println();
	}
        

    }
}
