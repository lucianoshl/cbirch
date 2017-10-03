package br.edu.ufu.comp.pos.db.imageretrieval.dataset;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;

import lombok.SneakyThrows;

public class UkbenchDatasetGenerator extends DatasetGenerator {

    public UkbenchDatasetGenerator(GeneratedDataset generatedDataset, File file) {
        super(generatedDataset, file);
    }

    @SneakyThrows
    public static void main(String[] args) {

        List<String> datasets = Arrays.asList("ukbench-00-1000", "ukbench-01-1000", "ukbench-02-1000", "ukbench-03-1000", "ukbench-04-1000", "ukbench-05-1000", "ukbench-06-1000", "ukbench-07-1000", "ukbench-08-1000", "ukbench-09-1000", "ukbench-10200", "ukbench-10-200");

        String workspace = System.getenv().get("DATASET_WORKSPACE");
        for (String name : datasets) {
            try {
                new UkbenchDatasetGenerator(new GeneratedDataset(name),
                        new File(workspace + "/raw-datasets/" + name)).generate();
            } catch (Exception e) {
                System.out.println("ERROORRR IN:" + name);
                e.printStackTrace();
            }
        }

    }

    @SneakyThrows
    protected void generateGroundtruth() {
        File gtFiles = new File(dataset.base, "gt_files");
        FileUtils.deleteQuietly(gtFiles);
        gtFiles.mkdirs();

        File currentFile = null;

        File[] listTrainImagesFiles = this.dataset.listTrainImagesFiles();
        for (int i = 0; i < listTrainImagesFiles.length; i++) {
            File file = listTrainImagesFiles[i];
            if (i % 4 == 0) {
                currentFile = new File(gtFiles, file.getName() + ".txt");
                currentFile.createNewFile();
                FileUtils.writeLines(currentFile, Arrays.asList(file.getName()), true);
            } else {
                FileUtils.writeLines(currentFile, Arrays.asList(file.getName()), true);
            }
        }

    }

}
