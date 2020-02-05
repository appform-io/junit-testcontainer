package io.appform.testcontainers.elasticsearch;

import io.appform.testcontainers.commons.AbstractCommandWaitStrategy;
import io.appform.testcontainers.elasticsearch.config.ElasticsearchContainerConfiguration;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WaitForGreenStatus extends AbstractCommandWaitStrategy {

    private final ElasticsearchContainerConfiguration containerConfiguration;

    @Override
    public String[] getCheckCommand() {
        return new String[]{
                "curl", "-X", "GET",
                "http://127.0.0.1:" + containerConfiguration.getHttpPort() + "/_cluster/health?wait_for_status=green&timeout=10s"
        };
    }
}

