package com.phonepe.testcontainer.demo.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AerospikeConfiguration {

  @NotNull
  private List<AerospikeHost> connection;

  private String user;

  private String password;

  @Min(0)
  private int retries = 5;

  @Min(10)
  @Max(10000)
  private int sleepBetweenRetries = 100;

  @Min(1)
  private int maxConnectionsPerNode = 5;

}
