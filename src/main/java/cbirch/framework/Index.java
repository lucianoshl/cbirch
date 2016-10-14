package cbirch.framework;


import cbirch.clustering.ClusteringMethod;
import cbirch.clustering.TreeNode;
import cbirch.dataset.Dataset;
import cbirch.dataset.Image;
import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * Created by void on 9/21/16.
 */
public abstract class Index<IndexElement> {

    public final Dataset dataset;

    protected final Map< TreeNode, IndexElement > index = new HashMap<>();


    protected Index( Dataset dataset ) {
        this.dataset = dataset;
    }


    public abstract void build( ClusteringMethod clustering );


    public abstract Image[] find( Image query, int k );
}
