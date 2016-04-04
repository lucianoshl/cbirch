package br.edu.ufu.comp.pos.db.imageretrieval.clustering.hkm;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.math3.ml.distance.EuclideanDistance;
import org.apache.log4j.Logger;

import com.jmatio.io.MatFileReader;
import com.jmatio.types.MLArray;
import com.jmatio.types.MLEmptyArray;
import com.jmatio.types.MLInt32;
import com.jmatio.types.MLStructure;

import br.edu.ufu.comp.pos.db.imageretrieval.clustering.commons.AbstractTreeNode;
import br.edu.ufu.comp.pos.db.imageretrieval.dataset.Dataset;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.ClusterTree;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.SiftScaled;
import lombok.SneakyThrows;

public class KMeansTree implements ClusterTree {

    final static Logger logger = Logger.getLogger(KMeansTree.class);

    private static final EuclideanDistance distance = new EuclideanDistance();

    private long id;

    private int k = 2;
    private int leaves;

    private File tmpFolder;

    private TreeNode[] startCentroids;

    private int amountLeaves = 0;

    @SneakyThrows
    public KMeansTree(int branchingFactor, int leaves) {
        this.k = branchingFactor;
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

        startCentroids = new TreeNode[k];
        for (int i = 0; i < k; i++) {
            double[] point = extractCenter(centers, i);

            if (subTree instanceof MLEmptyArray) {
                startCentroids[i] = new TreeNode(point, k);
            } else {
                MLStructure subTreeStruct = (MLStructure) subTree;
                startCentroids[i] = createNode(point, (MLInt32) subTreeStruct.getField("centers", i),
                        subTreeStruct.getField("sub", i));
            }

        }

    }

    private TreeNode createNode(double[] point, MLInt32 centers, MLArray mlArray) {
        TreeNode node = new TreeNode(point, k);

        int pointsQte = centers.getSize() / 128;
        for (int i = 0; i < pointsQte; i++) {
            double[] center = extractCenter(centers, i);

            if (mlArray instanceof MLEmptyArray) {
                node.addChild(new TreeNode(center, k));
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
            for (TreeNode treeNode : startCentroids) {
                walk(treeNode);
            }
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

        Arrays.sort(startCentroids, (a, b) -> {
            return Double.compare(distance.compute(sift, a.getCentroid()), distance.compute(sift, b.getCentroid()));
        });

        return startCentroids[0].findClosestCluster(sift);
    }

    @Override
    @SneakyThrows
    public void build(Dataset dataset) {
        
        if (dataset.getSiftReader().getClass().equals(SiftScaled.class)){
            throw new IllegalStateException("the dataset can not be scaled in hkm");
        }

        String featuresFile = dataset.getDatasetFeaturesFile().getAbsolutePath();
        long featuresAmount = dataset.getFeaturesSize();
        int branchingFactor = this.k;
        int nLeaves = this.leaves;
        String outputFile = this.getStoredTreeFile();

        String script = FileUtils
                .readFileToString(new File(this.getClass().getClassLoader().getResource("hkm.m").getFile()));

        script = script.replace("%featuresFile%", featuresFile);
        script = script.replace("%featuresAmount%", String.valueOf(featuresAmount));
        script = script.replace("%branchingFactor%", String.valueOf(branchingFactor));
        script = script.replace("%nLeaves%", String.valueOf(nLeaves));
        script = script.replace("%outputFile%", outputFile);

        File scriptFile = new File(tmpFolder, "hkm.m");
        FileUtils.write(scriptFile, script);

        Set<PosixFilePermission> perms = new HashSet<PosixFilePermission>();
        perms.addAll(Arrays.asList(PosixFilePermission.values()));

        Files.setPosixFilePermissions(scriptFile.toPath(), perms);

        logger.info("running matlab in " + tmpFolder);
        Process process = Runtime.getRuntime().exec(
                new String[] { "matlab", "-nodesktop", "-nosplash", "-r", "hkm;quit" }, new String[] {}, tmpFolder);

        process.waitFor();
        logger.info("command completed");
    }

}
