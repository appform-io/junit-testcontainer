package com.phonepe.testcontainer.commons;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;

import java.util.*;
import java.util.function.Consumer;

public class ApplicationContext {

    private ApplicationContext() {
    }

    public static final ApplicationContext APPLICATION_CONTEXT = new ApplicationContext();

    private static Map<Class<?>, Object> classMapping = new HashMap<>();
    private static List<AbstractModule> hermesModules = new ArrayList<>();
    private static Injector injector;

    public static void registerModule(AbstractModule module) {
        hermesModules.add(module);
    }

    public static void setup(AbstractModule... modules) {
        hermesModules.addAll(Arrays.asList(modules));
        injector = Guice.createInjector(hermesModules);
    }

    public static void setAlreadyCreatedInjector(Injector injector) {
        ApplicationContext.injector = injector;
    }

    public static void reset() {
        hermesModules.clear();
        injector = null;
        classMapping.clear();
    }

    /***
     * Add some more object in the application context to use.
     */
    public static void set(Class<?> cls, Object object) {
        classMapping.put(cls, object);
    }

    /**
     * Helper to get instance of class
     */
    @SuppressWarnings("unchecked")
    public static <T> T get(Class<T> cls) {
        if (classMapping.containsKey(cls)) {
            return (T) classMapping.get(cls);
        }
        return injector.getInstance(cls);
    }


    /**
     * Helper to get named instance of class
     */
    @SuppressWarnings("unchecked")
    public static <T> T get(Class<T> cls, String annotationName) {
        if (classMapping.containsKey(cls)) {
            return (T) classMapping.get(cls);
        }
        return injector.getInstance(Key.get(cls, Names.named(annotationName)));
    }

    /**
     * Helper to get instance of class
     */
    @SuppressWarnings("unchecked")
    private static <T> Optional<T> getOptionalFromGuice(Class<T> cls) {
        if (classMapping.containsKey(cls)) {
            return Optional.of((T) classMapping.get(cls));
        } else if (injector == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(injector.getInstance(cls));
    }

    /**
     * Helper method to execute when object is available
     */
    public static <T> void executeOnOptionalFromGuice(Class<T> cls, Consumer<T> function) {
        getOptionalFromGuice(cls).ifPresent(function);
    }
}
