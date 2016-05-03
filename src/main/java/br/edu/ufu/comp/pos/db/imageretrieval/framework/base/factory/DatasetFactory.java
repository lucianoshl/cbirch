package br.edu.ufu.comp.pos.db.imageretrieval.framework.base.factory;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.edu.ufu.comp.pos.db.imageretrieval.commons.Utils;
import br.edu.ufu.comp.pos.db.imageretrieval.dataset.Dataset;
import br.edu.ufu.comp.pos.db.imageretrieval.dataset.OxfordDataset;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.Result;

public class DatasetFactory {

    final static Logger logger = LoggerFactory.getLogger(DatasetFactory.class);

    public Dataset create(String[] args) {

        String workspace = System.getenv().get("DATASET_WORKSPACE");
        String datasetName = args[1];

        File datasetPath = Utils.getDatesetPath(workspace, datasetName);

        Result.extraInfo("Dataset workspace", workspace);
        Result.extraInfo("Dataset name", datasetName);
        Result.extraInfo("Dataset path", datasetPath);

        Result.current.setDatasetPath(datasetPath);
        Dataset dataset = null;

        if (new File(datasetPath, "README2.txt").exists()) {
            Result.extraInfo("Dataset class", OxfordDataset.class);
            dataset = OxfordDataset.createFromBase(workspace, datasetName);
        } else {
            throw new UnsupportedOperationException("unsupported dataset in " + datasetPath.getAbsolutePath());
        }

        Result.extraInfo("Dataset features", dataset.getFeaturesSize());

        return dataset;

    }

}
