package br.edu.ufu.comp.pos.db.imageretrieval.commons;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import br.edu.ufu.comp.pos.db.imageretrieval.clustering.birch.cftree.CFEntry;
import br.edu.ufu.comp.pos.db.imageretrieval.dataset.image.Image;

public class IndexedTree {

    private Map<CFEntry, Set<Image>> index = new HashMap<CFEntry, Set<Image>>();

    protected void putInIndex(CFEntry leaf, Image img) {

        Set<Image> set = index.get(leaf);
        if (set == null) {
            set = new HashSet<Image>();
            index.put(leaf, set);
        }
        set.add(img);
    }

    protected Set<Image> getImagesInLeaf(CFEntry leaf) {

        return this.index.get(leaf);
    }

}
