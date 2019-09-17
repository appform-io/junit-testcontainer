package com.phonepe.testcontainer.demo;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.google.inject.Stage;
import com.phonepe.testcontainer.commons.ApplicationContext;
import com.phonepe.testcontainer.demo.config.AppConfiguration;
import com.phonepe.testcontainer.demo.module.AerospikeTestContainerModule;
import com.phonepe.testcontainer.demo.utils.SerDe;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import lombok.extern.slf4j.Slf4j;
import ru.vyarus.dropwizard.guice.GuiceBundle;

@Slf4j
public class TestContainerDemoTestApplication extends TestContainerDemoApplication {

    private GuiceBundle<AppConfiguration> guiceBundle;

    public static void main(String[] args) throws Exception {
        new TestContainerDemoTestApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<AppConfiguration> bootstrap) {

        this.guiceBundle = GuiceBundle.<AppConfiguration>builder()
                .modules(new AerospikeTestContainerModule())
                .enableAutoConfig(getClass().getPackage().getName())
                .build(Stage.PRODUCTION);
        // Add Guice Bundle
        bootstrap.addBundle(this.guiceBundle);
    }


    @Override
    public void run(AppConfiguration hermesConfiguration, Environment environment) {
        initializeSerDeserializer(environment);
        // set guice injector in application context
        ApplicationContext.setAlreadyCreatedInjector(this.guiceBundle.getInjector());
    }

    /**
     * Initializes Serializer-Deserializer with Object Mapper
     *
     * @param environment {@link Environment}
     */
    private void initializeSerDeserializer(Environment environment) {
        environment.getObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        environment.getObjectMapper().configure(MapperFeature.USE_BASE_TYPE_AS_DEFAULT_IMPL, true);

        SerDe.init(environment.getObjectMapper());
    }

    @Override
    public String getName() {
        return "testcontainer-app";
    }

}