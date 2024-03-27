package com.sothis.tech.domain;

import static com.sothis.tech.domain.MachineTestSamples.*;
import static com.sothis.tech.domain.OrganizationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.sothis.tech.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class OrganizationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Organization.class);
        Organization organization1 = getOrganizationSample1();
        Organization organization2 = new Organization();
        assertThat(organization1).isNotEqualTo(organization2);

        organization2.setId(organization1.getId());
        assertThat(organization1).isEqualTo(organization2);

        organization2 = getOrganizationSample2();
        assertThat(organization1).isNotEqualTo(organization2);
    }

    @Test
    void machineTest() throws Exception {
        Organization organization = getOrganizationRandomSampleGenerator();
        Machine machineBack = getMachineRandomSampleGenerator();

        organization.addMachine(machineBack);
        assertThat(organization.getMachines()).containsOnly(machineBack);
        assertThat(machineBack.getOrganization()).isEqualTo(organization);

        organization.removeMachine(machineBack);
        assertThat(organization.getMachines()).doesNotContain(machineBack);
        assertThat(machineBack.getOrganization()).isNull();

        organization.machines(new HashSet<>(Set.of(machineBack)));
        assertThat(organization.getMachines()).containsOnly(machineBack);
        assertThat(machineBack.getOrganization()).isEqualTo(organization);

        organization.setMachines(new HashSet<>());
        assertThat(organization.getMachines()).doesNotContain(machineBack);
        assertThat(machineBack.getOrganization()).isNull();
    }
}
