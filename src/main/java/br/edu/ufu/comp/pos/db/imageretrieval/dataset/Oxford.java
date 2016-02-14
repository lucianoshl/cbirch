package br.edu.ufu.comp.pos.db.imageretrieval.dataset;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.function.Consumer;

import org.apache.commons.io.FileUtils;

import br.edu.ufu.comp.pos.db.imageretrieval.pojo.Image;

public class Oxford extends Dataset {
	private File binaryFile;
	private File imageFolder;
	private File outputFolder;
	private File rangeSwiftInBinary;
	private File orderInBinaryFile;

	public static void main(String[] args) throws IOException {

		createFromBase("/home/void/birch-experiment/datasets").process();

	}

	private static Oxford createFromBase(String projectBase) {
		return new Oxford( //
				projectBase + "/raw/oxford/feat_oxc1_hesaff_sift.bin", //
				projectBase + "/raw/oxford/word_oxc1_hesaff_sift_16M_1M", //
				projectBase + "/raw/oxford/images", //
				projectBase + "/raw/oxford/README2.txt", //
				projectBase + "/formated/oxford");
	}

	public Oxford(String binaryFile, String siftSizeFolderDescriptor, String imagesFolderPath, String orderInBinaryFile,
			String outputFolderPath) {
		super(null,null);
		this.binaryFile = new File(binaryFile);
		this.rangeSwiftInBinary = new File(siftSizeFolderDescriptor);
		this.imageFolder = new File(imagesFolderPath);
		this.orderInBinaryFile = new File(orderInBinaryFile);
		this.outputFolder = new File(outputFolderPath);
	}

	@Override
	public void scan(Consumer<Image> c) {
		try {
			FileInputStream binFileReader = new FileInputStream(binaryFile);

			Scanner fileOrder = new Scanner(orderInBinaryFile);
			int id = 0;
			while (fileOrder.hasNext()) {
				String orderElement = fileOrder.next();
				String imageName = orderElement.replace("oxc1_", "");
				String imageFileName = imageName + ".jpg";
				byte[] buffer = readFromBinary(binFileReader, orderElement + ".txt");
				File imgOrigin = new File(imageFolder, imageFileName);
				File siftTmpFile = File.createTempFile(imageFileName, ".sift");
				FileUtils.writeByteArrayToFile(siftTmpFile, buffer);

				c.accept(new Image(id, imgOrigin, siftTmpFile));
				id++;
			}
			fileOrder.close();
			binFileReader.close();
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}

	}

	private byte[] readFromBinary(FileInputStream binFileReader, String siftMetadataFilePath) throws IOException {
		Integer swiftsTotal = readSwiftsNumber(new File(rangeSwiftInBinary, siftMetadataFilePath));
		byte[] buffer = new byte[swiftsTotal * 128];
		binFileReader.read(buffer);
		return buffer;
	}

	public void process() throws IOException {

		this.scan((img -> {

			String imageFileName = String.format("%05d", img.getId()) + "_" + img.getImage().getName();
			String siftFileName = imageFileName.replace(".jpg", "") + ".sift";

			File imgOutput = new File(outputFolder, imageFileName);
			File siftOutput = new File(outputFolder, siftFileName);

			try {
				FileUtils.copyFile(img.getImage(), imgOutput);
				FileUtils.copyFile(img.getSift(), siftOutput);
			} catch (Exception e) {
				throw new IllegalStateException(e);
			}

		}));

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
