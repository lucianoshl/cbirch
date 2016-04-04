package br.edu.ufu.comp.pos.db.imageretrieval.dataset;

import java.io.File;
import java.util.Arrays;
import java.util.function.Consumer;

import br.edu.ufu.comp.pos.db.imageretrieval.dataset.image.Image;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.SiftExtractor;

public class UkbenchDataset extends Dataset {

    private File base;

    public UkbenchDataset(String base) {
        this.base = new File(base);
    }

    public static void main(String[] args) {
        UkbenchDataset dataset = new UkbenchDataset(
                "/home/lucianos/workspace/luciano/workspace-birch/datasets/ukbench");
        File folder = new File(dataset.base, "full");
        File[] images = folder.listFiles();
        Arrays.sort(images);
        for (File file : images) {
            byte[] sifts = SiftExtractor.extract(file);
        }

    }

    @Override
    protected void trainSet(Consumer<Image> c) {

    }

    @Override
    protected void testSet(String clazz, Consumer<Image> c) {
        // TODO Auto-generated method stub

    }

    @Override
    public String[] getTestClasses() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public File getDatasetFeaturesFile() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String quality(Image query, String imgName) {
        // TODO Auto-generated method stub
        return null;
    }

}
