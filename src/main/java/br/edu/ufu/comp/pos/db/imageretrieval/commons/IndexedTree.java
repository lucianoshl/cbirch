package br.edu.ufu.comp.pos.db.imageretrieval.commons;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import br.edu.ufu.comp.pos.db.imageretrieval.clustering.birch.cftree.CFEntry;
import br.edu.ufu.comp.pos.db.imageretrieval.dataset.image.OxfordImage;

public class IndexedTree {

    private Map<CFEntry, Set<OxfordImage>> index = new HashMap<CFEntry, Set<OxfordImage>>();

    protected void putInIndex(CFEntry leaf, OxfordImage img) {

	Set<OxfordImage> set = index.get(leaf);
	if (set == null) {
	    set = new HashSet<OxfordImage>();
	    index.put(leaf, set);
	}
	set.add(img);
    }

    protected Set<OxfordImage> getImagesInLeaf(CFEntry leaf) {

	return this.index.get(leaf);
    }

}
