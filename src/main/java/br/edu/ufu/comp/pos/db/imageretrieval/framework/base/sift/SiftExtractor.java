package br.edu.ufu.comp.pos.db.imageretrieval.framework.base.sift;


import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFilePermission;
import java.util.HashSet;
import java.util.Locale;
import java.util.Scanner;
import java.util.Set;


public class SiftExtractor {

    @SneakyThrows
    public static byte[] extract(File file) {

        String output = runExternalLowe(file);

        Scanner scanner = new Scanner(output);
        scanner.useLocale(Locale.US);

        int totalSift = scanner.nextInt();

        scanner.nextLine();

        int siftCount = -1;

        byte[] result2 = new byte[totalSift*128];
        
        

        while (scanner.hasNextDouble()) {
            siftCount++;
            scanner.nextLine();
            for (int i = 0; i < 128; i++) {
                byte b = (byte) scanner.nextInt();
                result2[siftCount * 128 + i] = b;
            }
            scanner.nextLine();
        }
        scanner.close();

        return result2;
    };

    private static String runExternalLowe(File file) throws IOException, InterruptedException {

        String binFile = "binaries/siftLowe";
        if (System.getProperty("os.name").startsWith("Windows")){
            binFile = "binaries/siftWin32.exe";
        }
        String lowePath = SiftExtractor.class.getClassLoader().getResource(binFile).getFile();

        Set perms = new HashSet();
        perms.add( PosixFilePermission.OWNER_EXECUTE );
        perms.add( PosixFilePermission.OWNER_READ );
        perms.add( PosixFilePermission.OWNER_WRITE );

        Files.setPosixFilePermissions( new File( lowePath ).toPath(), perms );

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
