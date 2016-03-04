package br.edu.ufu.comp.pos.db.imageretrieval.framework.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.edu.ufu.comp.pos.db.imageretrieval.dataset.image.OxfordImage;

public class Histograms {

    Map<OxfordImage, Histogram> content = new HashMap<OxfordImage, Histogram>();

    Map<Integer, Double> idfCache = new HashMap<Integer, Double>();

    public void add(Histogram histogram) {
	content.put(histogram.getImage(), histogram);

    }

    public List<Histogram> getSimilar(Histogram query, int k) {

	List<Histogram> result = new ArrayList<Histogram>(content.values());

	Histogram normalizedQuery = query.normalize(this);

	result.sort((b, a) -> {
	    double distA = normalizedQuery.distance(a.normalize(this));
	    double distB = normalizedQuery.distance(b.normalize(this));
	    return Double.compare(distA, distB);
	});

	return result.subList(0, k);
    }

    public static Histogram getHistogram(OxfordImage img, ClusterTree tree) {

	return Histogram.create(img,tree);
    }

    public double idf(int word) {
	Double value = this.idfCache.get(word);
	if (value == null) {
	    double n = 0;
	    Collection<Histogram> documents = content.values();
	    for (Histogram document : documents) {
		if (document.hasOcurrence(word)) {
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
