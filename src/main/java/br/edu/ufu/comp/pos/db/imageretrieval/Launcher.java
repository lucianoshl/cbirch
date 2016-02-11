package br.edu.ufu.comp.pos.db.imageretrieval;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;

import br.edu.ufu.comp.pos.db.imageretrieval.clustering.birch.cftree.CFTree;
import br.edu.ufu.comp.pos.db.imageretrieval.dataset.Dataset;
import br.edu.ufu.comp.pos.db.imageretrieval.pojo.ImageHits;
import br.edu.ufu.comp.pos.db.imageretrieval.pojo.QueryResult;

public class Launcher {
	// -Xmx4096m
	// -javaagent:/home/lucianos/birch-experiment/src/main/resources/SizeOf.jar
	public static void main(String[] args) throws IOException {
		Dataset dataset = new Dataset(
				"/home/lucianos/birch-experiment/datasets/formated/oxford-full");

		CFTree tree = new CFTree(100, 0.5, 1, true);
		tree.setAutomaticRebuild(true);
		tree.setMemoryLimitGB(2);

		dataset.scanSifts((sift) -> tree.insertEntry(sift));
	
		tree.rebuildTree();
		tree.rebuildTree();
		
		dataset.scan((img) -> tree.index(img));

		List<QueryResult> result = new ArrayList<QueryResult>();

		dataset.scan((queryImage) -> {
			List<ImageHits> queryResult = tree.queryImage(queryImage);
			result.add(new QueryResult(queryImage, queryResult));
		});

		saveReport(result);
	}

	protected static void saveReport(List<QueryResult> result) throws IOException {
		Handlebars handlebars = new Handlebars();
		handlebars.setStartDelimiter("<%");
		handlebars.setEndDelimiter("%>");
		String baseTemplate = IOUtils
				.toString(Launcher.class.getClassLoader().getResource("templates/results.html"));
		Template template = handlebars.compileInline(baseTemplate);

		File outFile = new File("/tmp/birch.html");
		FileUtils.deleteQuietly(outFile);
		FileWriter writer = new FileWriter(outFile);
		writer.write(template.apply(result));
		writer.close();
	}

}
