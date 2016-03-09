package br.edu.ufu.comp.pos.db.imageretrieval.framework.base.factory;

import org.apache.log4j.Logger;

import br.edu.ufu.comp.pos.db.imageretrieval.clustering.birch.cftree.CFTree;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.ClusterTree;

public class TreeFactory {

    final static Logger logger = Logger.getLogger(TreeFactory.class);

    public ClusterTree create(String[] args) {
	String treeName = args[0];

	if (treeName.equals("birch")) {
	    Integer branchingFactor = Integer.valueOf(args[2]);
	    Double threshold = Double.valueOf(args[3]);
	    Integer memory = Integer.valueOf(args[4]);

	    logger.info("Tree: Birch");
	    logger.info("Branching factor: " + branchingFactor);
	    logger.info("Threshold: " + threshold);
	    logger.info("Memory: " + memory);

	    return createCFTree(branchingFactor, threshold, memory);
	} else {
	    throw new UnsupportedOperationException();
	}
    }

    public ClusterTree createCFTree(Integer branchingFactor, Double threshold, Integer memory) {
	CFTree tree = new CFTree(branchingFactor, threshold, 1, true);
	tree.setAutomaticRebuild(true);
	tree.setMemoryLimitMB(memory);
	return tree;
    }

}
