package io.appform.testcontainers.mariadb.config;

import io.appform.testcontainers.commons.CommonContainerConfiguration;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class MariaDbContainerConfiguration extends CommonContainerConfiguration {

    public static final int DEFAULT_PORT = 3306;
    private static final String MARIADB = "mariadb";

    private String dockerImage = "mariadb:10.6.3-focal";
    private String databaseName = "test";
    private String userName = MARIADB;
    private String userPassword = MARIADB;
    private String rootPassword = MARIADB;
}
