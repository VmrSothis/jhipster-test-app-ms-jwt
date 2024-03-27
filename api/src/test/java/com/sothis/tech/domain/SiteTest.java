package com.sothis.tech.domain;

import static com.sothis.tech.domain.OrganizationTestSamples.*;
import static com.sothis.tech.domain.SiteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.sothis.tech.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SiteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Site.class);
        Site site1 = getSiteSample1();
        Site site2 = new Site();
        assertThat(site1).isNotEqualTo(site2);

        site2.setId(site1.getId());
        assertThat(site1).isEqualTo(site2);

        site2 = getSiteSample2();
        assertThat(site1).isNotEqualTo(site2);
    }

    @Test
    void organizationTest() throws Exception {
        Site site = getSiteRandomSampleGenerator();
        Organization organizationBack = getOrganizationRandomSampleGenerator();

        site.setOrganization(organizationBack);
        assertThat(site.getOrganization()).isEqualTo(organizationBack);

        site.organization(null);
        assertThat(site.getOrganization()).isNull();
    }
}
