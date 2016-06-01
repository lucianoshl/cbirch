package br.edu.ufu.comp.pos.db.imageretrieval.framework.base;


import java.io.File;
import java.util.function.Consumer;

import org.junit.Test;

import br.edu.ufu.comp.pos.db.imageretrieval.dataset.image.Image;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.histogram.Histogram;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.histogram.Histograms;


public class HistogramsTest {

    @Test
    public void testing() {

        Histograms histograms = new Histograms( 6 );
        histograms.add( new Histogram( createFakeImage( "1" ), new double[] { 0, 0, 1, 0, 1, 1 } ) );
        histograms.add( new Histogram( createFakeImage( "2" ), new double[] { 0, 0, 1, 1, 0, 1 } ) );
        histograms.add( new Histogram( createFakeImage( "3" ), new double[] { 1, 1, 0, 0, 1, 0 } ) );

        // TestCase.assertEquals(1.0986122886681098, histograms.idf(0));
        // TestCase.assertEquals(1.0986122886681098, histograms.idf(1));
        // TestCase.assertEquals(0.4054651081081644, histograms.idf(2));
        // TestCase.assertEquals(1.0986122886681098, histograms.idf(3));
        // TestCase.assertEquals(0.4054651081081644, histograms.idf(4));
        // TestCase.assertEquals(0.4054651081081644, histograms.idf(5));

    }


    private Image createFakeImage( String path ) {

        return new Image( null, null, 0, 0, null ) {

            @Override
            public File getImage() {

                return new File( path );
            };


            @Override
            public void scan( Consumer< double[] > c ) {

                throw new UnsupportedOperationException();

            }
        };
    }
}
