package io.appform.testcontainers.mariadb.config;

import io.appform.testcontainers.commons.CommonContainerConfiguration;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.ObjectUtils;

@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class MariaDbContainerConfiguration extends CommonContainerConfiguration {

    public static final int DEFAULT_PORT = 3306;

    private String defaultDockerImage = "mariadb:10.6.3-focal";
    private String defaultDatabaseName = "test";
    private String defaultUsername = "mariadb";
    private String defaultPassword = "mariadb";

    private String dockerImage = defaultDockerImage;

    private String databaseName = defaultDatabaseName;

    private String userName = defaultUsername;

    private String userPassword = defaultPassword;

    private String rootPassword = defaultPassword;

    @Builder
    public MariaDbContainerConfiguration(Long waitTimeoutInSeconds, Boolean enabled,
        String dockerImage, String databaseName, String userName, String userPassword,
        String rootPassword) {
        super(
            ObjectUtils.defaultIfNull(waitTimeoutInSeconds, CommonContainerConfiguration.DEFAULT_WAIT_TIMEOUT_IN_SECONDS),
            ObjectUtils.defaultIfNull(enabled, CommonContainerConfiguration.DEFAULT_IS_ENABLED));
        this.dockerImage = ObjectUtils.defaultIfNull(dockerImage, defaultDockerImage);
        this.databaseName = ObjectUtils.defaultIfNull(databaseName, defaultDatabaseName);
        this.userName = ObjectUtils.defaultIfNull(userName, defaultUsername);
        this.userPassword = ObjectUtils.defaultIfNull(userPassword, defaultPassword);
        this.rootPassword = ObjectUtils.defaultIfNull(rootPassword, defaultPassword);
    }
}
