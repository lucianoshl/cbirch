//package br.edu.ufu.comp.pos.db.imageretrieval.clustering.birch.cftree;
//
//
//import junit.framework.TestCase;
//
//import org.apache.log4j.Logger;
//import org.junit.Before;
//import org.junit.Test;
//
//import br.edu.ufu.comp.pos.db.imageretrieval.clustering.commons.ClusterTree;
//import br.edu.ufu.comp.pos.db.imageretrieval.dataset.OxfordDataset;
//import br.edu.ufu.comp.pos.db.imageretrieval.dataset.image.OxfordImage;
//import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.factory.TreeFactory;
//import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.sift.SiftScaled;
//
//
///**
// * Created by lucianos on 6/27/16.
// */
//public class CFTreeTest {
//
//    final static Logger logger = Logger.getLogger( CFTreeTest.class );
//
//    OxfordImage test;
//
//    private String workspace;
//
//    private String dsName;
//
//    private OxfordDataset dataset;
//
//
//    @Before
//    public void before() {
//
//        this.workspace = System.getenv().get( "DATASET_WORKSPACE" );
//        this.dsName = "oxford";
//        this.dataset = OxfordDataset.createFromBase( workspace, dsName );
//        this.dataset.setSiftReader( new SiftScaled() );
//    }
//
//
//    @Test
//    public void testing30() {
//
//        doTest( 99841, 30, 0 );
//        doTest( 94129, 30, 1 );
//        doTest( 87124, 30, 2 );
//    }
//
//
//    @Test
//    public void testing50() {
//
//        doTest( 173899, 50, 0 );
//        doTest( 163314, 50, 1 );
//        doTest( 150114, 50, 2 );
//    }
//
//
//    @Test
//    public void testing100() {
//
//        doTest( 364778, 100, 0 );
//        doTest( 343624, 100, 1 );
//        doTest( 313481, 100, 2 );
//    }
//
//
//    private void doTest( int expectedLeaves, int scanLimit, int builds ) {
//
//        ClusterTree cfTree = new TreeFactory().createCFTree( 75, 0d, 1024, 1000000 );
//        dataset.setScanLimit( scanLimit );
//        cfTree.build( dataset );
//        for ( int j = 0; j < builds; j++ ) {
//            ( (CFTree) cfTree ).rebuildTree();
//        }
//        int i = cfTree.setClustersNames();
//        TestCase.assertEquals( expectedLeaves, i );
//    }
//}