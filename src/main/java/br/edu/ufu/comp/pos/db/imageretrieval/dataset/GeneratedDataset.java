package br.edu.ufu.comp.pos.db.imageretrieval.dataset;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import br.edu.ufu.comp.pos.db.imageretrieval.commons.Utils;
import br.edu.ufu.comp.pos.db.imageretrieval.dataset.image.Image;
import br.edu.ufu.comp.pos.db.imageretrieval.dataset.image.OxfordImage;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ArrayUtils;

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
    @SneakyThrows
    protected void trainSet(Consumer<Image> c) {
        BufferedReader reader = new BufferedReader(new FileReader(trainFiles));

        int offset = 0;
        String line;
        while ((line = reader.readLine()) != null) {
            String[] split = line.split(" ");
            String fileName = split[0];
            Integer siftSize = Integer.valueOf(split[1]);
            c.accept(new OxfordImage(testBinFile, new File(images, fileName), offset, siftSize, siftReader));
            offset += siftSize * 128;
        }

        reader.close();

    }

    @Override
    @SneakyThrows
    protected void testSet(String clazz, Consumer<Image> c) {
        BufferedReader reader = new BufferedReader(new FileReader(testFiles));

        int offset = 0;
        String line;
        while ((line = reader.readLine()) != null) {
            String[] split = line.split(" ");
            String fileName = split[0];
            Integer siftSize = Integer.valueOf(split[1]);
            if (fileName.contains(clazz + ".")){
                c.accept(new OxfordImage(testBinFile, new File(images, fileName), offset, siftSize, siftReader));
            }
            offset += siftSize * 128;
        }

        reader.close();

    }

    @Override
    @SneakyThrows
    public String[] getTestClasses() {

        List<String> result = new ArrayList<String>();
        List<String> readLines = IOUtils.readLines(new FileReader(this.testFiles));
        for (String line : readLines) {
            String fileName = line.split(" ")[0];
            result.add(fileName.replace(".pgm", ""));
        }

        return result.toArray(new String[result.size()]);
    }

    @Override
    public File getSiftTrainFile() {
        return new File(base, "train.sift");
    }

    @Override
    public File getSiftTestFile() {
        return new File(base, "test.sift");
    }

    @Override
    public void scanSifts(Consumer<double[]> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    @SneakyThrows
    public String quality(Image query, String imgName) {

        File gtFiles = new File(base,"gt_files");
        String[] itens = FileUtils.readFileToString(new File(gtFiles, query.getImage().getName() + ".txt")).split("\n");
        return ArrayUtils.contains(itens,imgName) ? "good" : "absent";
    }

    protected File[] listImagesFiles() {
        File[] listFiles = images.listFiles();
        Arrays.sort(listFiles);
        return listFiles;
    }

    public File[] listTrainImagesFiles() {
        return listImagesFiles();
    }

    protected File[] listTestImagesFilesFromTrainSet() {
        File[] listTrainImagesFiles = this.listTrainImagesFiles();
        List<File> result = new ArrayList<File>();
        for (int i = 0; i < listTrainImagesFiles.length; i++) {
            File file = listTrainImagesFiles[i];
            if (i % 4 == 0) {
                result.add(file);
            }
        }
        return result.toArray(new File[result.size()]);
    }

    public List<double[]> trainSet() {
        List<double[]> result =  new ArrayList<>();
        scanTrainSet((c)-> {
            c.scan((sift) -> {
                result.add(sift);
            });
        });

        return result;
    }
}
