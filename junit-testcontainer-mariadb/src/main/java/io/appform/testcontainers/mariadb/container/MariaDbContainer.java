package io.appform.testcontainers.mariadb.container;

import io.appform.testcontainers.commons.ContainerUtils;
import io.appform.testcontainers.mariadb.MariaDbContainerStatusCheck;
import io.appform.testcontainers.mariadb.config.MariaDbContainerConfiguration;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.testcontainers.DockerClientFactory;
import org.testcontainers.containers.GenericContainer;

@Slf4j
@EqualsAndHashCode(callSuper = true)
public class MariaDbContainer extends GenericContainer<MariaDbContainer> {

    private final MariaDbContainerConfiguration mariaDbContainerConfiguration;

    public MariaDbContainer() {
        this(new MariaDbContainerConfiguration());
    }

    public MariaDbContainer(final MariaDbContainerConfiguration mariaDbContainerConfiguration) {

        super(mariaDbContainerConfiguration.getDockerImage());

        this.withEnv("MYSQL_DATABASE", mariaDbContainerConfiguration.getDatabaseName())
            .withEnv("MYSQL_USER", mariaDbContainerConfiguration.getUserName())
            .withEnv("MYSQL_PASSWORD", mariaDbContainerConfiguration.getUserPassword())
            .withEnv("MYSQL_ROOT_PASSWORD", mariaDbContainerConfiguration.getRootPassword())
            .withExposedPorts(MariaDbContainerConfiguration.DEFAULT_PORT)
            .withLogConsumer(ContainerUtils.containerLogsConsumer(log))
            .waitingFor(new MariaDbContainerStatusCheck(mariaDbContainerConfiguration))
            .withStartupTimeout(mariaDbContainerConfiguration.getTimeoutDuration());

        this.mariaDbContainerConfiguration = mariaDbContainerConfiguration;
    }

    public int getJdbcPort() {
        return getMappedPort(MariaDbContainerConfiguration.DEFAULT_PORT);
    }

    public String getRootPassword() {
        return mariaDbContainerConfiguration.getRootPassword();
    }

    public boolean executeQuery(final String query, final boolean asRootUser) {

        val execCmdResult =
            ContainerUtils.execCmd(DockerClientFactory.instance().client(),
                getContainerId(), new String[]{"mysql",
                    "--user=" + (asRootUser ? "root" : mariaDbContainerConfiguration.getUserName()),
                    "--password=" + (asRootUser ? mariaDbContainerConfiguration.getRootPassword()
                        : mariaDbContainerConfiguration.getUserPassword()),
                    "-e", query});

        return execCmdResult.getExitCode() == 0;
    }
}
