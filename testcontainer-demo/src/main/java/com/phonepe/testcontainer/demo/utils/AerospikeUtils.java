package com.phonepe.testcontainer.demo.utils;

import com.aerospike.client.Host;
import com.aerospike.client.policy.*;
import com.phonepe.testcontainer.demo.config.AerospikeConfiguration;
import com.phonepe.testcontainer.demo.config.AerospikeHost;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class AerospikeUtils {

    private AerospikeUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static ClientPolicy clientPolicy(AerospikeConfiguration aerospikeConfig) {
        ClientPolicy clientPolicy = new ClientPolicy();
        clientPolicy.readPolicyDefault = readPolicy(aerospikeConfig);
        clientPolicy.writePolicyDefault = writePolicy(aerospikeConfig);
        clientPolicy.maxConnsPerNode = aerospikeConfig.getMaxConnectionsPerNode();
        clientPolicy.threadPool = Executors
                .newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 4);
        clientPolicy.failIfNotConnected = true;
        return clientPolicy;
    }


    private static Policy readPolicy(AerospikeConfiguration aerospikeConfig) {
        Policy readPolicy = new Policy();
        readPolicy.maxRetries = aerospikeConfig.getRetries();
        readPolicy.sleepBetweenRetries = aerospikeConfig.getSleepBetweenRetries();
        readPolicy.sendKey = true;
        readPolicy.readModeAP = ReadModeAP.ONE;
        readPolicy.replica = Replica.MASTER_PROLES;
        return readPolicy;
    }

    private static WritePolicy writePolicy(AerospikeConfiguration aerospikeConfig) {
        WritePolicy writePolicy = new WritePolicy();
        writePolicy.maxRetries = aerospikeConfig.getRetries();
        writePolicy.sleepBetweenRetries = aerospikeConfig.getSleepBetweenRetries();
        writePolicy.commitLevel = CommitLevel.COMMIT_ALL;
        writePolicy.sendKey = true;
        writePolicy.replica = Replica.MASTER_PROLES;
        return writePolicy;
    }


    public static List<Host> hosts(List<AerospikeHost> connections) {
        return connections.stream()
                .map(connection -> new Host(connection.getHost(), connection.getPort()))
                .collect(Collectors.toList());
    }

}
