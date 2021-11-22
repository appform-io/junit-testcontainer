package io.appform.testcontainers.hbase.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestConstants {
    public static final String TEST_TABLE = "test-table";
    public static final String HBASE_CONTAINER_NAME = "hbase";
    public static final String LOCALHOST = "127.0.0.1";
    public static final String DOCKER_COMPOSE_PATH = "src/test/resources/docker-compose.yml";
}
