package examples.configuration;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.apache.spark.SparkConf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Models the configuration required for Spark.
 * Settings are defined by the relevant application.properties
 * file specific to the environment and are injected
 * as @Named bindings
 *
 * Created by Nicholas Lester on 14/07/2016
 */
public class SparkConfig
{

    Logger log = LoggerFactory.getLogger(SparkConfig.class);

    protected static String master;
    protected static String appName;
    protected static String executorMemory;
    protected static String defaultParallelism;
    protected static String coresMax;

    @Inject
    public SparkConfig(@Named("spark.master") String master,
                       @Named("spark.conf.app.name") String appName,
                       @Named("spark.conf.executor.memory") String executorMemory,
                       @Named("spark.conf.cores.max") String coresMax,
                       @Named("spark.conf.default.parallelism") String defaultParallelism){

        this.master = master;
        this.appName = appName;
        this.executorMemory=executorMemory;
        this.defaultParallelism = defaultParallelism;
        this.coresMax=coresMax;

    }

    /**
     * Obtains an instance of SparkConf for the
     * specified environment in use
     * @return
     */
    public SparkConf getSparkConf() {

        if(master == null) {
            master = "local";
        }

        log.info("Creating SparkContext.  Master: {} AppName: {} ExecutorMemory: {} DefaultParallelism: {}",
                master, appName, executorMemory, defaultParallelism);

       return new SparkConf().setAppName(appName)
                .setMaster(master)
                .set("spark.executor.memory", executorMemory)
                .set("spark.cores.max", coresMax)
                .set("spark.default.parallelism", defaultParallelism);
    }
}
