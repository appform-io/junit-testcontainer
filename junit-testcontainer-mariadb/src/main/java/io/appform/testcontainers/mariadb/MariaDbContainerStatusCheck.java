package io.appform.testcontainers.mariadb;

import io.appform.testcontainers.commons.AbstractCommandWaitStrategy;
import io.appform.testcontainers.mariadb.config.MariaDbContainerConfiguration;

public class MariaDbContainerStatusCheck extends AbstractCommandWaitStrategy {

    private final MariaDbContainerConfiguration mariaDbContainerConfiguration;

    public MariaDbContainerStatusCheck(
        final MariaDbContainerConfiguration mariaDbContainerConfiguration) {
        this.mariaDbContainerConfiguration = mariaDbContainerConfiguration;
    }

    @Override
    public String[] getCheckCommand() {
        return new String[]{
            "mysql", "--user=root",
            "--password=" + mariaDbContainerConfiguration.getRootPassword(),
            "-e", "select 1;"};
    }
}
