package br.edu.ufu.comp.pos.db.imageretrieval.framework.base;

import java.io.File;

import org.apache.commons.io.IOUtils;

import lombok.SneakyThrows;

public class SiftExtractor {

    @SneakyThrows
    public static byte[] extract(File file) {
        String lowePath = SiftExtractor.class.getClassLoader().getResource("binaries/siftLowe").getFile();


        ProcessBuilder processBuilder = new ProcessBuilder(lowePath);
        processBuilder.redirectInput(file);
        Process start = processBuilder.start();
        start.waitFor();
        
        String output = IOUtils.toString(start.getInputStream());

        return null;
    }

}
