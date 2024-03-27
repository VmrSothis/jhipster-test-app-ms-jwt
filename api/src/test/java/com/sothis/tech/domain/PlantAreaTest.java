package com.sothis.tech.domain;

import static com.sothis.tech.domain.PlantAreaTestSamples.*;
import static com.sothis.tech.domain.PlantTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.sothis.tech.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PlantAreaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlantArea.class);
        PlantArea plantArea1 = getPlantAreaSample1();
        PlantArea plantArea2 = new PlantArea();
        assertThat(plantArea1).isNotEqualTo(plantArea2);

        plantArea2.setId(plantArea1.getId());
        assertThat(plantArea1).isEqualTo(plantArea2);

        plantArea2 = getPlantAreaSample2();
        assertThat(plantArea1).isNotEqualTo(plantArea2);
    }

    @Test
    void plantTest() throws Exception {
        PlantArea plantArea = getPlantAreaRandomSampleGenerator();
        Plant plantBack = getPlantRandomSampleGenerator();

        plantArea.setPlant(plantBack);
        assertThat(plantArea.getPlant()).isEqualTo(plantBack);

        plantArea.plant(null);
        assertThat(plantArea.getPlant()).isNull();
    }
}
