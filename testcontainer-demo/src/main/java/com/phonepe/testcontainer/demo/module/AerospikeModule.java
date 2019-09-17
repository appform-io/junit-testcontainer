
package com.phonepe.testcontainer.demo.module;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.Host;
import com.aerospike.client.policy.ClientPolicy;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.phonepe.testcontainer.demo.config.AerospikeConfiguration;
import com.phonepe.testcontainer.demo.config.DemoAppConfiguration;

import java.util.List;

import static com.phonepe.testcontainer.demo.utils.AerospikeUtils.clientPolicy;
import static com.phonepe.testcontainer.demo.utils.AerospikeUtils.hosts;


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