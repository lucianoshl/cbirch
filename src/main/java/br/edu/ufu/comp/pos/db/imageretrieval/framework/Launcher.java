package br.edu.ufu.comp.pos.db.imageretrieval.framework;


import java.io.IOException;

import br.edu.ufu.comp.pos.db.imageretrieval.dataset.Dataset;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.ClusterTree;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.DatasetFactory;
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

        dataset.scanTestSetSifts( ( sift ) -> tree.insertEntry( sift ) );

        tree.optimize();

        dataset.testSet( ( img ) -> tree.index( img ) );

        tree.finishBuild();
    }
}
