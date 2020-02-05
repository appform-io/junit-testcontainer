package io.appform.testcontainers.elasticsearch.utils;

/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 Playtika
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import io.appform.testcontainers.elasticsearch.CreateIndex;
import io.appform.testcontainers.elasticsearch.WaitForGreenStatus;
import io.appform.testcontainers.elasticsearch.config.ElasticsearchContainerConfiguration;
import org.testcontainers.containers.wait.strategy.HostPortWaitStrategy;
import org.testcontainers.containers.wait.strategy.WaitAllStrategy;
import org.testcontainers.containers.wait.strategy.WaitStrategy;

import static io.appform.testcontainers.commons.ContainerUtils.DEFAULT_CONTAINER_WAIT_DURATION;

public class ElasticsearchContainerUtils {

    public static String getJavaOpts(ElasticsearchContainerConfiguration configuration) {
        return "-Xms" + configuration.getClusterRamMb() + "m -Xmx" + configuration.getClusterRamMb() + "m";
    }

    public static WaitStrategy getCompositeWaitStrategy(ElasticsearchContainerConfiguration configuration) {
        WaitAllStrategy strategy = new WaitAllStrategy()
                .withStrategy(new HostPortWaitStrategy());
        configuration.getIndices().forEach(index -> strategy.withStrategy(new CreateIndex(configuration, index)));
        return strategy
                .withStrategy(new WaitForGreenStatus(configuration))
                .withStartupTimeout(DEFAULT_CONTAINER_WAIT_DURATION);
    }
}
