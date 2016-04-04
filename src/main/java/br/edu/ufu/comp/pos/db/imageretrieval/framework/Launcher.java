package br.edu.ufu.comp.pos.db.imageretrieval.framework;

import java.io.IOException;

import org.apache.log4j.Logger;

import br.edu.ufu.comp.pos.db.imageretrieval.dataset.Dataset;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.ClusterTree;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.factory.DatasetFactory;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.factory.TreeFactory;

public class Launcher {

    final static Logger logger = Logger.getLogger(Launcher.class);

    public static void main(String[] args) throws IOException {

        if (args.length == 0) {
            throw new IllegalArgumentException("tree name is required");
        }

        Dataset dataset = new DatasetFactory().create(args);

        ClusterTree tree = new TreeFactory().create(args);

        Result.instance.elapsedTime("all", () -> {
            try {
                new Framework().run(dataset, tree, 4);
            } catch (Exception e) {
                e.printStackTrace();
                Result.instance.setError(e);
                logger.info("Error:" + e.getMessage());
                logger.error(e);
            } finally {
                logger.info(Result.instance.toString());
                Result.instance.save();
            }
        });

    }

}
