package io.appform.testcontainers.zookeeper;

import io.appform.testcontainers.zookeeper.config.ZookeeperContainerConfiguration;
import io.appform.testcontainers.zookeeper.container.ZookeeperContainer;
import org.junit.Assert;
import org.junit.Test;

public class TestZookeeperContainer {

  @Test
  public void testStartup() throws Exception {
    try (ZookeeperContainer zookeeperContainer = new ZookeeperContainer(new ZookeeperContainerConfiguration())) {
      zookeeperContainer.start();
      Assert.assertTrue(zookeeperContainer.isRunning());
    }
  }
}
