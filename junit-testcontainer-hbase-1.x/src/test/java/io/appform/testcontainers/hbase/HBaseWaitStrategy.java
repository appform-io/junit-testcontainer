package io.appform.testcontainers.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.ClusterStatus;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

import io.appform.testcontainers.commons.AbstractRetryingWaitStrategy;
import lombok.extern.slf4j.Slf4j;

/**
 * @author akshay.arni
 */
@Slf4j
public class HBaseWaitStrategy extends AbstractRetryingWaitStrategy {

    @Override
    protected boolean isReady() {
        final Configuration configuration = HBaseConfiguration.create();
        try (final Connection connection = ConnectionFactory.createConnection(configuration);
                final Admin admin = connection.getAdmin()) {
            final ClusterStatus clusterStatus = admin.getClusterStatus();
            return clusterStatus.getDeadServers() == 0;
        } catch (Exception e) {
            log.debug("HBase container has not yet started. {}", e.getMessage());
        }
        return false;
    }

}
