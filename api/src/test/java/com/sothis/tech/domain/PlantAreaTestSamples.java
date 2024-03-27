package com.sothis.tech.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PlantAreaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PlantArea getPlantAreaSample1() {
        return new PlantArea().id(1L).reference("reference1").name("name1").description("description1");
    }

    public static PlantArea getPlantAreaSample2() {
        return new PlantArea().id(2L).reference("reference2").name("name2").description("description2");
    }

    public static PlantArea getPlantAreaRandomSampleGenerator() {
        return new PlantArea()
            .id(longCount.incrementAndGet())
            .reference(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
