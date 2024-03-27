package com.sothis.tech.domain;

import static com.sothis.tech.domain.PlantTestSamples.*;
import static com.sothis.tech.domain.SiteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.sothis.tech.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PlantTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Plant.class);
        Plant plant1 = getPlantSample1();
        Plant plant2 = new Plant();
        assertThat(plant1).isNotEqualTo(plant2);

        plant2.setId(plant1.getId());
        assertThat(plant1).isEqualTo(plant2);

        plant2 = getPlantSample2();
        assertThat(plant1).isNotEqualTo(plant2);
    }

    @Test
    void siteTest() throws Exception {
        Plant plant = getPlantRandomSampleGenerator();
        Site siteBack = getSiteRandomSampleGenerator();

        plant.setSite(siteBack);
        assertThat(plant.getSite()).isEqualTo(siteBack);

        plant.site(null);
        assertThat(plant.getSite()).isNull();
    }
}
