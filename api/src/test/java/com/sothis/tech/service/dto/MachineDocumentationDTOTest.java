package com.sothis.tech.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.sothis.tech.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MachineDocumentationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MachineDocumentationDTO.class);
        MachineDocumentationDTO machineDocumentationDTO1 = new MachineDocumentationDTO();
        machineDocumentationDTO1.setId(1L);
        MachineDocumentationDTO machineDocumentationDTO2 = new MachineDocumentationDTO();
        assertThat(machineDocumentationDTO1).isNotEqualTo(machineDocumentationDTO2);
        machineDocumentationDTO2.setId(machineDocumentationDTO1.getId());
        assertThat(machineDocumentationDTO1).isEqualTo(machineDocumentationDTO2);
        machineDocumentationDTO2.setId(2L);
        assertThat(machineDocumentationDTO1).isNotEqualTo(machineDocumentationDTO2);
        machineDocumentationDTO1.setId(null);
        assertThat(machineDocumentationDTO1).isNotEqualTo(machineDocumentationDTO2);
    }
}
