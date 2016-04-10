package br.edu.ufu.comp.pos.db.imageretrieval.framework.base.factory;

import java.io.File;

import org.apache.log4j.Logger;

import br.edu.ufu.comp.pos.db.imageretrieval.commons.Utils;
import br.edu.ufu.comp.pos.db.imageretrieval.dataset.Dataset;
import br.edu.ufu.comp.pos.db.imageretrieval.dataset.GeneratedDataset;
import br.edu.ufu.comp.pos.db.imageretrieval.dataset.OxfordDataset;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.Result;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.sift.Sift;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.sift.SiftScaled;

public class DatasetFactory {

    final static Logger logger = Logger.getLogger(DatasetFactory.class);

    public Dataset create(String[] args) {

        String workspace = System.getenv().get("DATASET_WORKSPACE");
        String datasetName = args[2];
        Sift siftReader = args[1].equals("1") ? new SiftScaled() : new Sift();

        File datasetPath = Utils.getDatesetPath(workspace, datasetName);

        Result.extraInfo("Dataset workspace", workspace);
        Result.extraInfo("Dataset name", datasetName);
        Result.extraInfo("Dataset path", datasetPath);

        Result.instance.setDatasetPath(datasetPath);
        Dataset dataset = null;

        if (new File(datasetPath, "README2.txt").exists()) {
            Result.extraInfo("Dataset class", OxfordDataset.class);
            dataset = OxfordDataset.createFromBase(workspace, datasetName);
        } else if (new File(datasetPath, "train.sift").exists()) {
            dataset = new GeneratedDataset(datasetName);

        } else {
            throw new UnsupportedOperationException("unsupported dataset in " + datasetPath.getAbsolutePath());
        }

        Result.extraInfo("Dataset features", dataset.getFeaturesSize());

        dataset.setSiftReader(siftReader);
        return dataset;

    }

}
