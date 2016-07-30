package examples.configuration;

/**
 * Created by deokishore on 15/12/2015.
 */

import com.edubigdata.examples.deo.AssignmentImpl;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;
import java.util.Set;

public class DependencyInjectionModule extends AbstractModule {

    Logger log = LoggerFactory.getLogger(DependencyInjectionModule.class);

    protected String applicationProperties;

    private String xlsxFilePath;

    /**
     * Method constructs a path to an application.properties file
     * based on the supplied environment.
     * @param environment
     * @return
     */
    private String getApplicationProperties(String environment){
        StringBuilder path = new StringBuilder();
        path.append("/").append(environment).append(".application.properties");
        return path.toString();
    }


    // Potential candiate for deletion as this is replaced by overloaded method below
    public DependencyInjectionModule(String xlsxFilePath) {
        this.xlsxFilePath = xlsxFilePath;
    }

    public DependencyInjectionModule(String xlsxFilePath, String environment) {
        applicationProperties=getApplicationProperties(environment);
        this.xlsxFilePath = xlsxFilePath;
    }

    @Override
    protected void configure() {
        log.info(" Configuring dependency injection ");
        loadApplicationProperties();
        log.info(" Dependency injection was successful ");
    }

    private void loadApplicationProperties() {
        Properties props = null;
        try {
            props = loadProperties(applicationProperties);
            if(xlsxFilePath != null){
                Set<Object> keys = getAllKeys(props);
                for(Object k:keys){
                    String key = (String)k;
                    if(key.equals("xml.xpath.rule.xlsxfile")){
                        props.put(key, xlsxFilePath);
                    }
                }
            }
        } catch (IOException ex) {
            log.error(" Error while loading property file : ", ex);
        }
        Names.bindProperties(binder(), props);
        bindClassses();
    }

    private void bindClassses() {
        log.info(" Binding classes ");
        bind(SparkContext.class);
        bind(com.edubigdata.examples.deo.AssignmentImpl.class);
        bind(com.edubigdata.examples.rajesh.AssignmentImpl.class);
        bind(com.edubigdata.examples.daya.AssignmentImpl.class);
        bind(com.edubigdata.examples.tarun.AssignmentImpl.class);
        bind(com.edubigdata.examples.akash.AssignmentImpl.class);
        log.info(" Binding was successful ");
    }

    public Set<Object> getAllKeys(Properties prop){
        Set<Object> keys = prop.keySet();
        return keys;
    }

    public String getPropertyValue(Properties prop, String key){
        return prop.getProperty(key);
    }

    private Properties loadProperties(String propertyFile) throws IOException {
        Properties properties = new Properties();
        properties.load(getClass().getResourceAsStream(propertyFile));
        return properties;
    }

}
