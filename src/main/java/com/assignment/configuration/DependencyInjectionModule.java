package com.assignment.configuration;

/**
 * Created by deokishore on 15/12/2015.
 */


import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

public class DependencyInjectionModule extends AbstractModule {

    Logger log = LoggerFactory.getLogger(DependencyInjectionModule.class);

    protected String applicationProperties;


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

    public DependencyInjectionModule(String xlsxFilePath, String environment) {
        applicationProperties=getApplicationProperties(environment);
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
        } catch (IOException ex) {
            log.error(" Error while loading property file : ", ex);
        }
        Names.bindProperties(binder(), props);
        bindClassses();
    }

    private void bindClassses() {
        log.info(" Binding classes ");
        bind(SparkContext.class);
        bind(com.assignment.users.deo.AssignmentImpl.class);
        bind(com.assignment.users.rajesh.AssignmentImpl.class);
        bind(com.assignment.users.daya.AssignmentImpl.class);
        bind(com.assignment.users.tarun.AssignmentImpl.class);
        bind(com.assignment.users.akash.AssignmentImpl.class);
        log.info(" Binding was successful ");
    }
    private Properties loadProperties(String propertyFile) throws IOException {
        Properties properties = new Properties();
        properties.load(getClass().getResourceAsStream(propertyFile));
        return properties;
    }

}
