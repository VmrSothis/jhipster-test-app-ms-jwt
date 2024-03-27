package com.sothis.tech.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MachineTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Machine getMachineSample1() {
        return new Machine()
            .id(1L)
            .reference("reference1")
            .name("name1")
            .description("description1")
            .firmwareVersion("firmwareVersion1")
            .hardwareVersion("hardwareVersion1")
            .softwareVersion("softwareVersion1")
            .serialNumber("serialNumber1")
            .supportedProtocol("supportedProtocol1");
    }

    public static Machine getMachineSample2() {
        return new Machine()
            .id(2L)
            .reference("reference2")
            .name("name2")
            .description("description2")
            .firmwareVersion("firmwareVersion2")
            .hardwareVersion("hardwareVersion2")
            .softwareVersion("softwareVersion2")
            .serialNumber("serialNumber2")
            .supportedProtocol("supportedProtocol2");
    }

    public static Machine getMachineRandomSampleGenerator() {
        return new Machine()
            .id(longCount.incrementAndGet())
            .reference(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .firmwareVersion(UUID.randomUUID().toString())
            .hardwareVersion(UUID.randomUUID().toString())
            .softwareVersion(UUID.randomUUID().toString())
            .serialNumber(UUID.randomUUID().toString())
            .supportedProtocol(UUID.randomUUID().toString());
    }
}
