package com.phonepe.testcontainer.commons;

import lombok.Data;

import java.time.Duration;

@Data
public class CommonContainerConfiguration {

    private long waitTimeoutInSeconds = 300;
    private boolean enabled = true;

    public Duration getTimeoutDuration() {
        return Duration.ofSeconds(waitTimeoutInSeconds);
    }
}
