package io.appform.testcontainers.hbase;

import java.io.File;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.HostPortWaitStrategy;
import org.testcontainers.containers.wait.strategy.WaitAllStrategy;
import org.testcontainers.containers.wait.strategy.WaitStrategy;

import com.alibaba.dcm.DnsCacheManipulator;

import io.appform.testcontainers.hbase.constants.TestConstants;

/**
 * @author akshay.arni
 */
public class TestHBaseContainer {

    private static DockerComposeContainer<?> compose;

    public static void startContainer() throws Exception {
        compose = new DockerComposeContainer<>(new File(TestConstants.DOCKER_COMPOSE_PATH));
        DnsCacheManipulator.setDnsCache(TestConstants.HBASE_CONTAINER_NAME, TestConstants.LOCALHOST);

        final WaitStrategy waitStrategy = new WaitAllStrategy()
                .withStrategy(new HBaseWaitStrategy())
                .withStrategy(new HostPortWaitStrategy())
                .withStartupTimeout(Duration.of(300, ChronoUnit.SECONDS));
        compose.waitingFor(TestConstants.HBASE_CONTAINER_NAME, waitStrategy);

        compose.start();
    }

    public static void stopContainer() {
        compose.stop();
    }
}
