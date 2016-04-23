package br.edu.ufu.comp.pos.db.imageretrieval.framework.base.sift;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;

import lombok.SneakyThrows;

public class SiftExtractor {

    @SneakyThrows
    public static byte[] extract(File file) {

        String output = runExternalLowe(file);

        Scanner scanner = new Scanner(output);

        int totalSift = scanner.nextInt();

        scanner.nextLine();

        int siftCount = -1;

//        byte[][] result = new byte[totalSift][];
        byte[] result2 = new byte[totalSift*128];
        
        

        while (scanner.hasNextDouble()) {
            siftCount++;
            scanner.nextDouble();
            scanner.nextLine();
//            result[siftCount] = new byte[128];
            for (int i = 0; i < 128; i++) {
                result2[siftCount * 128 + i] = (byte) scanner.nextInt();
//                System.out.println(siftCount * 128 + i);
            }
        }
        scanner.close();

        return result2;
    };

    private static String runExternalLowe(File file) throws IOException, InterruptedException {
        String lowePath = SiftExtractor.class.getClassLoader().getResource("binaries/siftLowe").getFile();
        File siftTmpFile = File.createTempFile("sift", file.getName());
        ProcessBuilder processBuilder = new ProcessBuilder(lowePath);
        processBuilder.redirectInput(file);
        processBuilder.redirectOutput(siftTmpFile);
        Process start = processBuilder.start();
        start.waitFor();
        String output = FileUtils.readFileToString(siftTmpFile);
        siftTmpFile.delete();
        return output;
    }

}