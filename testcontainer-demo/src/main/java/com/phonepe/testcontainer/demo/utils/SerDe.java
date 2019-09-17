package com.phonepe.testcontainer.demo.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;

/**
 * Object mapper global access
 */
public class SerDe {

    private SerDe() {
        throw new IllegalStateException("Utility class");
    }

    private static ObjectMapper mapper;

    public static void init(ObjectMapper objectMapper) {
        mapper = objectMapper;
    }

    public static ObjectMapper mapper() {
        Preconditions.checkNotNull(mapper, "Please call SerDe.init(mapper) to set mapper");
        return mapper;
    }
}
