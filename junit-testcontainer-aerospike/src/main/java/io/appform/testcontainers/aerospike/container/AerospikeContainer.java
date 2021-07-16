package io.appform.testcontainers.aerospike.container;

import io.appform.testcontainers.aerospike.AerospikeContainerConfiguration;
import io.appform.testcontainers.aerospike.AerospikeWaitStrategy;
import io.appform.testcontainers.commons.ContainerUtils;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.testcontainers.containers.GenericContainer;

@Slf4j
@EqualsAndHashCode(callSuper = true)
public class AerospikeContainer extends GenericContainer<AerospikeContainer> {

    private final AerospikeContainerConfiguration aerospikeContainerConfig;

    public AerospikeContainer() {
        this(new AerospikeContainerConfiguration());
    }

    public AerospikeContainer(final AerospikeContainerConfiguration aerospikeContainerConfig) {

        super(aerospikeContainerConfig.getDockerImage());

        this.withEnv("NAMESPACE", aerospikeContainerConfig.getNamespace())
            .withEnv("SERVICE_PORT", String.valueOf(aerospikeContainerConfig.getPort()))
            .withEnv("MEM_GB", String.valueOf(1))
            .withEnv("STORAGE_GB", String.valueOf(1))
            .withExposedPorts(aerospikeContainerConfig.getPort())
            .withLogConsumer(ContainerUtils.containerLogsConsumer(log))
            .waitingFor(new AerospikeWaitStrategy(aerospikeContainerConfig))
            .withStartupTimeout(aerospikeContainerConfig.getTimeoutDuration());

        this.aerospikeContainerConfig = aerospikeContainerConfig;
    }

    public int getConnectionPort() {
        return getMappedPort(aerospikeContainerConfig.getPort());
    }
}