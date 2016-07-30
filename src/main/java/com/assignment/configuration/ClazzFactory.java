package com.assignment.configuration;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Created by deokishore on 15/12/2015.
 */
public final class ClazzFactory<T> {

    public static <T> T getInstance(Class<T> theClass, String environment) {
        Injector injector = Guice.createInjector(new DependencyInjectionModule(null, environment));
        return injector.getInstance(theClass);
    }
}
