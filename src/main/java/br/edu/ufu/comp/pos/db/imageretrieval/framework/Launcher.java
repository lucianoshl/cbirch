package br.edu.ufu.comp.pos.db.imageretrieval.framework;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.edu.ufu.comp.pos.db.imageretrieval.dataset.Dataset;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.ClusterTree;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.factory.DatasetFactory;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.factory.TreeFactory;

public class Launcher {

    final static Logger logger = LoggerFactory.getLogger(Launcher.class);

    public static void main(String[] args) throws IOException {
        exec(args);
    }

    public static void exec(String[] args) throws IOException {

        Result result = new Result();
        try {
            callExperiment(args);
        } catch (Exception e) {
            result.setError(e);
        } finally {
            result.save();
        }

    }

    private static void callExperiment(String[] args) {

        if (args.length == 0) {
            throw new IllegalArgumentException("tree name is required");
        }

        Dataset dataset = new DatasetFactory().create(args);

        ClusterTree tree = new TreeFactory().create(args);

        int k = 4;

        new Framework().run(dataset, tree, k).save();
    }

}
