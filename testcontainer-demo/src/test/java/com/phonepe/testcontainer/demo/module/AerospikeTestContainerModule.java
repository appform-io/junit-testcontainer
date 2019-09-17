package com.phonepe.testcontainer.demo.module;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.Host;
import com.aerospike.client.policy.ClientPolicy;
import com.github.dockerjava.api.model.Capability;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.phonepe.testcontainer.aerospike.AerospikeContainerConfiguration;
import com.phonepe.testcontainer.aerospike.AerospikeWaitStrategy;
import com.phonepe.testcontainer.demo.config.AerospikeConfiguration;
import com.phonepe.testcontainer.demo.config.AerospikeHost;
import com.phonepe.testcontainer.demo.config.DemoAppConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.HostPortWaitStrategy;
import org.testcontainers.containers.wait.strategy.WaitAllStrategy;
import org.testcontainers.containers.wait.strategy.WaitStrategy;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

import static com.phonepe.testcontainer.commons.ContainerUtils.containerLogsConsumer;
import static com.phonepe.testcontainer.demo.utils.AerospikeUtils.clientPolicy;
import static com.phonepe.testcontainer.demo.utils.AerospikeUtils.hosts;
import static java.time.temporal.ChronoUnit.SECONDS;

@Slf4j
public class AerospikeTestContainerModule extends AbstractModule {

    @Override
    protected void configure() {

    }

    @Provides
    @Singleton
    public AerospikeWaitStrategy aerospikeWaitStrategy(
            AerospikeContainerConfiguration aerospikeContainerConfig) {
        return new AerospikeWaitStrategy(aerospikeContainerConfig);
    }

    @Provides
    @Singleton
    public AerospikeContainerConfiguration aerospikeContainerConfiguration() {
        return new AerospikeContainerConfiguration();
    }

    @Provides
    @Singleton
    public GenericContainer aerospike(AerospikeWaitStrategy aerospikeWaitStrategy,
                                      AerospikeContainerConfiguration aerospikeContainerConfig) {
        log.info("Starting aerospike server. Docker image: {}",
                aerospikeContainerConfig.getDockerImage());
        WaitStrategy waitStrategy = new WaitAllStrategy()
                .withStrategy(aerospikeWaitStrategy)
                .withStrategy(new HostPortWaitStrategy())
                .withStartupTimeout(Duration.of(300, SECONDS));

        GenericContainer aerospike =
                new GenericContainer<>(aerospikeContainerConfig.getDockerImage())
                        .withExposedPorts(aerospikeContainerConfig.getPort())
                        .withLogConsumer(containerLogsConsumer(log))
                        // see https://github.com/aerospike/aerospike-server.docker/blob/master/aerospike.template.conf
                        .withEnv("NAMESPACE", aerospikeContainerConfig.getNamespace())
                        .withEnv("SERVICE_PORT", String.valueOf(aerospikeContainerConfig.getPort()))
                        .withEnv("MEM_GB", String.valueOf(1))
                        .withEnv("STORAGE_GB", String.valueOf(1))
                        .withCreateContainerCmdModifier(cmd -> cmd.withCapAdd(Capability.NET_ADMIN))
                        .waitingFor(waitStrategy)
                        .withStartupTimeout(aerospikeContainerConfig.getTimeoutDuration());

        aerospike.start();
        return aerospike;
    }

    @Singleton
    @Provides
    public AerospikeClient provideAerospikeClient(final GenericContainer aerospikeContainer,
                                                  final AerospikeContainerConfiguration aerospikeContainerConfig,
                                                  final DemoAppConfiguration config) {
        Integer mappedPort = aerospikeContainer.getMappedPort(aerospikeContainerConfig.getPort());
        String host = aerospikeContainer.getContainerIpAddress();
        AerospikeConfiguration aerospikeConfig = config.getAerospikeConfiguration();
        List<Host> aerospikeHosts = hosts(
                Collections.singletonList(new AerospikeHost(host, mappedPort)));
        ClientPolicy clientPolicy = clientPolicy(aerospikeConfig);
        return new AerospikeClient(clientPolicy, aerospikeHosts.toArray(new Host[0]));
    }

}
