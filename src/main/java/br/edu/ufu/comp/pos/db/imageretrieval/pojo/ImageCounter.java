package br.edu.ufu.comp.pos.db.imageretrieval.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.edu.ufu.comp.pos.db.imageretrieval.dataset.image.OxfordImage;

public class ImageCounter {

    Map<OxfordImage, Integer> counter = new HashMap<OxfordImage, Integer>();

    public void count(Set<OxfordImage> set) {

	for (OxfordImage image : set) {
	    count(image);
	}
    }

    protected void count(OxfordImage image) {

	Integer ocurrences = counter.get(image);
	if (ocurrences == null) {
	    ocurrences = 0;
	    counter.put(image, ocurrences);
	}
	counter.put(image, ocurrences + 1);
    }

    public List<ImageHits> rank() {

	List<ImageHits> result = new ArrayList<ImageHits>();

	Set<OxfordImage> keys = counter.keySet();
	for (OxfordImage image : keys) {
	    result.add(new ImageHits(image, counter.get(image)));
	}
	result.sort((ImageHits a, ImageHits b) -> b.getHits().compareTo(a.getHits()));
	return result;
    }

}
