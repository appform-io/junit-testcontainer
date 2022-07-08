package io.appform.testcontainers.zookeeper;

/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2019 Playtika
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

import io.appform.testcontainers.commons.AbstractRetryingWaitStrategy;
import io.appform.testcontainers.zookeeper.config.ZookeeperContainerConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.Socket;

@Slf4j
@RequiredArgsConstructor
public class ZookeeperStatusCheck extends AbstractRetryingWaitStrategy {

  private final ZookeeperContainerConfiguration containerConfiguration;

  @Override
  protected boolean isReady() {
    try (Socket s = new Socket(waitStrategyTarget.getHost(), waitStrategyTarget.getMappedPort(containerConfiguration.getPort()))) {
      if(log.isDebugEnabled()) {
        log.debug("Zookeeper started on host: {}, port: {}", s.getInetAddress().getHostAddress(), s.getPort());
      }
      return true;
    } catch (IOException e) {
      if(log.isDebugEnabled()) {
        log.debug("Zookeeper yet to start started on port: {}", containerConfiguration.getPort());
      }
      return false;
    }
  }
}