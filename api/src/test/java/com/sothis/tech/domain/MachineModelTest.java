package com.sothis.tech.domain;

import static com.sothis.tech.domain.MachineModelTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.sothis.tech.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MachineModelTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MachineModel.class);
        MachineModel machineModel1 = getMachineModelSample1();
        MachineModel machineModel2 = new MachineModel();
        assertThat(machineModel1).isNotEqualTo(machineModel2);

        machineModel2.setId(machineModel1.getId());
        assertThat(machineModel1).isEqualTo(machineModel2);

        machineModel2 = getMachineModelSample2();
        assertThat(machineModel1).isNotEqualTo(machineModel2);
    }
}
