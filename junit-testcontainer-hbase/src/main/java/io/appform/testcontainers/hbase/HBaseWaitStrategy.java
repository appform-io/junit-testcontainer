package io.appform.testcontainers.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.ClusterStatus;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

import com.github.dockerjava.api.command.InspectContainerResponse;

import io.appform.testcontainers.commons.AbstractRetryingWaitStrategy;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class HBaseWaitStrategy extends AbstractRetryingWaitStrategy {

    @Override
    protected boolean isReady() {
        final String containerId = waitStrategyTarget.getContainerId();
        log.debug("Check HBase container {} status", containerId);

        final InspectContainerResponse containerInfo = waitStrategyTarget.getContainerInfo();
        if (containerInfo == null) {
            log.debug("HBase container[{}] doesn't contain info. Abnormal situation, should not happen.", containerId);
            return false;
        }

        final Configuration configuration = HBaseConfiguration.create();
        try (final Connection connection = ConnectionFactory.createConnection(configuration);
             final Admin admin = connection.getAdmin()) {
            final ClusterStatus clusterStatus = admin.getClusterStatus();
            return clusterStatus.getDeadServers() == 0;
        } catch (Exception e) {
            log.debug("HBase container: {} has not yet started. {}", containerId, e.getMessage());
        }
        return false;
    }
}
