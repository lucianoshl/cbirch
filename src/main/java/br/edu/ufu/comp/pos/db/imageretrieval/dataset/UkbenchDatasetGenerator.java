package br.edu.ufu.comp.pos.db.imageretrieval.dataset;

import java.io.File;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;

import lombok.SneakyThrows;

public class UkbenchDatasetGenerator extends DatasetGenerator {

    public UkbenchDatasetGenerator(GeneratedDataset generatedDataset, File file) {
        super(generatedDataset, file);
    }

    @SneakyThrows
    public static void main(String[] args) {

        new UkbenchDatasetGenerator(new GeneratedDataset(args[0]),
                new File(args[1])).generate();

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
            if (i%4 == 0){
                currentFile = new File(gtFiles,file.getName() + ".txt");
                currentFile.createNewFile();
                FileUtils.writeLines(currentFile, Arrays.asList(file.getName()),true);
            } else {
                FileUtils.writeLines(currentFile, Arrays.asList(file.getName()),true);
            }
        }

    }

}
