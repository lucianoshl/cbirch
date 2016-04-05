package br.edu.ufu.comp.pos.db.imageretrieval.framework.base;

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

        scanner.nextLine();

        int siftCount = -1;

        while (scanner.hasNextDouble())
            siftCount++;
            scanner.nextLine();
        // } else {
        // throw new IllegalStateException("invalid format");
        // }

        byte[] sift = new byte[128];
        for (int i = 0; i < sift.length; i++) {
            sift[i] = (byte) scanner.nextInt();

        }
        return null;
    };

    // return null;

    // InputStream inputStream = start.getInputStream();
    // BufferedReader sufferedReader = new BufferedReader(new
    // InputStreamReader(inputStream));

    // String readLine = sufferedReader.readLine();
    // String[] firstLine = readLine.split(" ");
    // byte[][] sifits = new
    // byte[Integer.valueOf(firstLine[0])][Integer.valueOf(firstLine[1])];
    //
    // int index = -1;
    // do {
    // readLine = sufferedReader.readLine().trim();
    // String[] splitted = readLine.split(" ");
    // if (splitted.length == 4){
    // index++;
    // continue;
    // }
    // System.out.println(1);
    //
    // }while(true);
    //
    //
    //// keypoints found

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
