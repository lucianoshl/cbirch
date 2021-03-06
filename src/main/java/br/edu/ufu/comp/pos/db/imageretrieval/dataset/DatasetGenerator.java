package br.edu.ufu.comp.pos.db.imageretrieval.dataset;

import java.io.File;
import java.io.FileWriter;
import java.io.RandomAccessFile;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.sift.SiftExtractor;
import lombok.SneakyThrows;

public abstract class DatasetGenerator {
    protected File originImageFolder;
    protected GeneratedDataset dataset;

    public DatasetGenerator(GeneratedDataset generatedDataset, File file) {
        this.dataset = generatedDataset;
        this.originImageFolder = file;
    }

    @SneakyThrows
    public void generate() {
        FileUtils.deleteDirectory(dataset.base);
        copyImages();

        File[] train = dataset.listTrainImagesFiles();
        File[] test = dataset.listTestImagesFilesFromTrainSet();

        generateSifts(dataset.trainFiles, dataset.trainBinFile, train);
        generateSifts(dataset.testFiles, dataset.testBinFile, test);
        generateGroundtruth();
    }

    protected abstract void generateGroundtruth();

    @SneakyThrows
    private void generateListFiles(File trainFiles, File[] train) {
        FileWriter writer = new FileWriter(trainFiles);
        for (int i = 0; i < train.length; i++) {
            File file = train[i];
            writer.write(file.getName());
            if (i + 1 != train.length) {
                writer.write("\n");
            }
        }
        writer.close();

    }

    @SneakyThrows
    private void generateSifts(File fileDescriptor, File binFile, File[] images) {
        FileUtils.deleteQuietly(binFile);
        FileUtils.deleteQuietly(fileDescriptor);
        binFile.createNewFile();
        fileDescriptor.createNewFile();
        FileWriter writer = new FileWriter(fileDescriptor);

        RandomAccessFile randomAccessFile = new RandomAccessFile(binFile, "rw");
        randomAccessFile.seek(0);

        for (int i = 0; i < images.length; i++) {
            File file = images[i];
            byte[] sifts = SiftExtractor.extract(file);


            writer.write(file.getName() + " " + sifts.length / 128);

//            FileUtils.writeByteArrayToFile(binFile, sifts, true);
            randomAccessFile.write(sifts);
            System.out.println("LENGTH: "+ randomAccessFile.length());

            if (i + 1 < images.length) {
                writer.write("\n");
            }
        }

        randomAccessFile.write(new byte[12]);
//        FileUtils.writeByteArrayToFile(binFile, new byte[12], true);
        randomAccessFile.close();
        writer.close();

    }

    @SneakyThrows
    private void copyImages() {
        FileUtils.deleteDirectory(dataset.images);
        dataset.images.mkdirs();

        File[] listImages = originImageFolder.listFiles();
        for (File originImage : listImages) {
            if (originImage.getName().endsWith(".pgm")) {
                FileUtils.copyFile(originImage,new File(dataset.images,originImage.getName()));
            } else {
                convertImage(originImage, "pgm");
            }

        }

    }

    @SneakyThrows
    private void convertImage(File originImage, String format) {
        String origin = originImage.getAbsolutePath();
        String extension = FilenameUtils.getExtension(origin);
        String dest = new File(dataset.images, originImage.getName()).getAbsolutePath().replace("." + extension,
                "." + format);
        String command = "convert " + originImage.getAbsolutePath() + " " + dest;
        Runtime.getRuntime().exec(command).waitFor();
    }

}
