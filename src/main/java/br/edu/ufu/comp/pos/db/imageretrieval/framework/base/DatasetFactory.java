package br.edu.ufu.comp.pos.db.imageretrieval.framework.base;


import java.io.File;

import br.edu.ufu.comp.pos.db.imageretrieval.commons.Utils;
import br.edu.ufu.comp.pos.db.imageretrieval.dataset.Dataset;
import br.edu.ufu.comp.pos.db.imageretrieval.dataset.Oxford;
import br.edu.ufu.comp.pos.db.imageretrieval.dataset.OxfordDataset;


public class DatasetFactory {

    public Dataset create( String[] args ) {

        String workspace = args[ 1 ];
        String datasetName = args[ 2 ];

        File datasetPath = Utils.getDatesetPath(workspace,datasetName);
        
        if (new File(datasetPath,"README2.txt").exists()){
            OxfordDataset.createFromBase( workspace, datasetName );
        } else {
            throw new IllegalStateException();
        }
        
        
        return null;

    }
    
    public OxfordDataset create2( String[] args ) {

        String workspace = args[ 1 ];
        String datasetName = args[ 2 ];

        File datasetPath = Utils.getDatesetPath(workspace,datasetName);
        
        if (new File(datasetPath,"README2.txt").exists()){
            return OxfordDataset.createFromBase( workspace, datasetName );
        } else {
            throw new IllegalStateException();
        }
        
        

    }


    protected Dataset resolveDatasetByName( String workspace, String datasetName ) {

        if ( datasetName.equals( "oxford" ) ) {
            return Oxford.createFromBase( workspace, datasetName );
        } else {
            throw new UnsupportedOperationException();
        }
    }

}
