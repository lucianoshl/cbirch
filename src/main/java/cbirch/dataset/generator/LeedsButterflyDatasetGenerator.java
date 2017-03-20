package cbirch.dataset.generator;


import cbirch.sift.SiftExtractor;


/**
 * Download http://www.comp.leeds.ac.uk/scs6jwks/dataset/leedsbutterfly/files/
 * leedsbutterfly_dataset_v1.0.zip
 */
public class LeedsButterflyDatasetGenerator extends DatasetGenerator {

    public LeedsButterflyDatasetGenerator( SiftExtractor extractor ) {

        super( extractor );
    }


    @Override
    protected String datasetName() {

        return "leedsbutterfly";
    }
}
