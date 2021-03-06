
package io.appform.testcontainers.demo.module;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.Host;
import com.aerospike.client.policy.ClientPolicy;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import io.appform.testcontainers.demo.config.AerospikeConfiguration;
import io.appform.testcontainers.demo.config.DemoAppConfiguration;

import java.util.List;

import static io.appform.testcontainers.demo.utils.AerospikeUtils.clientPolicy;
import static io.appform.testcontainers.demo.utils.AerospikeUtils.hosts;


public class AerospikeModule extends AbstractModule {

    @Override
    protected void configure() {
        // Do nothing
    }

    @Singleton
    @Provides
    public AerospikeConfiguration provideAerospikeConfig(final DemoAppConfiguration config) {
        return config.getAerospikeConfiguration();
    }

    @Singleton
    @Provides
    public AerospikeClient provideAerospikeClient(final DemoAppConfiguration config) {
        AerospikeConfiguration aerospikeConfig = config.getAerospikeConfiguration();
        List<Host> aerospikeHosts = hosts(aerospikeConfig.getConnection());
        ClientPolicy clientPolicy = clientPolicy(aerospikeConfig);
        return new AerospikeClient(clientPolicy, aerospikeHosts.toArray(new Host[0]));
    }
}