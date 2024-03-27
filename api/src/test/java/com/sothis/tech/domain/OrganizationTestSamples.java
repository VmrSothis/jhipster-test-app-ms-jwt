package com.sothis.tech.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class OrganizationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Organization getOrganizationSample1() {
        return new Organization()
            .id(1L)
            .reference("reference1")
            .name("name1")
            .legalName("legalName1")
            .description("description1")
            .taxId("taxId1")
            .email("email1")
            .telephone("telephone1")
            .url("url1")
            .address("address1")
            .postalCode("postalCode1")
            .region("region1")
            .locality("locality1")
            .country("country1")
            .location("location1");
    }

    public static Organization getOrganizationSample2() {
        return new Organization()
            .id(2L)
            .reference("reference2")
            .name("name2")
            .legalName("legalName2")
            .description("description2")
            .taxId("taxId2")
            .email("email2")
            .telephone("telephone2")
            .url("url2")
            .address("address2")
            .postalCode("postalCode2")
            .region("region2")
            .locality("locality2")
            .country("country2")
            .location("location2");
    }

    public static Organization getOrganizationRandomSampleGenerator() {
        return new Organization()
            .id(longCount.incrementAndGet())
            .reference(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .legalName(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .taxId(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .telephone(UUID.randomUUID().toString())
            .url(UUID.randomUUID().toString())
            .address(UUID.randomUUID().toString())
            .postalCode(UUID.randomUUID().toString())
            .region(UUID.randomUUID().toString())
            .locality(UUID.randomUUID().toString())
            .country(UUID.randomUUID().toString())
            .location(UUID.randomUUID().toString());
    }
}
