package cbirch.dataset;


/**
 * Created by void on 9/24/16.
 */
public abstract class BinaryDataset extends BasicDataset {

    public BinaryDataset( String datasetName, String siftBinaryFileName, String siftPositionsFileName ) {
        super( datasetName, siftBinaryFileName, siftPositionsFileName );
    }
}
