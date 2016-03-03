package br.edu.ufu.comp.pos.db.imageretrieval.framework;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import br.edu.ufu.comp.pos.db.imageretrieval.dataset.Dataset;
import br.edu.ufu.comp.pos.db.imageretrieval.dataset.image.OxfordImage;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.ClusterTree;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.DatasetFactory;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.Histogram;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.TreeFactory;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.tree.BirchTree;

public class Launcher {

    final static Logger logger = Logger.getLogger(Launcher.class);
    
    public static void main(String[] args) throws IOException {
	
	if (args.length == 0) {
	    throw new IllegalArgumentException("tree name is required");
	}

	Dataset dataset = new DatasetFactory().create(args);
	BirchTree tree = new TreeFactory().create(args);
	new Launcher().run(dataset, tree, 4);

    }

    public void run(Dataset dataset, ClusterTree tree, int K) throws IOException {

	dataset.scanTrainSetSifts((sift) -> tree.insertEntry(sift));

	tree.optimize();
	tree.finishBuild();

	dataset.scanTrainSet((img) -> tree.index(img));

	List<Double> averagePrecision = new ArrayList<Double>();

	for (String clazz : dataset.getTestClasses()) {
	    List<Double> precisionList = new ArrayList<Double>();
	    dataset.scanTestSet(clazz, (query) -> {
		precisionList.add(precision(dataset, tree, query, K));
	    });	    
	    
	    if (!precisionList.isEmpty()){ // for developer testing
		averagePrecision.add(precisionList.stream().mapToDouble(a -> a).average().getAsDouble());
	    }  
	}

	System.out.println("Vocabulary size: "+ tree.getWordsSize());
	System.out.println("mAP: " + averagePrecision.stream().mapToDouble(a -> a).average().getAsDouble());

    }

    private double precision(Dataset dataset, ClusterTree tree, OxfordImage query, int K) {
	
	
	
	int queryAssert = 0;
	List<Histogram> results = tree.findTopK(query, K);
	for (int j = 0; j < results.size(); j++) {
	    String imgName = results.get(j).getImage().getImage().getName();
	    String classification = dataset.quality(query, imgName);
	    if (Arrays.asList("good", "ok", "junk").contains(classification)) {
		queryAssert += 1;
	    }
	}
	return queryAssert/Double.valueOf(K);
    }
}
