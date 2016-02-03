package br.edu.ufu.comp.pos.db.imageretrieval;

import java.io.IOException;

import br.edu.ufu.comp.pos.db.imageretrieval.clustering.birch.cftree.CFTree;
import br.edu.ufu.comp.pos.db.imageretrieval.dataset.Dataset;

public class Launcher {
	// -Xmx4096m -XX:MaxPermSize=256m
	// -javaagent:/home/lucianos/birch-experiment/src/main/resources/SizeOf.jar
	public static void main(String[] args) throws IOException {
		Dataset dataset = new Dataset("/home/lucianos/birch-experiment/datasets/formated/oxford");

		CFTree cfTree = new CFTree(10, 10, 50, true);
		cfTree.setAutomaticRebuild(true);
		cfTree.setMemoryLimit(1024l * 1024l * 2l);

		dataset.scan((a) -> cfTree.insertEntry(a));

		System.out.println(1);

		// cfTree.insertEntry(x);
	}

}
