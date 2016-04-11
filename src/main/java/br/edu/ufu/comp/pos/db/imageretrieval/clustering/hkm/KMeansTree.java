package br.edu.ufu.comp.pos.db.imageretrieval.clustering.hkm;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Arrays;
import java.util.HashSet;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.jmatio.io.MatFileReader;
import com.jmatio.types.MLArray;
import com.jmatio.types.MLEmptyArray;
import com.jmatio.types.MLInt32;
import com.jmatio.types.MLStructure;

import br.edu.ufu.comp.pos.db.imageretrieval.clustering.commons.AbstractTreeNode;
import br.edu.ufu.comp.pos.db.imageretrieval.clustering.commons.ClusterTree;
import br.edu.ufu.comp.pos.db.imageretrieval.dataset.Dataset;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.sift.SiftScaled;
import lombok.SneakyThrows;

public class KMeansTree implements ClusterTree {

    final static Logger logger = Logger.getLogger(KMeansTree.class);

    private long id;

    private int branchingFactor;
    private int leaves;

    private TreeNode root;

    private File tmpFolder;

    private int amountLeaves = 0;

    @SneakyThrows
    public KMeansTree(int branchingFactor, int leaves) {
        this.branchingFactor = branchingFactor;
        this.leaves = leaves;
        this.id = System.currentTimeMillis();
        this.tmpFolder = Files.createTempDirectory("khm-" + id).toFile();
    }

    @Override
    @SneakyThrows
    public void finishBuild() {
        MatFileReader matFileReader = new MatFileReader(this.getStoredTreeFile());
        logger.info("Build tree with matlab result");
        createTree(matFileReader);
        logger.info("Removing tmp files");
        FileUtils.deleteDirectory(tmpFolder);
        logger.info("labeling clusters");
        getEntriesAmount();
        logger.info("hkm tree build completed");
    }

    private void createTree(MatFileReader matFileReader) {
        MLStructure tree = (MLStructure) matFileReader.getContent().get("tree");
        MLInt32 centers = (MLInt32) tree.getField("centers");
        MLArray subTree = tree.getField("sub");

        root = createNode(new double[branchingFactor], (MLInt32) centers, subTree);

    }

    private TreeNode createNode(double[] point, MLInt32 centers, MLArray mlArray) {
        TreeNode node = new TreeNode(point, branchingFactor);

        int pointsQte = centers.getSize() / 128;
        for (int i = 0; i < pointsQte; i++) {
            double[] center = extractCenter(centers, i);

            if (mlArray instanceof MLEmptyArray) {
                node.addChild(new TreeNode(center, branchingFactor));
            } else {
                MLStructure iterateSubTree = (MLStructure) mlArray;
                node.addChild(createNode(center, (MLInt32) iterateSubTree.getField("centers", i),
                        iterateSubTree.getField("sub", i)));
            }
        }

        return node;

    }

    private double[] extractCenter(MLInt32 centers, int i) {
        double[] point = new double[128];
        for (int j = 0; j < 128; j++) {
            point[j] = centers.get(j, i);
        }
        return point;
    }

    private String getStoredTreeFile() {
        return new File(tmpFolder, "storedTree.mat").getAbsolutePath();
    }

    @Override
    public int getEntriesAmount() {
        if (amountLeaves == 0) {
            walk(root);
        }

        return amountLeaves;
    }

    private void walk(TreeNode treeNode) {
        if (treeNode.isLeaf()) {
            treeNode.setId(amountLeaves);
            amountLeaves++;
        } else {
            for (TreeNode entry : treeNode.getEntries()) {
                entry.setId(-1);
                walk(entry);
            }
        }
    }

    @Override
    public AbstractTreeNode findClosestCluster(double[] sift) {

        return root.findClosestCluster(sift);
    }

    @Override
    @SneakyThrows
    public void build(Dataset dataset) {

        if (dataset.getSiftReader().getClass().equals(SiftScaled.class)) {
            throw new IllegalStateException("the dataset can not be scaled in hkm");
        }

        String featuresFile = dataset.getSiftTrainFile().getAbsolutePath();
        long featuresAmount = dataset.getFeaturesSize();
        int branchingFactor = this.branchingFactor;
        int nLeaves = this.leaves;
        String outputFile = this.getStoredTreeFile();

        String script = FileUtils
                .readFileToString(new File(this.getClass().getClassLoader().getResource("matlab/hkm.m").getFile()));

        script = script.replace("%featuresFile%", featuresFile);
        script = script.replace("%featuresAmount%", String.valueOf(featuresAmount));
        script = script.replace("%branchingFactor%", String.valueOf(branchingFactor));
        script = script.replace("%nLeaves%", String.valueOf(nLeaves));
        script = script.replace("%outputFile%", outputFile);

        File scriptFile = new File(tmpFolder, "hkm.m");
        FileUtils.write(scriptFile, script);

        Files.setPosixFilePermissions(scriptFile.toPath(),
                new HashSet<PosixFilePermission>(Arrays.asList(PosixFilePermission.values())));

        logger.info("running matlab in " + tmpFolder);
        Process process = Runtime.getRuntime().exec(
                new String[] { "matlab", "-nodesktop", "-nosplash", "-r", "hkm;quit" }, new String[] {}, tmpFolder);

        process.waitFor();
        logger.info("command completed");
    }

}
