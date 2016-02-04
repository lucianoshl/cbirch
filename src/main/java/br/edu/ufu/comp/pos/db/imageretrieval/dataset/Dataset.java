package br.edu.ufu.comp.pos.db.imageretrieval.dataset;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import org.apache.commons.io.filefilter.FileFilterUtils;

import br.edu.ufu.comp.pos.db.imageretrieval.pojo.Image;

public class Dataset {
	private String datasetPath;

	public Dataset(String path) {
		this.datasetPath = path;
	}

	public void scanSifts(Consumer<double[]> c) throws FileNotFoundException, IOException {
		this.scan((image) -> image.scan(c) );
	}

	public List<Image> list() throws FileNotFoundException, IOException{
		List<Image> result = new ArrayList<Image>();
		this.scan((i) -> result.add(i));
		return result;
	}
	
	public void scan(Consumer<Image> c) throws FileNotFoundException, IOException {
		File[] sifts = listFiles("sift");
		File[] images = listFiles("jpg");
		Arrays.sort(sifts);

		System.out.println("Scanning DB:");
		for (int i = 0; i < sifts.length; i++) {
			c.accept(new Image(i, images[i], sifts[i]));
			System.out.println(((i + 1) / Double.valueOf(sifts.length)) * 100.0 + "%");
		}
		System.out.println("End scanning.");
	}

	protected File[] listFiles(String extension) {
		FileFilter suffixFileFilter = FileFilterUtils.suffixFileFilter("." + extension);
		File[] files = new File(datasetPath).listFiles(suffixFileFilter);
		return files;
	}
}
