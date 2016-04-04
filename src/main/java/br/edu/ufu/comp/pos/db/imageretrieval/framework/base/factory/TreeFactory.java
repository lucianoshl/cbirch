package br.edu.ufu.comp.pos.db.imageretrieval.framework.base.factory;

import org.apache.log4j.Logger;

import br.edu.ufu.comp.pos.db.imageretrieval.clustering.birch.cftree.CFTree;
import br.edu.ufu.comp.pos.db.imageretrieval.clustering.hkm.KMeansTree;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.Result;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.ClusterTree;

public class TreeFactory {

    final static Logger logger = Logger.getLogger(TreeFactory.class);

    public ClusterTree create(String[] args) {
        String treeName = args[0];
        Result.extraInfo("Tree", treeName);
        int startIndex = 3;
        if (treeName.equals("birch")) {
            Integer branchingFactor = Integer.valueOf(args[startIndex++]);
            Double threshold = Double.valueOf(args[startIndex++]);
            Integer memory = Integer.valueOf(args[startIndex++]);

            Result.extraInfo("Branching factor", branchingFactor);
            Result.extraInfo("Threshold", threshold);
            Result.extraInfo("Memory", memory);

            return createCFTree(branchingFactor, threshold, memory);
        }
        if (treeName.equals("hkm")) {
            Integer branchingFactor = Integer.valueOf(args[startIndex++]);
            Integer leaves = Integer.valueOf(args[startIndex++]);

            Result.extraInfo("Branching factor", branchingFactor);
            Result.extraInfo("Leaves", leaves);

            return new KMeansTree(branchingFactor, leaves);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public ClusterTree createCFTree(Integer branchingFactor, Double threshold, Integer memory) {
        Result.registerBirch(threshold, 0, 0);
        CFTree tree = new CFTree(branchingFactor, threshold, 1, true);
        tree.setAutomaticRebuild(true);
        tree.setMemoryLimitMB(memory);
        return tree;
    }

}
