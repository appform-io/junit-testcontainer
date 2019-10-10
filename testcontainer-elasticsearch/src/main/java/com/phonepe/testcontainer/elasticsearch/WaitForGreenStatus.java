package com.phonepe.testcontainer.elasticsearch;

import com.phonepe.testcontainer.commons.AbstractCommandWaitStrategy;
import com.phonepe.testcontainer.elasticsearch.config.ElasticsearchContainerConfiguration;
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

