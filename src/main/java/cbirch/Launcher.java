package cbirch;


import cbirch.cli.SystemParameters;
import cbirch.clustering.ClusteringMethod;
import cbirch.dataset.Dataset;
import cbirch.factory.ClusteringMethodFactory;
import cbirch.factory.DatasetFactory;
import cbirch.framework.Framework;
import de.tototec.cmdoption.CmdlineParser;


/**
 * Created by lucianos on 3/10/17.
 */
public class Launcher {

    private static void runner(SystemParameters config) {

        Dataset dataset = new DatasetFactory().create(config);
        ClusteringMethod clustering = new ClusteringMethodFactory().create(config);
        new Framework().run(dataset, clustering, config.rankSize);
    }

    public static void main(String[] args) {

        final SystemParameters config = new SystemParameters();
        final CmdlineParser cp = new CmdlineParser(config);
        cp.setProgramName("cbirch");

        cp.parse(args);

        config.validate();
        runner(config);

//        cp.usage();
//        System.exit(0);
    }

}
