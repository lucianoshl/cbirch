package br.edu.ufu.comp.pos.db.imageretrieval.dataset;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;

public class Oxford {
	private File binaryFile;
	private File imageFolder;
	private File outputFolder;
	private File rangeSwiftInBinary;
	private File orderInBinaryFile;

	public static void main(String[] args) throws IOException {

		Oxford dataset = new Oxford(
				"/home/lucianos/birch-experiment/datasets/raw/oxford/feat_oxc1_hesaff_sift.bin",
				"/home/lucianos/birch-experiment/datasets/raw/oxford/word_oxc1_hesaff_sift_16M_1M",
				"/home/lucianos/birch-experiment/datasets/raw/oxford/images",
				"/home/lucianos/birch-experiment/datasets/raw/oxford/README2.txt",
				"/home/lucianos/birch-experiment/datasets/formated/oxford");

		dataset.process();

	}

	public Oxford(String binaryFile, String rangeSwiftInBinary, String imagesFolderPath,
			String orderInBinaryFile, String outputFolderPath) {
		this.binaryFile = new File(binaryFile);
		this.rangeSwiftInBinary = new File(rangeSwiftInBinary);
		this.imageFolder = new File(imagesFolderPath);
		this.orderInBinaryFile = new File(orderInBinaryFile);
		this.outputFolder = new File(outputFolderPath);
	}

	public void process() throws IOException {

		FileInputStream binFileReader = new FileInputStream(binaryFile);

		Scanner fileOrder = new Scanner(orderInBinaryFile);
		int totalSift = 0; 
		while (fileOrder.hasNext()) {
			String element = fileOrder.next();
			String imageName = element.replace("oxc1_", "");
			String imageFileName = imageName + ".jpg";
			String siftMetadata = element + ".txt";
			String siftFileName = imageName + ".sift";
			Integer swiftsTotal = readSwiftsNumber(new File(rangeSwiftInBinary, siftMetadata));
			totalSift += swiftsTotal;
			byte[] buffer = new byte[swiftsTotal * 128];
			binFileReader.read(buffer);

			File imgOutput = new File(outputFolder, imageFileName);
			File imgOrigin = new File(imageFolder, imageFileName);
			FileUtils.copyFile(imgOrigin, imgOutput);

			File siftFile = new File(outputFolder, siftFileName);
			FileUtils.writeByteArrayToFile(siftFile, buffer);

		}
		FileUtils.write(new File(outputFolder,"total"), String.valueOf(totalSift));
		fileOrder.close();
		binFileReader.close();

	}

	private Integer readSwiftsNumber(File file) {

		Scanner fileScanner;
		try {
			fileScanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			throw new IllegalStateException(e);
		}
		fileScanner.next();

		Integer number = Integer.valueOf(fileScanner.next());
		fileScanner.close();
		return number;
	}
}
