package br.edu.ufu.comp.pos.db.imageretrieval.clustering.birch.cftree;


import org.junit.Assert;
import org.junit.Test;

public class CFTreeTest {

    @Test
    public void validateSizeOf() {

        CFTree tree = new CFTree(100, 0.01, 0, true);
        for (int i = 0; i < 100000; i++) {
            tree.insertEntry(generateEntry(2));
        }

        System.out.println(Math.abs(tree.countEntries() - tree.test()));
        Assert.assertEquals(tree.countEntries(), tree.test());
    }

    private double[] generateEntry(int dim) {
        double[] doubles = new double[dim];
        for (int i = 0; i < doubles.length; i++) {
            doubles[i] = Math.random();
        }
        return doubles;
    }
}
