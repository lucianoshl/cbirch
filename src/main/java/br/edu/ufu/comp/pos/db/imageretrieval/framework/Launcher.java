package br.edu.ufu.comp.pos.db.imageretrieval.framework;

import java.io.IOException;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.log4j.Logger;

import br.edu.ufu.comp.pos.db.imageretrieval.dataset.Dataset;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.ClusterTree;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.DatasetFactory;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.TreeFactory;

public class Launcher {

    final static Logger logger = Logger.getLogger(Launcher.class);

    public static void main(String[] args) throws IOException {

	if (args.length == 0) {
	    throw new IllegalArgumentException("tree name is required");
	}
	
	Dataset dataset = new DatasetFactory().create(args);
	ClusterTree tree = new TreeFactory().create(args);

	StopWatch stopWatch = new StopWatch();
	stopWatch.start();

	Result result = new Framework().run(dataset, tree, 4);
	
	logger.info("elapsed time " + stopWatch.getTime());
	
	logger.info(result.toString());
	
	result.save();
    }


}
