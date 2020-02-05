package io.appform.testcontainers.elasticsearch.utils;

import io.appform.testcontainers.elasticsearch.CreateIndex;
import io.appform.testcontainers.elasticsearch.WaitForGreenStatus;
import io.appform.testcontainers.elasticsearch.config.ElasticsearchContainerConfiguration;
import org.testcontainers.containers.wait.strategy.HostPortWaitStrategy;
import org.testcontainers.containers.wait.strategy.WaitAllStrategy;
import org.testcontainers.containers.wait.strategy.WaitStrategy;

import static io.appform.testcontainers.commons.ContainerUtils.DEFAULT_CONTAINER_WAIT_DURATION;

public class ElasticsearchContainerUtils {

    public static String getJavaOpts(ElasticsearchContainerConfiguration configuration) {
        return "-Xms" + configuration.getClusterRamMb() + "m -Xmx" + configuration.getClusterRamMb() + "m";
    }

    public static WaitStrategy getCompositeWaitStrategy(ElasticsearchContainerConfiguration configuration) {
        WaitAllStrategy strategy = new WaitAllStrategy()
                .withStrategy(new HostPortWaitStrategy());
        configuration.getIndices().forEach(index -> strategy.withStrategy(new CreateIndex(configuration, index)));
        return strategy
                .withStrategy(new WaitForGreenStatus(configuration))
                .withStartupTimeout(DEFAULT_CONTAINER_WAIT_DURATION);
    }
}
