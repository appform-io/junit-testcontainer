package io.appform.testcontainers.commons;

import static org.awaitility.Awaitility.await;

import com.github.dockerjava.api.DockerClient;
import lombok.extern.slf4j.Slf4j;
import org.testcontainers.containers.startupcheck.IsRunningStartupCheckStrategy;
import java.util.concurrent.TimeUnit;



/*
 * Sometimes the test container startup checks fails due to container not up or busy in acquiring IO resources
 * This delay startup check strategy is used to check the state after a {delayInMilliSec} ms delay
 * Uses:
 *      myContainer.withStartupCheckStrategy(new IsRunningStartupCheckStrategyWithDelay(5000));
 *      myContainer.withStartupCheckStrategy(new IsRunningStartupCheckStrategyWithDelay()); // uses 1000ms as default delay
 */


@Slf4j
public class IsRunningStartupCheckStrategyWithDelay extends IsRunningStartupCheckStrategy {
    
    private final long delayInMilliSec;
    
    public IsRunningStartupCheckStrategyWithDelay(final long delayInMilliSec) {
        this.delayInMilliSec = delayInMilliSec;
    }

    public IsRunningStartupCheckStrategyWithDelay() {
        this.delayInMilliSec = 1000;
    }


    @Override
    public StartupStatus checkStartupState(DockerClient dockerClient, String containerId) {

        try {
            // startup with delay.
            await().pollDelay(delayInMilliSec, TimeUnit.MILLISECONDS).until(() -> true);
        } catch (Exception e) {
            log.error("Unable to pause thread", e);
        }
        
        return super.checkStartupState(dockerClient, containerId);
    }

}
