package br.edu.ufu.comp.pos.db.imageretrieval.framework.base;

import br.edu.ufu.comp.pos.db.imageretrieval.framework.tree.BirchTree;

public class TreeFactory {

    public BirchTree create( String[] args ) {
        String treeName = args[0];
        
        if (treeName.equals( "birch" )){
            Integer branchingFactor = Integer.valueOf( args[2] );
            Double threshold = Double.valueOf( args[3] );
            Integer memory = Integer.valueOf( args[4] );
            
            System.out.println( "Tree: Birch" );
            System.out.println( "Branching factor: "+branchingFactor );
            System.out.println( "Threshold: "+threshold );
            System.out.println( "Memory: "+memory );
            
            return new BirchTree( branchingFactor , threshold, memory);
        }else {
            throw new UnsupportedOperationException();
        }
    }

}
