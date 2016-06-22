package br.edu.ufu.comp.pos.db.imageretrieval.framework.base.factory;

import br.edu.ufu.comp.pos.db.imageretrieval.commons.Utils;
import br.edu.ufu.comp.pos.db.imageretrieval.dataset.Dataset;
import br.edu.ufu.comp.pos.db.imageretrieval.dataset.GeneratedDataset;
import br.edu.ufu.comp.pos.db.imageretrieval.dataset.OxfordDataset;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.sift.Sift;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.sift.SiftScaled;
import cbirch.Report;
import org.apache.log4j.Logger;

import java.io.File;

public class DatasetFactory {

    final static Logger logger = Logger.getLogger(DatasetFactory.class);

    public Dataset create(String[] args) {

        String workspace = System.getenv().get("DATASET_WORKSPACE");
        String datasetName = args[2];

        Sift siftReader = null;
        boolean normalized = args[1].equals("normalized");
        Report.info("Normalized", normalized);
        if (normalized) {
            siftReader = new SiftScaled();
        } else if (args[1].equals("non-normalized")) {
            siftReader = new Sift();
        } else {
            new IllegalStateException("invalid normalized parameter: normalized or non-normalized");
        }


        File datasetPath = Utils.getDatesetPath(workspace, datasetName);

        Report.info("Dataset workspace", workspace);
        Report.info("Dataset name", datasetName);
        Report.info("Dataset path", datasetPath);

        Dataset dataset = null;

        if (new File(datasetPath, "README2.txt").exists()) {
            Report.info("Dataset class", OxfordDataset.class);
            dataset = OxfordDataset.createFromBase(workspace, datasetName);
        } else if (new File(datasetPath, "train.sift").exists()) {
            dataset = new GeneratedDataset(datasetName);

        } else {
            throw new UnsupportedOperationException("unsupported dataset in " + datasetPath.getAbsolutePath());
        }

        Report.info("Dataset features", dataset.getFeaturesSize());

        dataset.setSiftReader(siftReader);
        return dataset;

    }

}
