package br.edu.ufu.comp.pos.db.imageretrieval.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ImageCounter {

	Map<Image, Integer> counter = new HashMap<Image, Integer>();

	public void count(Set<Image> set) {
		for (Image image : set) {
			count(image);
		}
	}

	protected void count(Image image) {
		Integer ocurrences = counter.get(image);
		if (ocurrences == null) {
			ocurrences = 0;
			counter.put(image, ocurrences);
		}
		counter.put(image, ocurrences + 1);
	}

	public List<ImageHits> rank() {

		List<ImageHits> result = new ArrayList<ImageHits>();

		Set<Image> keys = counter.keySet();
		for (Image image : keys) {
			result.add(new ImageHits(image, counter.get(image)));
		}
		result.sort((ImageHits a, ImageHits b) -> b.getHits().compareTo(a.getHits()));
		return result;
	}

}
