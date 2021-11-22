package io.appform.testcontainers.hbase;

import io.appform.testcontainers.commons.CommonContainerConfiguration;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class HBaseContainerConfiguration extends CommonContainerConfiguration {

    public static final String HBASE_DOCKER_PATH = "testhbase";
    public static final String HBASE_VERSION = "2.0.0";
    public static final String HBASE_USER_ID = "1000";
    public static final String HBASE_GROUP_ID = "1000";

}
