package com.sothis.tech.domain;

import static com.sothis.tech.domain.MachineModelTestSamples.*;
import static com.sothis.tech.domain.MachineTestSamples.*;
import static com.sothis.tech.domain.OrganizationTestSamples.*;
import static com.sothis.tech.domain.PlantAreaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.sothis.tech.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MachineTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Machine.class);
        Machine machine1 = getMachineSample1();
        Machine machine2 = new Machine();
        assertThat(machine1).isNotEqualTo(machine2);

        machine2.setId(machine1.getId());
        assertThat(machine1).isEqualTo(machine2);

        machine2 = getMachineSample2();
        assertThat(machine1).isNotEqualTo(machine2);
    }

    @Test
    void plantAreaTest() throws Exception {
        Machine machine = getMachineRandomSampleGenerator();
        PlantArea plantAreaBack = getPlantAreaRandomSampleGenerator();

        machine.setPlantArea(plantAreaBack);
        assertThat(machine.getPlantArea()).isEqualTo(plantAreaBack);

        machine.plantArea(null);
        assertThat(machine.getPlantArea()).isNull();
    }

    @Test
    void machineModelTest() throws Exception {
        Machine machine = getMachineRandomSampleGenerator();
        MachineModel machineModelBack = getMachineModelRandomSampleGenerator();

        machine.setMachineModel(machineModelBack);
        assertThat(machine.getMachineModel()).isEqualTo(machineModelBack);

        machine.machineModel(null);
        assertThat(machine.getMachineModel()).isNull();
    }

    @Test
    void organizationTest() throws Exception {
        Machine machine = getMachineRandomSampleGenerator();
        Organization organizationBack = getOrganizationRandomSampleGenerator();

        machine.setOrganization(organizationBack);
        assertThat(machine.getOrganization()).isEqualTo(organizationBack);

        machine.organization(null);
        assertThat(machine.getOrganization()).isNull();
    }
}
