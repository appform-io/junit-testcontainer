package io.appform.testcontainers.hbase;

import java.util.Arrays;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HConstants;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.ColumnFamilyDescriptorBuilder;
import org.apache.hadoop.hbase.client.ColumnFamilyDescriptorBuilder.ModifyableColumnFamilyDescriptor;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.TableDescriptorBuilder;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Assert;
import org.junit.Test;

public class HbaseContainerTest {

    @Test
    public void testStartup() throws Exception {
        try (HBaseContainer hbase = new HBaseContainer(new HBaseWaitStrategy())) {
            hbase.start();

            Assert.assertTrue(hbase.isRunning());
        }
    }

    @Test
    public void testTableCreation() throws Exception {
        try (HBaseContainer hbase = new HBaseContainer(new HBaseWaitStrategy())) {
            hbase.start();

            Configuration configuration = HBaseConfiguration.create();
            Connection connection = ConnectionFactory.createConnection(configuration);
            Admin admin = connection.getAdmin();
            createTable(admin, "test", Arrays.asList(Bytes.toBytes("a")));

            Assert.assertTrue(hbase.isRunning());
            Assert.assertTrue(admin.tableExists(TableName.valueOf("test")));
        }
    }

    private void createTable(final Admin admin, final String tableName, final List<byte[]> columnFamilies)
            throws Exception {
        final TableName tableToCreate = TableName.valueOf(tableName);
        final TableDescriptorBuilder builder = TableDescriptorBuilder.newBuilder(tableToCreate);
        for (final byte[] columnFamily : columnFamilies) {
            final ModifyableColumnFamilyDescriptor descriptor = new ColumnFamilyDescriptorBuilder.ModifyableColumnFamilyDescriptor(
                    columnFamily);
            descriptor.setScope(HConstants.REPLICATION_SCOPE_GLOBAL);
            builder.setColumnFamily(descriptor);
        }
        admin.createTable(builder.build());
    }

}
