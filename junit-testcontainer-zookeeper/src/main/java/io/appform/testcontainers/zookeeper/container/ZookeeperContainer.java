package io.appform.testcontainers.zookeeper.container;

import io.appform.testcontainers.commons.ContainerUtils;
import io.appform.testcontainers.zookeeper.ZookeeperStatusCheck;
import io.appform.testcontainers.zookeeper.config.ZookeeperContainerConfiguration;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.testcontainers.containers.GenericContainer;

import java.util.Objects;

@Slf4j
@EqualsAndHashCode(callSuper = true)
public class ZookeeperContainer extends GenericContainer<ZookeeperContainer> {

    private final ZookeeperContainerConfiguration zookeeperContainerConfiguration;

    public ZookeeperContainer() {
        this(new ZookeeperContainerConfiguration());
    }

    public ZookeeperContainer(final ZookeeperContainerConfiguration zookeeperContainerConfiguration) {
        super(zookeeperContainerConfiguration.getDockerImage());
        this.withEnv("ZOO_MY_ID", String.valueOf(zookeeperContainerConfiguration.getId()))
            .withEnv("ZOO_MAX_CLIENT_CNXNS", String.valueOf(zookeeperContainerConfiguration.getMaxClientConnections()))
            .withExposedPorts(zookeeperContainerConfiguration.getPort())
            .withLogConsumer(ContainerUtils.containerLogsConsumer(log))
            .waitingFor(new ZookeeperStatusCheck(zookeeperContainerConfiguration))
            .withStartupTimeout(zookeeperContainerConfiguration.getTimeoutDuration());
        if(StringUtils.isNotBlank(zookeeperContainerConfiguration.getServers())) {
            this.withEnv("ZOO_SERVERS", zookeeperContainerConfiguration.getServers());
        }
        if(StringUtils.isNotBlank(zookeeperContainerConfiguration.getJvmFlags())) {
            this.withEnv("JVMFLAGS", zookeeperContainerConfiguration.getJvmFlags());
        }
        this.zookeeperContainerConfiguration = zookeeperContainerConfiguration;
    }

    public int getConnectionPort() {
        return getMappedPort(zookeeperContainerConfiguration.getPort());
    }
}
