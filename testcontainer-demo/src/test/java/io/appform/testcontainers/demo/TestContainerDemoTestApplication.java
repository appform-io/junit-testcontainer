package io.appform.testcontainers.demo;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.google.inject.Stage;
import io.appform.testcontainers.commons.ApplicationContext;
import io.appform.testcontainers.demo.config.DemoAppConfiguration;
import io.appform.testcontainers.demo.module.AerospikeTestContainerModule;
import io.appform.testcontainers.demo.utils.SerDe;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import lombok.extern.slf4j.Slf4j;
import ru.vyarus.dropwizard.guice.GuiceBundle;

@Slf4j
public class TestContainerDemoTestApplication extends TestContainerDemoApplication {

    private GuiceBundle<DemoAppConfiguration> guiceBundle;

    public static void main(String[] args) throws Exception {
        new TestContainerDemoTestApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<DemoAppConfiguration> bootstrap) {

        this.guiceBundle = GuiceBundle.<DemoAppConfiguration>builder()
                .modules(new AerospikeTestContainerModule())
                .enableAutoConfig(getClass().getPackage().getName())
                .build(Stage.PRODUCTION);
        // Add Guice Bundle
        bootstrap.addBundle(this.guiceBundle);
    }


    @Override
    public void run(DemoAppConfiguration hermesConfiguration, Environment environment) {
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