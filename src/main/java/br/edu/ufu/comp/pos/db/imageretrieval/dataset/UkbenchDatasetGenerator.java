package br.edu.ufu.comp.pos.db.imageretrieval.dataset;

import java.io.File;

import lombok.SneakyThrows;

public class UkbenchDatasetGenerator extends DatasetGenerator {

    public UkbenchDatasetGenerator(GeneratedDataset generatedDataset, File file) {
       super(generatedDataset,file);
    }

    @SneakyThrows
    public static void main(String[] args) {

        
        
        new UkbenchDatasetGenerator(new GeneratedDataset("ukbench"),
//                new File("/home/void/workspace/birch/workspace/datasets/raw-dataset-ukbench/full")).generate();
        new File(args[0])).generate();

    }

    protected void generateGroundtruth() {
        // TODO Auto-generated method stub

    }

}
