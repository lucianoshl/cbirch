package br.edu.ufu.comp.pos.db.imageretrieval.framework.base.factory;

import cbirch.Report;
import org.apache.log4j.Logger;

import br.edu.ufu.comp.pos.db.imageretrieval.clustering.birch.cftree.CFTree;
import br.edu.ufu.comp.pos.db.imageretrieval.clustering.commons.ClusterTree;
import br.edu.ufu.comp.pos.db.imageretrieval.clustering.hkm.KMeansTree;

public class TreeFactory {

    final static Logger logger = Logger.getLogger(TreeFactory.class);

    public ClusterTree create(String[] args) {
        String treeName = args[0];
        Report.info("Tree", treeName);
        int startIndex = 3;
        if (treeName.equals("birch")) {
            Integer branchingFactor = Integer.valueOf(args[startIndex++]);
            Double threshold = Double.valueOf(args[startIndex++]);
            Integer memory = Integer.valueOf(args[startIndex++]);
            Integer leavesLimit = Integer.valueOf(args[startIndex++]);

            Report.info("branching_factor", branchingFactor);
            Report.info("threshold", threshold);
            Report.info("memory", memory);
            Report.info("leaves_limit", leavesLimit);

            return createCFTree(branchingFactor, threshold, memory, leavesLimit);
        }
        if (treeName.equals("hkm")) {
            Integer branchingFactor = Integer.valueOf(args[startIndex++]);
            Integer leaves = Integer.valueOf(args[startIndex++]);

            Report.info("branching_factor", branchingFactor);
            Report.info("leaves", leaves);

            return new KMeansTree(branchingFactor, leaves);
        } else {
            throw new UnsupportedOperationException();
        }
    }


    public ClusterTree createCFTree(Integer branchingFactor, Double threshold, Integer memory, Integer leavesLimit) {
        Report.info("birch_evolution", threshold, 0, 0);
        CFTree tree = new CFTree(branchingFactor, threshold, 1, true);
        tree.setAutomaticRebuild(true);
        tree.setMemoryLimitMB(memory);
        tree.setLeavesLimit(leavesLimit);
        return tree;
    }

}
