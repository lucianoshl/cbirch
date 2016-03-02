package br.edu.ufu.comp.pos.db.imageretrieval.framework;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import br.edu.ufu.comp.pos.db.imageretrieval.dataset.Dataset;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.ClusterTree;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.DatasetFactory;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.Histogram;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.TreeFactory;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.tree.BirchTree;

public class Launcher {

    double[] averagePrecision;
    int i;

    public static void main(String[] args) throws IOException {

	if (args.length == 0) {
	    throw new IllegalArgumentException("tree name is required");
	}

	Dataset dataset = new DatasetFactory().create(args);
	BirchTree tree = new TreeFactory().create(args);
	new Launcher().run(dataset, tree);

    }

    public void run(Dataset dataset, ClusterTree tree) throws IOException {

	dataset.scanTrainSetSifts((sift) -> tree.insertEntry(sift));

	// tree.optimize();
	tree.finishBuild();

	dataset.scanTrainSet((img) -> tree.index(img));

	String[] testClasses = dataset.getTestClasses();
	averagePrecision = new double[testClasses.length];

	for (i = 0; i < testClasses.length; i++) {
	    String clazz = testClasses[i];
	    System.out.println("=============== START QUERY FOR CLASS " + clazz);
	    dataset.scanTestSet(clazz, (query) -> {
		double queryAssert = 0.0;
		System.out.println();
		System.out.println("\tQuery: " + query.getImage().getName());
		List<Histogram> results = tree.findTopK(query, 4);
		for (int j = 0; j < results.size(); j++) {
		    String imgName = results.get(j).getImage().getImage().getName();
		    String classification = dataset.quality(query, imgName);
		    System.out.println(String.format("\t\tRank %s: %s %s", j, imgName, classification));
		    if (Arrays.asList("good","ok","junk").contains(classification)){
			System.out.println("Classification is valid"+averagePrecision[i]);
			queryAssert += 1;
			System.out.println("now is "+averagePrecision[i] );
		    }
		}

		averagePrecision[i] += queryAssert/4.0;
		System.out.println("Average precision: " + averagePrecision[i]);
		System.out.println();
	    });
	    averagePrecision[i] = averagePrecision[i]/5;
	    System.out.println();
	}
	
	double mAP = 0.0;
	for (int i = 0; i < averagePrecision.length; i++) {
	    mAP += averagePrecision[i];
	}
	System.out.println("mAP: " + mAP/Double.valueOf(averagePrecision.length));

    }
}
