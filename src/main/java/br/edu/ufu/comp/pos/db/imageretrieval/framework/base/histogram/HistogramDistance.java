package br.edu.ufu.comp.pos.db.imageretrieval.framework.base.histogram;

import lombok.Getter;

public class HistogramDistance {

    @Getter
    private double distance;
    @Getter
    private Histogram histogram;

    public HistogramDistance(Histogram histogram, double distance) {
	this.histogram = histogram;
	this.distance = distance;
    }

}
