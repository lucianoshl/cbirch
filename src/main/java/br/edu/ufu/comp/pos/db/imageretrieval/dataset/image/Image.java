package br.edu.ufu.comp.pos.db.imageretrieval.dataset.image;

import java.io.File;
import java.util.function.Consumer;

import br.edu.ufu.comp.pos.db.imageretrieval.framework.QueryResultItem;

public abstract class Image {

    public abstract File getImage();

    public abstract void scan(Consumer<double[]> c);

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getImage() == null) ? 0 : getImage().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Image other = (Image) obj;
        if (getImage() == null) {
            if (other.getImage() != null)
                return false;
        } else if (!getImage().equals(other.getImage()))
            return false;
        return true;
    }

}
