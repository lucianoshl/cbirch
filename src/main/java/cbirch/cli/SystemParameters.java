package cbirch.cli;


import de.tototec.cmdoption.CmdOption;


/**
 * Created by lucianos on 3/10/17.
 */
public class SystemParameters {

    @CmdOption( names = { "--help", "-h" }, description = "Show this help", isHelp = true )
    public boolean help;

    @CmdOption( names = { "--dataset", "-ds" }, args = { "name" }, description = "Dataset" )
    public String dataset = "short-leeds-butterfly";

    @CmdOption( names = { "--clustering", "-c" }, args = { "name" }, description = "Clustering method" )
    public String clustering = "birch";

    @CmdOption( names = { "--branching-factor", "-cf" }, args = { "value" }, description = "Branching Factor used in trees" )
    public Integer branchingFactor = 100;

    @CmdOption( names = { "--rank-size", "-rs" }, args = { "value" }, description = "Results in framework list" )
    public Integer rankSize = 4;


    public void validate() {

    }
}
