package br.edu.ufu.comp.pos.db.imageretrieval.dataset;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.function.Consumer;

import org.apache.commons.io.filefilter.FileFilterUtils;

public class Dataset {
	private String datasetPath;

	public Dataset(String path) {
		this.datasetPath = path;
	}

	public void scan(Consumer<double[]> c) throws FileNotFoundException, IOException {
//		FileFilter suffixFileFilter = FileFilterUtils.suffixFileFilter(".sift");
//		File[] files = new File(datasetPath).listFiles(suffixFileFilter);
		File[] files = new File(datasetPath).listFiles(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".sift");
			}
		});
		Arrays.sort(files);

		byte[] buffer = new byte[128];

		for (int i = 0; i < files.length; i++) {
			File sift = files[i];
			FileInputStream binFileReader = new FileInputStream(sift);
			while (binFileReader.read(buffer) != -1) {
				c.accept(convertToDouble(buffer));
			}
			binFileReader.close();
		}
	}

	private static double[] convertToDouble(byte[] buffer) {
		double[] result = new double[buffer.length];
		for (int i = 0; i < buffer.length; i++) {
			result[i] = buffer[i];
		}
		return result;
	}
}
