package io.appform.testcontainers.azure.blob.container;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.common.StorageSharedKeyCredential;
import io.appform.testcontainers.azure.blob.AzureBlobContainerStatusCheck;
import io.appform.testcontainers.azure.blob.config.AzureBlobContainerConfiguration;
import io.appform.testcontainers.commons.ContainerUtils;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.testcontainers.containers.GenericContainer;

@Slf4j
@EqualsAndHashCode(callSuper = true)
public class AzureBlobTestContainer extends GenericContainer<AzureBlobTestContainer> {

    private final AzureBlobContainerConfiguration azureBlobContainerConfiguration;

    public AzureBlobTestContainer(AzureBlobContainerConfiguration azureBlobContainerConfiguration) {
        super(azureBlobContainerConfiguration.getDockerImage());

        this.withEnv("AZURE_BLOB_ACCOUNT_NAME", azureBlobContainerConfiguration.getAccountName())
                .withEnv("AZURE_BLOB_ACCOUNT_KEY", azureBlobContainerConfiguration.getAccountKey())
                .withExposedPorts(AzureBlobContainerConfiguration.DEFAULT_PORT)
                .withLogConsumer(ContainerUtils.containerLogsConsumer(log))
                .waitingFor(new AzureBlobContainerStatusCheck(azureBlobContainerConfiguration))
                .withStartupTimeout(azureBlobContainerConfiguration.getTimeoutDuration());

        this.azureBlobContainerConfiguration = azureBlobContainerConfiguration;
    }

    public String getBlobStorageEmulatorEndpoint() {
        return String.format("http://%s:%s/%s", getHost(),
                getMappedPort(AzureBlobContainerConfiguration.DEFAULT_PORT),
                azureBlobContainerConfiguration.getAccountName());
    }

    public void createContainer(String containerId) {
        StorageSharedKeyCredential credential = new StorageSharedKeyCredential(
                azureBlobContainerConfiguration.getAccountName(), azureBlobContainerConfiguration.getAccountKey());
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
