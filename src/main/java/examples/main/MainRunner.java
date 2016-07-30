package examples.main;

import com.edubigdata.examples.configuration.ClazzFactory;
import com.edubigdata.examples.rajesh.AssignmentImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main entry point for. This can be executed locally with
 * file:/// paths, on hdfs with hdfs:// paths
 * and executed in the target environment.
 * Created by Deo Kishore on 29/07/2016.
 */
public class MainRunner extends AbstractRunner{

    Logger log = LoggerFactory.getLogger(MainRunner.class);

    public static void main(String[] args) throws Exception {
        MainRunner runner = new MainRunner();
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

        log.info("Processing input path: {} to output path: {}", input, output);

        AssignmentImpl rhsRDFGeneratorController =
                ClazzFactory.getInstance(AssignmentImpl.class, environment);

        log.info("Main Started.... ");

        try{
            rhsRDFGeneratorController.wordWount(input, output, "");
            rhsRDFGeneratorController.wordSearch(input, output, "");
        }
        catch(Exception ex){
            log.error(" RDF generation was not successful : ", ex );
            throw new RuntimeException(ex);
        }
        return false;
    }
}
