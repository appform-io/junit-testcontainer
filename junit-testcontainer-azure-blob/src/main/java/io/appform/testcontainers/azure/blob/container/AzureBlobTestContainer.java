package io.appform.testcontainers.azure.blob.container;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.common.StorageSharedKeyCredential;
import io.appform.testcontainers.azure.blob.AzureBlobTestContainerStatusCheck;
import io.appform.testcontainers.azure.blob.config.AzureBlobTestContainerConfiguration;
import io.appform.testcontainers.commons.ContainerUtils;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.testcontainers.containers.GenericContainer;

@Slf4j
@EqualsAndHashCode(callSuper = true)
public class AzureBlobTestContainer extends GenericContainer<AzureBlobTestContainer> {

    private final AzureBlobTestContainerConfiguration azureBlobTestContainerConfiguration;

    public AzureBlobTestContainer(AzureBlobTestContainerConfiguration azureBlobTestContainerConfiguration) {
        super(azureBlobTestContainerConfiguration.getDockerImage());

        this.withEnv("AZURE_BLOB_ACCOUNT_NAME", azureBlobTestContainerConfiguration.getAccountName())
                .withEnv("AZURE_BLOB_ACCOUNT_KEY", azureBlobTestContainerConfiguration.getAccountKey())
                .withExposedPorts(AzureBlobTestContainerConfiguration.DEFAULT_PORT)
                .withLogConsumer(ContainerUtils.containerLogsConsumer(log))
                .waitingFor(new AzureBlobTestContainerStatusCheck(azureBlobTestContainerConfiguration))
                .withStartupTimeout(azureBlobTestContainerConfiguration.getTimeoutDuration());

        this.azureBlobTestContainerConfiguration = azureBlobTestContainerConfiguration;
    }

    public String getBlobStorageEmulatorEndpoint() {
        return String.format("http://%s:%s/%s", getHost(),
                getMappedPort(AzureBlobTestContainerConfiguration.DEFAULT_PORT),
                azureBlobTestContainerConfiguration.getAccountName());
    }

    public void createContainer(String containerId) {
        StorageSharedKeyCredential credential = new StorageSharedKeyCredential(
                azureBlobTestContainerConfiguration.getAccountName(), azureBlobTestContainerConfiguration.getAccountKey());
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .endpoint(getBlobStorageEmulatorEndpoint())
                .credential(credential)
                .buildClient();
        BlobContainerClient blobContainerClient = blobServiceClient.getBlobContainerClient(containerId);
        if (blobContainerClient.exists()) {
            return;
        }
        blobContainerClient.create();
    }

}
