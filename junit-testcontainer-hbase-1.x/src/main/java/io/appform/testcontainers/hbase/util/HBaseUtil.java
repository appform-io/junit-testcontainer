package io.appform.testcontainers.hbase.util;

import java.util.List;

import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.util.Bytes;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HBaseUtil {

    public static void ensureTableExists(final Admin hBaseAdmin,
                                         final String tableName,
                                         final List<String> columnFamilies) {
        final HTableDescriptor hTableDescriptor = new HTableDescriptor(TableName.valueOf(tableName));

        if (columnFamilies != null) {
            columnFamilies.stream().forEach(family -> {
                final HColumnDescriptor columnDescriptor = new HColumnDescriptor(family);
                hTableDescriptor.addFamily(columnDescriptor);
            });
        }

        try {
            if (!hBaseAdmin.tableExists(TableName.valueOf(Bytes.toBytes(tableName)))) {
                hBaseAdmin.createTable(hTableDescriptor);
                log.info("table_created: {}", tableName);
            } else {
                log.info("table_exists: {}", tableName);
            }
        } catch (final Exception e) {
            log.error("Could not create table: " + tableName, e);
        }
    }

}
