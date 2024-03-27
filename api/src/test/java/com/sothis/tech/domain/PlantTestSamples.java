package com.sothis.tech.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PlantTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Plant getPlantSample1() {
        return new Plant()
            .id(1L)
            .reference("reference1")
            .name("name1")
            .description("description1")
            .address("address1")
            .location("location1");
    }

    public static Plant getPlantSample2() {
        return new Plant()
            .id(2L)
            .reference("reference2")
            .name("name2")
            .description("description2")
            .address("address2")
            .location("location2");
    }

    public static Plant getPlantRandomSampleGenerator() {
        return new Plant()
            .id(longCount.incrementAndGet())
            .reference(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .address(UUID.randomUUID().toString())
            .location(UUID.randomUUID().toString());
    }
}
