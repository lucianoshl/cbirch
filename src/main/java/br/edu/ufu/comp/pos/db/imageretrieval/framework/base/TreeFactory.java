package br.edu.ufu.comp.pos.db.imageretrieval.framework.base;

import org.apache.log4j.Logger;

import br.edu.ufu.comp.pos.db.imageretrieval.framework.tree.BirchTree;

public class TreeFactory {

    final static Logger logger = Logger.getLogger(TreeFactory.class);
    
    public BirchTree create(String[] args) {
	String treeName = args[0];
	

	if (treeName.equals("birch")) {
	    Integer branchingFactor = Integer.valueOf(args[2]);
	    Double threshold = Double.valueOf(args[3]);
	    Integer memory = Integer.valueOf(args[4]);

	    logger.info("Tree: Birch");
	    logger.info("Branching factor: " + branchingFactor);
	    logger.info("Threshold: " + threshold);
	    logger.info("Memory: " + memory);

	    return new BirchTree(branchingFactor, threshold, memory);
	} else {
	    throw new UnsupportedOperationException();
	}
    }

}
