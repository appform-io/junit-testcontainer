package io.appform.testcontainers.azure.blob;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.common.StorageSharedKeyCredential;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.NetworkSettings;
import com.github.dockerjava.api.model.Ports;
import io.appform.testcontainers.azure.blob.config.AzureBlobContainerConfiguration;
import io.appform.testcontainers.commons.AbstractRetryingWaitStrategy;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.testcontainers.DockerClientFactory;

import java.util.Map;

@Slf4j
@AllArgsConstructor
public class AzureBlobContainerStatusCheck extends AbstractRetryingWaitStrategy {

    private final AzureBlobContainerConfiguration azureBlobContainerConfiguration;

    @Override
    protected boolean isReady() {
        String containerId = waitStrategyTarget.getContainerId();
        log.debug("Check Azure Blob container {} status", containerId);

        InspectContainerResponse containerInfo = waitStrategyTarget.getContainerInfo();
        if (containerInfo == null) {
            log.debug(
                    "Azure Blob container[{}] doesn't contain info. Abnormal situation, should not happen.",
                    containerId);
            return false;
        }

        int port = getMappedPort(containerInfo.getNetworkSettings(),
                AzureBlobContainerConfiguration.DEFAULT_PORT);
        String host = DockerClientFactory.instance().dockerHostIpAddress();

        StorageSharedKeyCredential credential = new StorageSharedKeyCredential(
                AzureBlobContainerConfiguration.DEFAULT_ACCOUNT_NAME, AzureBlobContainerConfiguration.DEFAULT_ACCOUNT_KEY);

        String endpoint = String.format("http://%s:%s/%s", host, port, AzureBlobContainerConfiguration.DEFAULT_ACCOUNT_NAME);
        log.info("url:{}",endpoint);
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .endpoint(endpoint)
                .credential(credential)
                .buildClient();
        BlobContainerClient blobContainerClient = blobServiceClient.getBlobContainerClient("test");
        try {
           blobContainerClient.create();
           return true;
        } catch (Exception e) {
            log.debug("Azure Blob container: {} not yet started. {}", containerId, e.getMessage());
        }
        return false;
    }

    private int getMappedPort(NetworkSettings networkSettings, int originalPort) {
        ExposedPort exposedPort = new ExposedPort(originalPort);
        Ports ports = networkSettings.getPorts();
        Map<ExposedPort, Ports.Binding[]> bindings = ports.getBindings();
        Ports.Binding[] binding = bindings.get(exposedPort);
        return Integer.valueOf(binding[0].getHostPortSpec());
    }
}
