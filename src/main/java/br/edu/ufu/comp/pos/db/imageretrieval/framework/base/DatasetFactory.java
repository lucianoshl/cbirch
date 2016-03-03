package br.edu.ufu.comp.pos.db.imageretrieval.framework.base;

import java.io.File;

import org.apache.log4j.Logger;

import br.edu.ufu.comp.pos.db.imageretrieval.commons.Utils;
import br.edu.ufu.comp.pos.db.imageretrieval.dataset.Dataset;
import br.edu.ufu.comp.pos.db.imageretrieval.dataset.OxfordDataset;

public class DatasetFactory {

    final static Logger logger = Logger.getLogger(DatasetFactory.class);

    public Dataset create(String[] args) {

	String workspace = System.getenv().get("DATASET_WORKSPACE");
	String datasetName = args[1];

	File datasetPath = Utils.getDatesetPath(workspace, datasetName);

	logger.info("Dataset workspace: " + workspace);
	logger.info("Dataset name: " + datasetName);
	logger.info("Dataset path: " + datasetPath);

	if (new File(datasetPath, "README2.txt").exists()) {
	    return OxfordDataset.createFromBase(workspace, datasetName);
	} else {
	    throw new UnsupportedOperationException("unsupported dataset in " + datasetPath.getAbsolutePath());
	}

    }

}
