package com.sothis.tech.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MachineDocumentationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static MachineDocumentation getMachineDocumentationSample1() {
        return new MachineDocumentation().id(1L).reference("reference1").name("name1").description("description1").url("url1");
    }

    public static MachineDocumentation getMachineDocumentationSample2() {
        return new MachineDocumentation().id(2L).reference("reference2").name("name2").description("description2").url("url2");
    }

    public static MachineDocumentation getMachineDocumentationRandomSampleGenerator() {
        return new MachineDocumentation()
            .id(longCount.incrementAndGet())
            .reference(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .url(UUID.randomUUID().toString());
    }
}
