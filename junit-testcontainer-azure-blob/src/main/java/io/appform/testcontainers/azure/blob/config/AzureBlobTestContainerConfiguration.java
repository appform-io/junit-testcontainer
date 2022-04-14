package io.appform.testcontainers.azure.blob.config;

import io.appform.testcontainers.commons.CommonContainerConfiguration;
import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AzureBlobTestContainerConfiguration extends CommonContainerConfiguration {

    public static final int DEFAULT_PORT = 10000;

    @NotEmpty
    private String accountName;

    @NotEmpty
    private String accountKey;

    private String dockerImage = "mcr.microsoft.com/azure-storage/azurite";

    public AzureBlobTestContainerConfiguration(String accountName, String accountKey) {
        super();
        this.accountName = accountName;
        this.accountKey = accountKey;
    }

    public AzureBlobTestContainerConfiguration(String accountName, String accountKey, String dockerImage) {
        this(accountName, accountKey);
        this.dockerImage = dockerImage;
    }

}
