package io.appform.testcontainers.hbase;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;

import org.apache.hadoop.hbase.TableName;
import org.junit.jupiter.api.Test;

import io.appform.testcontainers.hbase.util.HBaseUtil;

/**
 * @author akshay.arni
 */
class HBaseSanityTest extends BaseHBaseTest {

    /*
     * Test that HBase is up and running.
     */
    @Test
    void testTableCreation() throws Exception {
        HBaseUtil.ensureTableExists(admin, "football", Collections.singletonList("a"));

        assertTrue(admin.tableExists(TableName.valueOf("football")));
    }

}
