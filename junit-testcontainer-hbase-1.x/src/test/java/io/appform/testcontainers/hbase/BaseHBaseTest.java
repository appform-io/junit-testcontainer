package io.appform.testcontainers.hbase;

import java.util.Arrays;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.appform.testcontainers.hbase.constants.TestConstants;
import io.appform.testcontainers.hbase.util.HBaseUtil;

/**
 * @author akshay.arni
 */
@ExtendWith(HBaseTestExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseHBaseTest {
    protected ObjectMapper mapper;
    protected Admin admin;
    protected Connection connection;

    @BeforeAll
    public void startup() throws Exception {
        /*
         * Mocks
         */
        mapper = new ObjectMapper();

        /*
         * HBase related initialization
         */
        connection = ConnectionFactory.createConnection();
        admin = connection.getAdmin();
        HBaseUtil.ensureTableExists(admin, TestConstants.TEST_TABLE, Arrays.asList("a", "b"));
    }

    @AfterAll
    public void teardownClass() throws Exception {
        admin.disableTable(TableName.valueOf(TestConstants.TEST_TABLE));
        admin.deleteTable(TableName.valueOf(TestConstants.TEST_TABLE));

        TestHBaseContainer.stopContainer();
    }
}
