package br.edu.ufu.comp.pos.db.imageretrieval.dataset;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;

import br.edu.ufu.comp.pos.db.imageretrieval.commons.Utils;
import br.edu.ufu.comp.pos.db.imageretrieval.dataset.image.OxfordImage;

public class OxfordDataset extends Dataset {

    private File binaryFile;

    private File imageFolder;

    private File rangeSwiftInBinary;

    private File gtFilesFolder;

    private File datasetFolder;

    private File orderInBinaryFile;

    private Map<String, File> queryFiles;

    private Map<String, List<String>> queryClass;

    @Override
    public void trainSet(Consumer<OxfordImage> c) {

	this.scanAllImages(c);
    }

    public void testSet(String clazz, Consumer<OxfordImage> c) {

	this.scanAllImages((image) -> {
	    if (isQueryFile(clazz, image)) {
		c.accept(image);
	    }
	});
    }

    private boolean isQueryFile(String clazz, OxfordImage image) {

	boolean isQuery = this.queryFiles.keySet().contains(image.getImage().getName());
	boolean isFromClazz = this.queryClass.get(clazz).contains(image.getImage().getName());
	return isQuery && isFromClazz;
    }

    private void fillQueryFiles() {
	queryClass = new HashMap<String, List<String>>();

	for (String testClazz : this.getTestClasses()) {
	    queryClass.put(testClazz, new ArrayList<String>());
	}

	this.queryFiles = new HashMap<String, File>();
	File[] listFiles = this.gtFilesFolder.listFiles();
	for (File file : listFiles) {
	    String fileName = file.getName();
	    if (fileName.contains("query.txt")) {
		String queryFile = Utils.readFileToString(file).split(" ")[0].replace("oxc1_", "") + ".jpg";
		String classFile = fileName.split("_\\d+")[0];
		queryFiles.put(queryFile, file);
		queryClass.get(classFile).add(queryFile);
	    }
	}
    }

    protected void scanAllImages(Consumer<OxfordImage> c) {

	Map<String, Long> aux = new HashMap<String, Long>();
	aux.put("aux", 0l);
	scanOrderFile((fileName) -> {
	    long siftSize = getImageSiftSize(fileName);
	    c.accept(new OxfordImage(binaryFile, new File(imageFolder, fileName), aux.get("aux"), siftSize));
	    aux.put("aux", aux.get("aux") + siftSize * 128);
	});

    }

    private long getImageSiftSize(String fileName) {

	File siftSizeFile = new File(rangeSwiftInBinary, "oxc1_" + fileName.replace(".jpg", ".txt"));
	Scanner createScanner = Utils.createScanner(siftSizeFile);
	createScanner.nextLine();
	long result = createScanner.nextLong();
	createScanner.close();
	return result;
    }

    private void scanOrderFile(Consumer<String> c) {

	Scanner scanner = Utils.createScanner(orderInBinaryFile);
	int i = 0;
	while (scanner.hasNextLine()) {
	    String fileName = scanner.nextLine().replace("oxc1_", "") + ".jpg";
	    c.accept(fileName);
	    i++;
//	    if (i == 13){
//		break;
//	    }
	}
	scanner.close();
    }

    public static OxfordDataset createFromBase(String workspace, String datasetName) {

	return new OxfordDataset(workspace, datasetName, "feat_oxc1_hesaff_sift.bin", "word_oxc1_hesaff_sift_16M_1M",
		"images", "gt_files", "README2.txt");
    }

    public OxfordDataset(String workspace, String datasetName, String binaryFile, String siftSizeFolderDescriptor,
	    String imagesFolderPath, String gtFiles, String orderInBinaryFile) {

	// this.workspace = workspace;
	this.datasetFolder = Utils.getDatesetPath(workspace, datasetName);
	this.binaryFile = new File(datasetFolder, binaryFile);
	this.rangeSwiftInBinary = new File(datasetFolder, siftSizeFolderDescriptor);
	this.imageFolder = new File(datasetFolder, imagesFolderPath);
	this.gtFilesFolder = new File(datasetFolder, gtFiles);
	this.orderInBinaryFile = new File(this.datasetFolder, orderInBinaryFile);
	this.fillQueryFiles();

    }

    @Override
    public String quality(OxfordImage query, String imgName) {
	File queryFile = this.queryFiles.get(query.getImage().getName());

	String result = "null";

	result = checkIs(imgName, queryFile, "good", result);
	result = checkIs(imgName, queryFile, "junk", result);
	result = checkIs(imgName, queryFile, "ok", result);

	return result;
    }

    private String checkIs(String imgName, File queryFile, String quality, String result) {
	List<String> r = Utils.readLines(queryFile.getAbsolutePath().replace("query.txt", quality + ".txt"));
	if (r.contains(imgName.replaceAll(".jpg", ""))) {
	    return quality;
	}
	return result;
    }

    @Override
    public String[] getTestClasses() {

	File[] files = this.gtFilesFolder.listFiles();
	HashSet<String> uniq = new HashSet<String>();
	for (File file : files) {
	    uniq.add(file.getName().split("_\\d")[0]);
	}
	String[] result = uniq.toArray(new String[uniq.size()]);
	Arrays.sort(result);
	return result;
    }

}
