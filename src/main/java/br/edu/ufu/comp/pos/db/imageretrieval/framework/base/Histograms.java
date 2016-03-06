package br.edu.ufu.comp.pos.db.imageretrieval.framework.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.log4j.Logger;

import br.edu.ufu.comp.pos.db.imageretrieval.dataset.image.Image;

public class Histograms {
    
    final static Logger logger = Logger.getLogger(Histograms.class);
    
    Map<Image, Histogram> content = new HashMap<Image, Histogram>();

    Map<Integer, Double> idfCache = new HashMap<Integer, Double>();

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


//	stopWatch.start();
//	result.sort((b, a) -> {
//	    double distA = normalizedQuery.distance(a.normalize(this));
//	    double distB = normalizedQuery.distance(b.normalize(this));
//	    return Double.compare(distA, distB);
//	});

	sortList.subList(0, k).forEach((a) -> {
	    result.add(a.getHistogram());
	});
	
	return result.subList(0, k);
    }

    public static Histogram getHistogram(Image img, ClusterTree tree) {

	return Histogram.create(img,tree);
    }

    public double idf(int word) {
	Double value = this.idfCache.get(word);
	if (value == null) {
	    double n = 0;
	    Collection<Histogram> documents = content.values();
	    for (Histogram document : documents) {
		
		if (document.getContent()[word] > 0.0) {
		    n++;
		}
	    }
	    if (n == 0) {
		System.out.println("N = ZERO");
		System.exit(1);
	    }
	    value = Math.log(documents.size() / n);
	    this.idfCache.put(word, value);
	}

	return value;
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
