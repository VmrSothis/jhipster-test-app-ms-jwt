package com.sothis.tech.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.sothis.tech.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PlantAreaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlantAreaDTO.class);
        PlantAreaDTO plantAreaDTO1 = new PlantAreaDTO();
        plantAreaDTO1.setId(1L);
        PlantAreaDTO plantAreaDTO2 = new PlantAreaDTO();
        assertThat(plantAreaDTO1).isNotEqualTo(plantAreaDTO2);
        plantAreaDTO2.setId(plantAreaDTO1.getId());
        assertThat(plantAreaDTO1).isEqualTo(plantAreaDTO2);
        plantAreaDTO2.setId(2L);
        assertThat(plantAreaDTO1).isNotEqualTo(plantAreaDTO2);
        plantAreaDTO1.setId(null);
        assertThat(plantAreaDTO1).isNotEqualTo(plantAreaDTO2);
    }
}
