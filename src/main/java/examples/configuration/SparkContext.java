package examples.configuration;

import com.google.inject.Inject;
import org.apache.spark.api.java.JavaSparkContext;

import java.io.Serializable;

/**
 * Created by deokishore on 15/12/2015.
 */
public class SparkContext implements Serializable{

    private static SparkConfig sparkConfig;

    private static JavaSparkContext javaSparkContext;

    @Inject
    public void setSparkConfig(SparkConfig sparkConfig){
        this.sparkConfig = sparkConfig;
    }

    public JavaSparkContext getJavaSparkContext(){
        if(javaSparkContext == null) {
            javaSparkContext = new JavaSparkContext(sparkConfig.getSparkConf());
        }
        return javaSparkContext;
    }
}
