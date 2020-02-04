package io.appform.testcontainers.demo.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.dropwizard.Configuration;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author jitendra.dhawan
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper = true)
public class DemoAppConfiguration extends Configuration {
    private AerospikeConfiguration aerospikeConfiguration;

}
