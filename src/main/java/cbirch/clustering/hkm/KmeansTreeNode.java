package cbirch.clustering.hkm;


import cbirch.clustering.TreeNode;
import org.apache.commons.math3.ml.clustering.Clusterable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by void on 9/11/16.
 */
public class KmeansTreeNode implements Clusterable,TreeNode {

    private final double[] point;

    private List< KmeansTreeNode > childen = new ArrayList< KmeansTreeNode >();


    public KmeansTreeNode( double[] point ) {
        this.point = point;
    }


    @Override
    public double[] getPoint() {

        return point;
    }


    public List< KmeansTreeNode > getChilden() {

        return childen;
    }


    public void setChilden( List< KmeansTreeNode > childen ) {

        this.childen = childen;
    }

    @Override
    public String toString() {
        return Arrays.toString(point);
    }

    @Override
    public int getId() {
        throw new UnsupportedOperationException();
    }
}
