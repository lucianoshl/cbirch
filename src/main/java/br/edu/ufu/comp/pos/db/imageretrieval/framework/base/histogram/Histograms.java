package br.edu.ufu.comp.pos.db.imageretrieval.framework.base.histogram;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.log4j.Logger;

import br.edu.ufu.comp.pos.db.imageretrieval.dataset.image.Image;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.ClusterTree;

public class Histograms {
    
    final static Logger logger = Logger.getLogger(Histograms.class);
    
    Map<Image, Histogram> content = new HashMap<Image, Histogram>();

    private double[] idf;

    private int wordsSize;

    public Histograms(int wordsSize) {
	this.wordsSize = wordsSize;
    }

    public void add(Histogram histogram) {
	content.put(histogram.getImage(), histogram);

    }

    public List<Histogram> getSimilar(Histogram query, int k) {

	logger.debug("Normalizing query");
	Histogram normalizedQuery = query.normalize(this);
	
	List<Histogram> result = new ArrayList<Histogram>();
	
	List<HistogramDistance> sortList = new ArrayList<HistogramDistance>();

	StopWatch stopWatch = new StopWatch();
	stopWatch.start();
	logger.debug("start sort by distance");
	for (Histogram histogram : content.values()) {
	    double distance = histogram.normalize(this).distance(normalizedQuery);
	    sortList.add(new HistogramDistance(histogram,distance));
	}
	
	sortList.sort((b,a) -> {
	    return Double.compare(a.getDistance(), b.getDistance());
	});

	logger.debug("end sort by distance " + stopWatch.getTime());

	sortList.subList(0, k).forEach((a) -> {
	    result.add(a.getHistogram());
	});
	
	return result.subList(0, k);
    }

    public static Histogram getHistogram(Image img, ClusterTree tree) {

	return Histogram.create(img,tree);
    }

    public double idf(int word) {
	if (idf == null) {
	    calcAllIdfs();
	}
	return idf[word];

    }

    private void calcAllIdfs() {
	idf = new double[this.wordsSize];
	
	Collection<Histogram> documents = content.values();
	int documentsSize = documents.size();
	for (Histogram document : documents) {
	    double[] histogram = document.getContent();
	    for (int i = 0; i < histogram.length; i++) {
		if (histogram[i] > 0){
			idf[i] += 1;
		}
	    }
	}

	for (int i = 0; i < idf.length; i++) {
	    // inverse document frequency
	    if (idf[i] != 0) {
		idf[i] = Math.log(Double.valueOf(documentsSize) / idf[i]);
	    }

	}

    }

    public double idf(List<List<String>> docs, String term) {
	double n = 0;
	for (List<String> doc : docs) {
	    for (String word : doc) {
		if (term.equalsIgnoreCase(word)) {
		    n++;
		    break;
		}
	    }
	}
	return Math.log(docs.size() / n);
    }

}
