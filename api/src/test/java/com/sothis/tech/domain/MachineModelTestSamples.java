package com.sothis.tech.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MachineModelTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static MachineModel getMachineModelSample1() {
        return new MachineModel()
            .id(1L)
            .reference("reference1")
            .name("name1")
            .brandName("brandName1")
            .description("description1")
            .type("type1")
            .manufacurerName("manufacurerName1")
            .version("version1");
    }

    public static MachineModel getMachineModelSample2() {
        return new MachineModel()
            .id(2L)
            .reference("reference2")
            .name("name2")
            .brandName("brandName2")
            .description("description2")
            .type("type2")
            .manufacurerName("manufacurerName2")
            .version("version2");
    }

    public static MachineModel getMachineModelRandomSampleGenerator() {
        return new MachineModel()
            .id(longCount.incrementAndGet())
            .reference(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .brandName(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .type(UUID.randomUUID().toString())
            .manufacurerName(UUID.randomUUID().toString())
            .version(UUID.randomUUID().toString());
    }
}
