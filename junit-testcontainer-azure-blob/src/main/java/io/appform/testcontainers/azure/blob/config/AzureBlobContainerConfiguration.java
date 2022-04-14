package io.appform.testcontainers.azure.blob.config;

import io.appform.testcontainers.commons.CommonContainerConfiguration;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AzureBlobContainerConfiguration extends CommonContainerConfiguration {

    public static final int DEFAULT_PORT = 10000;
    public static final String DEFAULT_ACCOUNT_NAME = "devstoreaccount1";
    public static final String DEFAULT_ACCOUNT_KEY = "Eby8vdM02xNOcqFlqUwJPLlmEtlCDXJ1OUzFT50uSRZ6IFsuFq2UVErCz4I6tq/K1SZFPTOtr/KBHBeksoGMGw==";

    private String dockerImage = "mcr.microsoft.com/azure-storage/azurite";
}
