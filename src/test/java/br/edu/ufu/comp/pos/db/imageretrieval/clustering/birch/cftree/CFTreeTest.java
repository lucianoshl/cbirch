package br.edu.ufu.comp.pos.db.imageretrieval.clustering.birch.cftree;

import static java.lang.Math.random;

import org.junit.Test;

import junit.framework.TestCase;
import net.sourceforge.sizeof.SizeOf;

public class CFTreeTest {
    @Test
    public void validateSizeOf() {
	CFTree tree = new CFTree(100, 10, 0, true);
	for (int i = 0; i < 100000; i++) {
	    tree.insertEntry(new double[] { random(), random(), random(), random(), random(), random(), random(),
		    random(), random(), random(), random(), random(), random(), random(), random(), random(), random(),
		    random(), random(), random(), random(), random(), random(), random(), random(), random(), random(),
		    random(), random(), random(), random(), random(), random(), random(), random(), random(), random(),
		    random(), random(), random(), random(), random(), random(), random(), random(), random(), random(),
		    random(), random(), random(), random(), random(), random(), random(), random(), random(), random(),
		    random(), random(), random(), random(), random(), random(), random(), random(), random(), random(),
		    random() });
	}
	long size = tree.computeMemorySize(tree);
	System.out.println(SizeOf.humanReadable(size));
	System.out.println(size);
	TestCase.assertEquals(2029568, size);
    }
}
