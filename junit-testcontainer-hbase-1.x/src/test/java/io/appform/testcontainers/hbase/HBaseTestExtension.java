package io.appform.testcontainers.hbase;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author akshay.arni
 */
@Slf4j
public class HBaseTestExtension implements BeforeAllCallback, BeforeEachCallback, AfterAllCallback {

    /*
     * Ensure container instance is started just once.
     */
    static {
        log.info("********************** Starting HBase Test Container **********************");
        try {
            TestHBaseContainer.startContainer();
        } catch (Exception e) {
            log.error("Could not start hbase container: {}", e);
            /*
             * Allow quick failure of tests by throwing an exception.
             * Without this, HBase Client keeps retrying for ZK connection forever.
             */
            throw new RuntimeException(e);
        }
    }

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        log.info("********************** Inside Before All **********************");
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) {
        log.info("********************** Inside After All **********************");
    }

    @Override
    public void beforeEach(ExtensionContext extensionContext) {
        log.info("********************** Inside Before Each **********************");
    }
}
