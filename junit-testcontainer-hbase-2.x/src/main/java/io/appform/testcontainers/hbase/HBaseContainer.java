/* Copyright 2021 JanusGraph Authors

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License. */

package io.appform.testcontainers.hbase;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;

public class HBaseContainer extends GenericContainer<HBaseContainer> {

    public HBaseContainer(final HBaseWaitStrategy waitStrategy) {
        super(getDockerImageFromDockerFile());
        addFixedExposedPort(2181, 2181);
        addFixedExposedPort(16000, 16000);
        addFixedExposedPort(16010, 16010);
        addFixedExposedPort(16020, 16020);
        addFixedExposedPort(16030, 16030);
        withCreateContainerCmdModifier(createContainerCmd -> createContainerCmd.withHostName("localhost"));
        waitingFor(waitStrategy);
    }

    private static ImageFromDockerfile getDockerImageFromDockerFile() {
        return new ImageFromDockerfile()
                .withFileFromPath(".", getPath())
                .withBuildArg("HBASE_VERSION", HBaseContainerConfiguration.HBASE_VERSION)
                .withBuildArg("HBASE_UID", HBaseContainerConfiguration.HBASE_USER_ID)
                .withBuildArg("HBASE_GID", HBaseContainerConfiguration.HBASE_GROUP_ID);
    }

    private static Path getPath() {
        try {
            Path path = Paths.get(".").toRealPath();
            return Paths.get(path.toString(), HBaseContainerConfiguration.HBASE_DOCKER_PATH);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}