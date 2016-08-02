package br.edu.ufu.comp.pos.db.imageretrieval.framework.base.map;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;

@Builder
public class OxfordMapCalculator implements MapCalculator {

    List<String> ignore;
    List<String> positive;
    List<String> negative;

    public double calc(List<String> qualities) {
        qualities = new ArrayList<>(qualities);
        if (ignore != null){
            qualities.removeAll(ignore);
        }


        int positives = 0;
        int total = qualities.size();

        for (String quality : qualities) {
            if (positive.contains(quality)) {
                positives++;
            }
        }

        return positives / Double.valueOf(total);

    }

}
