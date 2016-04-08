package br.edu.ufu.comp.pos.db.imageretrieval.dataset;

import java.io.File;
import java.io.FileWriter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.SiftExtractor;
import lombok.Builder;
import lombok.SneakyThrows;

@Builder
public class DatasetGenerator {
    private File originImageFolder;
    private GeneratedDataset dataset;

    @SneakyThrows
    public static void main(String[] args) {

        DatasetGenerator generator = DatasetGenerator.builder().dataset(new GeneratedDataset("ukbench"))
                .originImageFolder(new File("/home/lucianos/tmp/ukbench/full")).build();
        generator.generate();

    }

    @SneakyThrows
    private void generate() {
        FileUtils.deleteDirectory(dataset.base);
        copyImages();

        File[] train = dataset.listTrainImagesFiles();
        File[] test = dataset.listTestImagesFilesFromTrainSet();

        generateSifts(dataset.trainFiles, dataset.trainBinFile, train);
        generateSifts(dataset.testFiles, dataset.testBinFile, test);
    }

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

        for (int i = 0; i < images.length; i++) {
            File file = images[i];
            byte[][] sifts = SiftExtractor.extract(file);
            writer.write(file.getName() + " " + sifts.length);
            for (byte[] bs : sifts) {
                FileUtils.writeByteArrayToFile(binFile, bs, true);

            }
            if (i + 1 < images.length) {
                writer.write("\n");
            }
        }

        FileUtils.writeByteArrayToFile(binFile, new byte[12], true);
        writer.close();

    }

    @SneakyThrows
    private void copyImages() {
        FileUtils.deleteDirectory(dataset.images);
        dataset.images.mkdirs();

        File[] listImages = originImageFolder.listFiles();
        for (File originImage : listImages) {
            convertImage(originImage, "pgm");
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
