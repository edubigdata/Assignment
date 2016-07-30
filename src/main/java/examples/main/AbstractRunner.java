package examples.main;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;

/**
 * Class offers configuration capability to be used
 * by various jobs
 *
 * Created by Nicholas Lester on 14/07/2016.
 */
public abstract class AbstractRunner {

    protected CommandLineParser parser = new BasicParser();
    protected Options options = new Options();
    protected CommandLine commandLine;

    protected abstract void addOptions();
    protected abstract boolean doRun();

    protected AbstractRunner(){
       addOptions();
    }

    protected Option createOption(String name, String desc, int max, boolean required) {

        return OptionBuilder
                .withArgName(name)
                .hasArgs(max)
                .withDescription(desc)
                .isRequired(required)
                .create(name);
    }

    protected boolean run(String[] args) throws Exception{
        commandLine = parser.parse(options, args);
        return doRun();
    }

}
