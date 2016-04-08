package br.edu.ufu.comp.pos.db.imageretrieval.dataset;

import java.io.File;
import java.util.Arrays;
import java.util.function.Consumer;

import br.edu.ufu.comp.pos.db.imageretrieval.commons.Utils;
import br.edu.ufu.comp.pos.db.imageretrieval.dataset.image.Image;

public class GeneratedDataset extends Dataset {

    File base;
    File images;
    File testFiles;
    File trainFiles;
    File trainBinFile;
    File testBinFile;

    public GeneratedDataset(String workspace, String datasetName) {
        this.base = Utils.getDatesetPath(workspace, datasetName);
        this.fillVariables();
    }

    public GeneratedDataset(String datasetName) {
        this.base = Utils.getDatesetPath(System.getenv().get("DATASET_WORKSPACE"), datasetName);
        this.fillVariables();
    }

    private void fillVariables() {
        this.images = new File(base, "images");
        this.trainBinFile = new File(base, "train.sift");
        this.testBinFile = new File(base, "test.sift");
        this.trainFiles = new File(base, "train.txt");
        this.testFiles = new File(base, "test.txt");

    }

    @Override
    protected void trainSet(Consumer<Image> c) {
        throw new UnsupportedOperationException();

    }

    @Override
    protected void testSet(String clazz, Consumer<Image> c) {
        throw new UnsupportedOperationException();

    }

    @Override
    public String[] getTestClasses() {
        throw new UnsupportedOperationException();
    }

    @Override
    public File getDatasetFeaturesFile() {
        return new File(base, "sift.bin");
    }

    @Override
    public String quality(Image query, String imgName) {
        throw new UnsupportedOperationException();
    }

    protected File[] listImagesFiles() {
        File[] listFiles = images.listFiles();
        Arrays.sort(listFiles);
        return listFiles;
    }

    public File[] listTrainImagesFiles() {
        return listImagesFiles();
    }

    protected File[] listTestImagesFilesFromTrainSet(){
        return null;
    }

}
