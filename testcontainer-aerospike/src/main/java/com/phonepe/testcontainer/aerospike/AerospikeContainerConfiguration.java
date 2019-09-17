package com.phonepe.testcontainer.aerospike;

import com.phonepe.testcontainer.commons.CommonContainerConfiguration;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AerospikeContainerConfiguration extends CommonContainerConfiguration {

    public static final String AEROSPIKE_BEAN_NAME = "aerospike";

    private boolean enabled = true;
    private String dockerImage = "aerospike/aerospike-server:4.3.0.8";
    private String namespace = "mercury";
    private String host = "localhost";
    private int port = 3000;
}
