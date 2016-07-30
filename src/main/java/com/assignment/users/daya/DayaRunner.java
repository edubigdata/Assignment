package com.assignment.users.daya;

import com.assignment.configuration.ClazzFactory;
import com.assignment.main.AbstractRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main entry point for. This can be executed locally with
 * file:/// paths, on hdfs with hdfs:// paths
 * and executed in the target environment.
 * Created by Deo Kishore on 29/07/2016.
 */
public class DayaRunner extends AbstractRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(DayaRunner.class);

    public static void main(String[] args) throws Exception {
        DayaRunner runner = new DayaRunner();
        runner.run(args);
    }

    protected void addOptions() {
        options.addOption(createOption("input", "Path to  Input File,", 1, true))
                .addOption(createOption("output", "Path to  Output File,", 1, true))
                .addOption(createOption("environment", "Specify the runtime environment,", 1, true));
    }

    protected boolean doRun(){
        String input = commandLine.getOptionValue("input");
        String output = commandLine.getOptionValue("output");
        String environment = commandLine.getOptionValue("environment");

        LOGGER.info("Processing input path: {} to output path: {}", input, output);

        AssignmentImpl assignment =
                ClazzFactory.getInstance(AssignmentImpl.class, environment);

        LOGGER.info("Main Started.... ");

        try{
            assignment.wordWount(input, output, "");
            assignment.wordSearch(input, output, "");
        }
        catch(Exception ex){
            LOGGER.error(" Generation was not successful : ", ex );
            throw new RuntimeException(ex);
        }
        return false;
    }
}
