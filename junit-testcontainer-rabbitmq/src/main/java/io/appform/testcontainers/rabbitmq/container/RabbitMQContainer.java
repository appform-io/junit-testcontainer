package io.appform.testcontainers.rabbitmq.container;

import io.appform.testcontainers.commons.ContainerUtils;
import io.appform.testcontainers.rabbitmq.RabbitMQStatusCheck;
import io.appform.testcontainers.rabbitmq.config.RabbitMQContainerConfiguration;
import io.appform.testcontainers.commons.IsRunningStartupCheckStrategyWithDelay;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.testcontainers.containers.GenericContainer;

@Slf4j
@EqualsAndHashCode(callSuper = true)
public class RabbitMQContainer extends GenericContainer<RabbitMQContainer> {

    private final RabbitMQContainerConfiguration rabbitMQContainerConfiguration;

    public RabbitMQContainer() {
        this(new RabbitMQContainerConfiguration());
    }

    public RabbitMQContainer(final RabbitMQContainerConfiguration rabbitMQContainerConfiguration) {

        super(rabbitMQContainerConfiguration.getDockerImage());

        this.withEnv("RABBITMQ_DEFAULT_VHOST", rabbitMQContainerConfiguration.getVhost())
            .withEnv("RABBITMQ_DEFAULT_USER", rabbitMQContainerConfiguration.getUser())
            .withEnv("RABBITMQ_DEFAULT_PASS", rabbitMQContainerConfiguration.getPassword())
            .withExposedPorts(rabbitMQContainerConfiguration.getPort(), rabbitMQContainerConfiguration.getManagementPort())
            .withLogConsumer(ContainerUtils.containerLogsConsumer(log))
            .waitingFor(new RabbitMQStatusCheck(rabbitMQContainerConfiguration))
            .withStartupTimeout(rabbitMQContainerConfiguration.getTimeoutDuration())
            .withStartupCheckStrategy(new IsRunningStartupCheckStrategyWithDelay());
        this.rabbitMQContainerConfiguration = rabbitMQContainerConfiguration;
    }

    public int getConnectionPort() {
        return getMappedPort(rabbitMQContainerConfiguration.getPort());
    }
}
