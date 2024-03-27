package com.sothis.tech.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.sothis.tech.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MachineModelDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MachineModelDTO.class);
        MachineModelDTO machineModelDTO1 = new MachineModelDTO();
        machineModelDTO1.setId(1L);
        MachineModelDTO machineModelDTO2 = new MachineModelDTO();
        assertThat(machineModelDTO1).isNotEqualTo(machineModelDTO2);
        machineModelDTO2.setId(machineModelDTO1.getId());
        assertThat(machineModelDTO1).isEqualTo(machineModelDTO2);
        machineModelDTO2.setId(2L);
        assertThat(machineModelDTO1).isNotEqualTo(machineModelDTO2);
        machineModelDTO1.setId(null);
        assertThat(machineModelDTO1).isNotEqualTo(machineModelDTO2);
    }
}
