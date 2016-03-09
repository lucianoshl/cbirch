package br.edu.ufu.comp.pos.db.imageretrieval.framework.base;

import static java.util.Arrays.asList;

import org.junit.Test;

import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.map.MapCalculator;
import br.edu.ufu.comp.pos.db.imageretrieval.framework.base.map.OxfordMapCalculator;
import junit.framework.TestCase;;

public class MapCalculatorTest {

    MapCalculator calculator = OxfordMapCalculator.builder()//
	    .ignore(asList("junk"))//
	    .positive(asList("ok", "good"))//
	    .negative(asList("absent"))//
	    .build();

    @Test
    public void test1() {
	double result = calculator.calc(asList("good", "good", "ok", "absent", "junk"));
	TestCase.assertEquals(3.0 / 4.0, result);
    }
    
    @Test
    public void test2() {
	double result = calculator.calc(asList("good", "good" ));
	TestCase.assertEquals(2.0 / 2.0, result);
    }
    
    @Test
    public void test3() {
	double result = calculator.calc(asList("good", "good", "junk", "junk" ));
	TestCase.assertEquals(2.0 / 2.0, result);
    }

}
