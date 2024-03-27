package com.sothis.tech.web.rest;

import static com.sothis.tech.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sothis.tech.IntegrationTest;
import com.sothis.tech.domain.Organization;
import com.sothis.tech.domain.Site;
import com.sothis.tech.repository.SiteRepository;
import com.sothis.tech.service.SiteService;
import com.sothis.tech.service.dto.SiteDTO;
import com.sothis.tech.service.mapper.SiteMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SiteResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SiteResourceIT {

    private static final String DEFAULT_REFERENCE = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_TELEPHONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEPHONE = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_POSTAL_CODE = "AAAAAAAAAA";
    private static final String UPDATED_POSTAL_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_REGION = "AAAAAAAAAA";
    private static final String UPDATED_REGION = "BBBBBBBBBB";

    private static final String DEFAULT_LOCALITY = "AAAAAAAAAA";
    private static final String UPDATED_LOCALITY = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/sites";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SiteRepository siteRepository;

    @Mock
    private SiteRepository siteRepositoryMock;

    @Autowired
    private SiteMapper siteMapper;

    @Mock
    private SiteService siteServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSiteMockMvc;

    private Site site;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Site createEntity(EntityManager em) {
        Site site = new Site()
            .reference(DEFAULT_REFERENCE)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .email(DEFAULT_EMAIL)
            .telephone(DEFAULT_TELEPHONE)
            .address(DEFAULT_ADDRESS)
            .postalCode(DEFAULT_POSTAL_CODE)
            .region(DEFAULT_REGION)
            .locality(DEFAULT_LOCALITY)
            .country(DEFAULT_COUNTRY)
            .location(DEFAULT_LOCATION)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        // Add required entity
        Organization organization;
        if (TestUtil.findAll(em, Organization.class).isEmpty()) {
            organization = OrganizationResourceIT.createEntity(em);
            em.persist(organization);
            em.flush();
        } else {
            organization = TestUtil.findAll(em, Organization.class).get(0);
        }
        site.setOrganization(organization);
        return site;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Site createUpdatedEntity(EntityManager em) {
        Site site = new Site()
            .reference(UPDATED_REFERENCE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .email(UPDATED_EMAIL)
            .telephone(UPDATED_TELEPHONE)
            .address(UPDATED_ADDRESS)
            .postalCode(UPDATED_POSTAL_CODE)
            .region(UPDATED_REGION)
            .locality(UPDATED_LOCALITY)
            .country(UPDATED_COUNTRY)
            .location(UPDATED_LOCATION)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        // Add required entity
        Organization organization;
        if (TestUtil.findAll(em, Organization.class).isEmpty()) {
            organization = OrganizationResourceIT.createUpdatedEntity(em);
            em.persist(organization);
            em.flush();
        } else {
            organization = TestUtil.findAll(em, Organization.class).get(0);
        }
        site.setOrganization(organization);
        return site;
    }

    @BeforeEach
    public void initTest() {
        site = createEntity(em);
    }

    @Test
    @Transactional
    void createSite() throws Exception {
        int databaseSizeBeforeCreate = siteRepository.findAll().size();
        // Create the Site
        SiteDTO siteDTO = siteMapper.toDto(site);
        restSiteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(siteDTO)))
            .andExpect(status().isCreated());

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll();
        assertThat(siteList).hasSize(databaseSizeBeforeCreate + 1);
        Site testSite = siteList.get(siteList.size() - 1);
        assertThat(testSite.getReference()).isEqualTo(DEFAULT_REFERENCE);
        assertThat(testSite.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSite.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSite.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testSite.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
        assertThat(testSite.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testSite.getPostalCode()).isEqualTo(DEFAULT_POSTAL_CODE);
        assertThat(testSite.getRegion()).isEqualTo(DEFAULT_REGION);
        assertThat(testSite.getLocality()).isEqualTo(DEFAULT_LOCALITY);
        assertThat(testSite.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testSite.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testSite.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testSite.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createSiteWithExistingId() throws Exception {
        // Create the Site with an existing ID
        site.setId(1L);
        SiteDTO siteDTO = siteMapper.toDto(site);

        int databaseSizeBeforeCreate = siteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSiteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(siteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll();
        assertThat(siteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkReferenceIsRequired() throws Exception {
        int databaseSizeBeforeTest = siteRepository.findAll().size();
        // set the field null
        site.setReference(null);

        // Create the Site, which fails.
        SiteDTO siteDTO = siteMapper.toDto(site);

        restSiteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(siteDTO)))
            .andExpect(status().isBadRequest());

        List<Site> siteList = siteRepository.findAll();
        assertThat(siteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = siteRepository.findAll().size();
        // set the field null
        site.setName(null);

        // Create the Site, which fails.
        SiteDTO siteDTO = siteMapper.toDto(site);

        restSiteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(siteDTO)))
            .andExpect(status().isBadRequest());

        List<Site> siteList = siteRepository.findAll();
        assertThat(siteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSites() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList
        restSiteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(site.getId().intValue())))
            .andExpect(jsonPath("$.[*].reference").value(hasItem(DEFAULT_REFERENCE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE)))
            .andExpect(jsonPath("$.[*].region").value(hasItem(DEFAULT_REGION)))
            .andExpect(jsonPath("$.[*].locality").value(hasItem(DEFAULT_LOCALITY)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSitesWithEagerRelationshipsIsEnabled() throws Exception {
        when(siteServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSiteMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(siteServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSitesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(siteServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSiteMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(siteRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getSite() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get the site
        restSiteMockMvc
            .perform(get(ENTITY_API_URL_ID, site.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(site.getId().intValue()))
            .andExpect(jsonPath("$.reference").value(DEFAULT_REFERENCE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.postalCode").value(DEFAULT_POSTAL_CODE))
            .andExpect(jsonPath("$.region").value(DEFAULT_REGION))
            .andExpect(jsonPath("$.locality").value(DEFAULT_LOCALITY))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.updatedAt").value(sameInstant(DEFAULT_UPDATED_AT)));
    }

    @Test
    @Transactional
    void getSitesByIdFiltering() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        Long id = site.getId();

        defaultSiteShouldBeFound("id.equals=" + id);
        defaultSiteShouldNotBeFound("id.notEquals=" + id);

        defaultSiteShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSiteShouldNotBeFound("id.greaterThan=" + id);

        defaultSiteShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSiteShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSitesByReferenceIsEqualToSomething() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where reference equals to DEFAULT_REFERENCE
        defaultSiteShouldBeFound("reference.equals=" + DEFAULT_REFERENCE);

        // Get all the siteList where reference equals to UPDATED_REFERENCE
        defaultSiteShouldNotBeFound("reference.equals=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    void getAllSitesByReferenceIsInShouldWork() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where reference in DEFAULT_REFERENCE or UPDATED_REFERENCE
        defaultSiteShouldBeFound("reference.in=" + DEFAULT_REFERENCE + "," + UPDATED_REFERENCE);

        // Get all the siteList where reference equals to UPDATED_REFERENCE
        defaultSiteShouldNotBeFound("reference.in=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    void getAllSitesByReferenceIsNullOrNotNull() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where reference is not null
        defaultSiteShouldBeFound("reference.specified=true");

        // Get all the siteList where reference is null
        defaultSiteShouldNotBeFound("reference.specified=false");
    }

    @Test
    @Transactional
    void getAllSitesByReferenceContainsSomething() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where reference contains DEFAULT_REFERENCE
        defaultSiteShouldBeFound("reference.contains=" + DEFAULT_REFERENCE);

        // Get all the siteList where reference contains UPDATED_REFERENCE
        defaultSiteShouldNotBeFound("reference.contains=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    void getAllSitesByReferenceNotContainsSomething() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where reference does not contain DEFAULT_REFERENCE
        defaultSiteShouldNotBeFound("reference.doesNotContain=" + DEFAULT_REFERENCE);

        // Get all the siteList where reference does not contain UPDATED_REFERENCE
        defaultSiteShouldBeFound("reference.doesNotContain=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    void getAllSitesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where name equals to DEFAULT_NAME
        defaultSiteShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the siteList where name equals to UPDATED_NAME
        defaultSiteShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSitesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where name in DEFAULT_NAME or UPDATED_NAME
        defaultSiteShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the siteList where name equals to UPDATED_NAME
        defaultSiteShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSitesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where name is not null
        defaultSiteShouldBeFound("name.specified=true");

        // Get all the siteList where name is null
        defaultSiteShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllSitesByNameContainsSomething() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where name contains DEFAULT_NAME
        defaultSiteShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the siteList where name contains UPDATED_NAME
        defaultSiteShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSitesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where name does not contain DEFAULT_NAME
        defaultSiteShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the siteList where name does not contain UPDATED_NAME
        defaultSiteShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSitesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where description equals to DEFAULT_DESCRIPTION
        defaultSiteShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the siteList where description equals to UPDATED_DESCRIPTION
        defaultSiteShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSitesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultSiteShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the siteList where description equals to UPDATED_DESCRIPTION
        defaultSiteShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSitesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where description is not null
        defaultSiteShouldBeFound("description.specified=true");

        // Get all the siteList where description is null
        defaultSiteShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllSitesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where description contains DEFAULT_DESCRIPTION
        defaultSiteShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the siteList where description contains UPDATED_DESCRIPTION
        defaultSiteShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSitesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where description does not contain DEFAULT_DESCRIPTION
        defaultSiteShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the siteList where description does not contain UPDATED_DESCRIPTION
        defaultSiteShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSitesByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where email equals to DEFAULT_EMAIL
        defaultSiteShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the siteList where email equals to UPDATED_EMAIL
        defaultSiteShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllSitesByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultSiteShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the siteList where email equals to UPDATED_EMAIL
        defaultSiteShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllSitesByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where email is not null
        defaultSiteShouldBeFound("email.specified=true");

        // Get all the siteList where email is null
        defaultSiteShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllSitesByEmailContainsSomething() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where email contains DEFAULT_EMAIL
        defaultSiteShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the siteList where email contains UPDATED_EMAIL
        defaultSiteShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllSitesByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where email does not contain DEFAULT_EMAIL
        defaultSiteShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the siteList where email does not contain UPDATED_EMAIL
        defaultSiteShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllSitesByTelephoneIsEqualToSomething() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where telephone equals to DEFAULT_TELEPHONE
        defaultSiteShouldBeFound("telephone.equals=" + DEFAULT_TELEPHONE);

        // Get all the siteList where telephone equals to UPDATED_TELEPHONE
        defaultSiteShouldNotBeFound("telephone.equals=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllSitesByTelephoneIsInShouldWork() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where telephone in DEFAULT_TELEPHONE or UPDATED_TELEPHONE
        defaultSiteShouldBeFound("telephone.in=" + DEFAULT_TELEPHONE + "," + UPDATED_TELEPHONE);

        // Get all the siteList where telephone equals to UPDATED_TELEPHONE
        defaultSiteShouldNotBeFound("telephone.in=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllSitesByTelephoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where telephone is not null
        defaultSiteShouldBeFound("telephone.specified=true");

        // Get all the siteList where telephone is null
        defaultSiteShouldNotBeFound("telephone.specified=false");
    }

    @Test
    @Transactional
    void getAllSitesByTelephoneContainsSomething() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where telephone contains DEFAULT_TELEPHONE
        defaultSiteShouldBeFound("telephone.contains=" + DEFAULT_TELEPHONE);

        // Get all the siteList where telephone contains UPDATED_TELEPHONE
        defaultSiteShouldNotBeFound("telephone.contains=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllSitesByTelephoneNotContainsSomething() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where telephone does not contain DEFAULT_TELEPHONE
        defaultSiteShouldNotBeFound("telephone.doesNotContain=" + DEFAULT_TELEPHONE);

        // Get all the siteList where telephone does not contain UPDATED_TELEPHONE
        defaultSiteShouldBeFound("telephone.doesNotContain=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllSitesByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where address equals to DEFAULT_ADDRESS
        defaultSiteShouldBeFound("address.equals=" + DEFAULT_ADDRESS);

        // Get all the siteList where address equals to UPDATED_ADDRESS
        defaultSiteShouldNotBeFound("address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllSitesByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where address in DEFAULT_ADDRESS or UPDATED_ADDRESS
        defaultSiteShouldBeFound("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS);

        // Get all the siteList where address equals to UPDATED_ADDRESS
        defaultSiteShouldNotBeFound("address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllSitesByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where address is not null
        defaultSiteShouldBeFound("address.specified=true");

        // Get all the siteList where address is null
        defaultSiteShouldNotBeFound("address.specified=false");
    }

    @Test
    @Transactional
    void getAllSitesByAddressContainsSomething() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where address contains DEFAULT_ADDRESS
        defaultSiteShouldBeFound("address.contains=" + DEFAULT_ADDRESS);

        // Get all the siteList where address contains UPDATED_ADDRESS
        defaultSiteShouldNotBeFound("address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllSitesByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where address does not contain DEFAULT_ADDRESS
        defaultSiteShouldNotBeFound("address.doesNotContain=" + DEFAULT_ADDRESS);

        // Get all the siteList where address does not contain UPDATED_ADDRESS
        defaultSiteShouldBeFound("address.doesNotContain=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllSitesByPostalCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where postalCode equals to DEFAULT_POSTAL_CODE
        defaultSiteShouldBeFound("postalCode.equals=" + DEFAULT_POSTAL_CODE);

        // Get all the siteList where postalCode equals to UPDATED_POSTAL_CODE
        defaultSiteShouldNotBeFound("postalCode.equals=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllSitesByPostalCodeIsInShouldWork() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where postalCode in DEFAULT_POSTAL_CODE or UPDATED_POSTAL_CODE
        defaultSiteShouldBeFound("postalCode.in=" + DEFAULT_POSTAL_CODE + "," + UPDATED_POSTAL_CODE);

        // Get all the siteList where postalCode equals to UPDATED_POSTAL_CODE
        defaultSiteShouldNotBeFound("postalCode.in=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllSitesByPostalCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where postalCode is not null
        defaultSiteShouldBeFound("postalCode.specified=true");

        // Get all the siteList where postalCode is null
        defaultSiteShouldNotBeFound("postalCode.specified=false");
    }

    @Test
    @Transactional
    void getAllSitesByPostalCodeContainsSomething() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where postalCode contains DEFAULT_POSTAL_CODE
        defaultSiteShouldBeFound("postalCode.contains=" + DEFAULT_POSTAL_CODE);

        // Get all the siteList where postalCode contains UPDATED_POSTAL_CODE
        defaultSiteShouldNotBeFound("postalCode.contains=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllSitesByPostalCodeNotContainsSomething() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where postalCode does not contain DEFAULT_POSTAL_CODE
        defaultSiteShouldNotBeFound("postalCode.doesNotContain=" + DEFAULT_POSTAL_CODE);

        // Get all the siteList where postalCode does not contain UPDATED_POSTAL_CODE
        defaultSiteShouldBeFound("postalCode.doesNotContain=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllSitesByRegionIsEqualToSomething() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where region equals to DEFAULT_REGION
        defaultSiteShouldBeFound("region.equals=" + DEFAULT_REGION);

        // Get all the siteList where region equals to UPDATED_REGION
        defaultSiteShouldNotBeFound("region.equals=" + UPDATED_REGION);
    }

    @Test
    @Transactional
    void getAllSitesByRegionIsInShouldWork() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where region in DEFAULT_REGION or UPDATED_REGION
        defaultSiteShouldBeFound("region.in=" + DEFAULT_REGION + "," + UPDATED_REGION);

        // Get all the siteList where region equals to UPDATED_REGION
        defaultSiteShouldNotBeFound("region.in=" + UPDATED_REGION);
    }

    @Test
    @Transactional
    void getAllSitesByRegionIsNullOrNotNull() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where region is not null
        defaultSiteShouldBeFound("region.specified=true");

        // Get all the siteList where region is null
        defaultSiteShouldNotBeFound("region.specified=false");
    }

    @Test
    @Transactional
    void getAllSitesByRegionContainsSomething() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where region contains DEFAULT_REGION
        defaultSiteShouldBeFound("region.contains=" + DEFAULT_REGION);

        // Get all the siteList where region contains UPDATED_REGION
        defaultSiteShouldNotBeFound("region.contains=" + UPDATED_REGION);
    }

    @Test
    @Transactional
    void getAllSitesByRegionNotContainsSomething() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where region does not contain DEFAULT_REGION
        defaultSiteShouldNotBeFound("region.doesNotContain=" + DEFAULT_REGION);

        // Get all the siteList where region does not contain UPDATED_REGION
        defaultSiteShouldBeFound("region.doesNotContain=" + UPDATED_REGION);
    }

    @Test
    @Transactional
    void getAllSitesByLocalityIsEqualToSomething() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where locality equals to DEFAULT_LOCALITY
        defaultSiteShouldBeFound("locality.equals=" + DEFAULT_LOCALITY);

        // Get all the siteList where locality equals to UPDATED_LOCALITY
        defaultSiteShouldNotBeFound("locality.equals=" + UPDATED_LOCALITY);
    }

    @Test
    @Transactional
    void getAllSitesByLocalityIsInShouldWork() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where locality in DEFAULT_LOCALITY or UPDATED_LOCALITY
        defaultSiteShouldBeFound("locality.in=" + DEFAULT_LOCALITY + "," + UPDATED_LOCALITY);

        // Get all the siteList where locality equals to UPDATED_LOCALITY
        defaultSiteShouldNotBeFound("locality.in=" + UPDATED_LOCALITY);
    }

    @Test
    @Transactional
    void getAllSitesByLocalityIsNullOrNotNull() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where locality is not null
        defaultSiteShouldBeFound("locality.specified=true");

        // Get all the siteList where locality is null
        defaultSiteShouldNotBeFound("locality.specified=false");
    }

    @Test
    @Transactional
    void getAllSitesByLocalityContainsSomething() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where locality contains DEFAULT_LOCALITY
        defaultSiteShouldBeFound("locality.contains=" + DEFAULT_LOCALITY);

        // Get all the siteList where locality contains UPDATED_LOCALITY
        defaultSiteShouldNotBeFound("locality.contains=" + UPDATED_LOCALITY);
    }

    @Test
    @Transactional
    void getAllSitesByLocalityNotContainsSomething() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where locality does not contain DEFAULT_LOCALITY
        defaultSiteShouldNotBeFound("locality.doesNotContain=" + DEFAULT_LOCALITY);

        // Get all the siteList where locality does not contain UPDATED_LOCALITY
        defaultSiteShouldBeFound("locality.doesNotContain=" + UPDATED_LOCALITY);
    }

    @Test
    @Transactional
    void getAllSitesByCountryIsEqualToSomething() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where country equals to DEFAULT_COUNTRY
        defaultSiteShouldBeFound("country.equals=" + DEFAULT_COUNTRY);

        // Get all the siteList where country equals to UPDATED_COUNTRY
        defaultSiteShouldNotBeFound("country.equals=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllSitesByCountryIsInShouldWork() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where country in DEFAULT_COUNTRY or UPDATED_COUNTRY
        defaultSiteShouldBeFound("country.in=" + DEFAULT_COUNTRY + "," + UPDATED_COUNTRY);

        // Get all the siteList where country equals to UPDATED_COUNTRY
        defaultSiteShouldNotBeFound("country.in=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllSitesByCountryIsNullOrNotNull() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where country is not null
        defaultSiteShouldBeFound("country.specified=true");

        // Get all the siteList where country is null
        defaultSiteShouldNotBeFound("country.specified=false");
    }

    @Test
    @Transactional
    void getAllSitesByCountryContainsSomething() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where country contains DEFAULT_COUNTRY
        defaultSiteShouldBeFound("country.contains=" + DEFAULT_COUNTRY);

        // Get all the siteList where country contains UPDATED_COUNTRY
        defaultSiteShouldNotBeFound("country.contains=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllSitesByCountryNotContainsSomething() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where country does not contain DEFAULT_COUNTRY
        defaultSiteShouldNotBeFound("country.doesNotContain=" + DEFAULT_COUNTRY);

        // Get all the siteList where country does not contain UPDATED_COUNTRY
        defaultSiteShouldBeFound("country.doesNotContain=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllSitesByLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where location equals to DEFAULT_LOCATION
        defaultSiteShouldBeFound("location.equals=" + DEFAULT_LOCATION);

        // Get all the siteList where location equals to UPDATED_LOCATION
        defaultSiteShouldNotBeFound("location.equals=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllSitesByLocationIsInShouldWork() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where location in DEFAULT_LOCATION or UPDATED_LOCATION
        defaultSiteShouldBeFound("location.in=" + DEFAULT_LOCATION + "," + UPDATED_LOCATION);

        // Get all the siteList where location equals to UPDATED_LOCATION
        defaultSiteShouldNotBeFound("location.in=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllSitesByLocationIsNullOrNotNull() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where location is not null
        defaultSiteShouldBeFound("location.specified=true");

        // Get all the siteList where location is null
        defaultSiteShouldNotBeFound("location.specified=false");
    }

    @Test
    @Transactional
    void getAllSitesByLocationContainsSomething() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where location contains DEFAULT_LOCATION
        defaultSiteShouldBeFound("location.contains=" + DEFAULT_LOCATION);

        // Get all the siteList where location contains UPDATED_LOCATION
        defaultSiteShouldNotBeFound("location.contains=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllSitesByLocationNotContainsSomething() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where location does not contain DEFAULT_LOCATION
        defaultSiteShouldNotBeFound("location.doesNotContain=" + DEFAULT_LOCATION);

        // Get all the siteList where location does not contain UPDATED_LOCATION
        defaultSiteShouldBeFound("location.doesNotContain=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllSitesByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where createdAt equals to DEFAULT_CREATED_AT
        defaultSiteShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the siteList where createdAt equals to UPDATED_CREATED_AT
        defaultSiteShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllSitesByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultSiteShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the siteList where createdAt equals to UPDATED_CREATED_AT
        defaultSiteShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllSitesByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where createdAt is not null
        defaultSiteShouldBeFound("createdAt.specified=true");

        // Get all the siteList where createdAt is null
        defaultSiteShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllSitesByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where createdAt is greater than or equal to DEFAULT_CREATED_AT
        defaultSiteShouldBeFound("createdAt.greaterThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the siteList where createdAt is greater than or equal to UPDATED_CREATED_AT
        defaultSiteShouldNotBeFound("createdAt.greaterThanOrEqual=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllSitesByCreatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where createdAt is less than or equal to DEFAULT_CREATED_AT
        defaultSiteShouldBeFound("createdAt.lessThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the siteList where createdAt is less than or equal to SMALLER_CREATED_AT
        defaultSiteShouldNotBeFound("createdAt.lessThanOrEqual=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllSitesByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where createdAt is less than DEFAULT_CREATED_AT
        defaultSiteShouldNotBeFound("createdAt.lessThan=" + DEFAULT_CREATED_AT);

        // Get all the siteList where createdAt is less than UPDATED_CREATED_AT
        defaultSiteShouldBeFound("createdAt.lessThan=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllSitesByCreatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where createdAt is greater than DEFAULT_CREATED_AT
        defaultSiteShouldNotBeFound("createdAt.greaterThan=" + DEFAULT_CREATED_AT);

        // Get all the siteList where createdAt is greater than SMALLER_CREATED_AT
        defaultSiteShouldBeFound("createdAt.greaterThan=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllSitesByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultSiteShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the siteList where updatedAt equals to UPDATED_UPDATED_AT
        defaultSiteShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllSitesByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultSiteShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the siteList where updatedAt equals to UPDATED_UPDATED_AT
        defaultSiteShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllSitesByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where updatedAt is not null
        defaultSiteShouldBeFound("updatedAt.specified=true");

        // Get all the siteList where updatedAt is null
        defaultSiteShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllSitesByUpdatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where updatedAt is greater than or equal to DEFAULT_UPDATED_AT
        defaultSiteShouldBeFound("updatedAt.greaterThanOrEqual=" + DEFAULT_UPDATED_AT);

        // Get all the siteList where updatedAt is greater than or equal to UPDATED_UPDATED_AT
        defaultSiteShouldNotBeFound("updatedAt.greaterThanOrEqual=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllSitesByUpdatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where updatedAt is less than or equal to DEFAULT_UPDATED_AT
        defaultSiteShouldBeFound("updatedAt.lessThanOrEqual=" + DEFAULT_UPDATED_AT);

        // Get all the siteList where updatedAt is less than or equal to SMALLER_UPDATED_AT
        defaultSiteShouldNotBeFound("updatedAt.lessThanOrEqual=" + SMALLER_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllSitesByUpdatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where updatedAt is less than DEFAULT_UPDATED_AT
        defaultSiteShouldNotBeFound("updatedAt.lessThan=" + DEFAULT_UPDATED_AT);

        // Get all the siteList where updatedAt is less than UPDATED_UPDATED_AT
        defaultSiteShouldBeFound("updatedAt.lessThan=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllSitesByUpdatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList where updatedAt is greater than DEFAULT_UPDATED_AT
        defaultSiteShouldNotBeFound("updatedAt.greaterThan=" + DEFAULT_UPDATED_AT);

        // Get all the siteList where updatedAt is greater than SMALLER_UPDATED_AT
        defaultSiteShouldBeFound("updatedAt.greaterThan=" + SMALLER_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllSitesByOrganizationIsEqualToSomething() throws Exception {
        Organization organization;
        if (TestUtil.findAll(em, Organization.class).isEmpty()) {
            siteRepository.saveAndFlush(site);
            organization = OrganizationResourceIT.createEntity(em);
        } else {
            organization = TestUtil.findAll(em, Organization.class).get(0);
        }
        em.persist(organization);
        em.flush();
        site.setOrganization(organization);
        siteRepository.saveAndFlush(site);
        Long organizationId = organization.getId();
        // Get all the siteList where organization equals to organizationId
        defaultSiteShouldBeFound("organizationId.equals=" + organizationId);

        // Get all the siteList where organization equals to (organizationId + 1)
        defaultSiteShouldNotBeFound("organizationId.equals=" + (organizationId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSiteShouldBeFound(String filter) throws Exception {
        restSiteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(site.getId().intValue())))
            .andExpect(jsonPath("$.[*].reference").value(hasItem(DEFAULT_REFERENCE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE)))
            .andExpect(jsonPath("$.[*].region").value(hasItem(DEFAULT_REGION)))
            .andExpect(jsonPath("$.[*].locality").value(hasItem(DEFAULT_LOCALITY)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));

        // Check, that the count call also returns 1
        restSiteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSiteShouldNotBeFound(String filter) throws Exception {
        restSiteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSiteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSite() throws Exception {
        // Get the site
        restSiteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSite() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        int databaseSizeBeforeUpdate = siteRepository.findAll().size();

        // Update the site
        Site updatedSite = siteRepository.findById(site.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSite are not directly saved in db
        em.detach(updatedSite);
        updatedSite
            .reference(UPDATED_REFERENCE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .email(UPDATED_EMAIL)
            .telephone(UPDATED_TELEPHONE)
            .address(UPDATED_ADDRESS)
            .postalCode(UPDATED_POSTAL_CODE)
            .region(UPDATED_REGION)
            .locality(UPDATED_LOCALITY)
            .country(UPDATED_COUNTRY)
            .location(UPDATED_LOCATION)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        SiteDTO siteDTO = siteMapper.toDto(updatedSite);

        restSiteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, siteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(siteDTO))
            )
            .andExpect(status().isOk());

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll();
        assertThat(siteList).hasSize(databaseSizeBeforeUpdate);
        Site testSite = siteList.get(siteList.size() - 1);
        assertThat(testSite.getReference()).isEqualTo(UPDATED_REFERENCE);
        assertThat(testSite.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSite.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSite.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testSite.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testSite.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testSite.getPostalCode()).isEqualTo(UPDATED_POSTAL_CODE);
        assertThat(testSite.getRegion()).isEqualTo(UPDATED_REGION);
        assertThat(testSite.getLocality()).isEqualTo(UPDATED_LOCALITY);
        assertThat(testSite.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testSite.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testSite.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testSite.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingSite() throws Exception {
        int databaseSizeBeforeUpdate = siteRepository.findAll().size();
        site.setId(longCount.incrementAndGet());

        // Create the Site
        SiteDTO siteDTO = siteMapper.toDto(site);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSiteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, siteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(siteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll();
        assertThat(siteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSite() throws Exception {
        int databaseSizeBeforeUpdate = siteRepository.findAll().size();
        site.setId(longCount.incrementAndGet());

        // Create the Site
        SiteDTO siteDTO = siteMapper.toDto(site);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSiteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(siteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll();
        assertThat(siteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSite() throws Exception {
        int databaseSizeBeforeUpdate = siteRepository.findAll().size();
        site.setId(longCount.incrementAndGet());

        // Create the Site
        SiteDTO siteDTO = siteMapper.toDto(site);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSiteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(siteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll();
        assertThat(siteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSiteWithPatch() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        int databaseSizeBeforeUpdate = siteRepository.findAll().size();

        // Update the site using partial update
        Site partialUpdatedSite = new Site();
        partialUpdatedSite.setId(site.getId());

        partialUpdatedSite
            .reference(UPDATED_REFERENCE)
            .name(UPDATED_NAME)
            .email(UPDATED_EMAIL)
            .address(UPDATED_ADDRESS)
            .locality(UPDATED_LOCALITY);

        restSiteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSite.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSite))
            )
            .andExpect(status().isOk());

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll();
        assertThat(siteList).hasSize(databaseSizeBeforeUpdate);
        Site testSite = siteList.get(siteList.size() - 1);
        assertThat(testSite.getReference()).isEqualTo(UPDATED_REFERENCE);
        assertThat(testSite.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSite.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSite.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testSite.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
        assertThat(testSite.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testSite.getPostalCode()).isEqualTo(DEFAULT_POSTAL_CODE);
        assertThat(testSite.getRegion()).isEqualTo(DEFAULT_REGION);
        assertThat(testSite.getLocality()).isEqualTo(UPDATED_LOCALITY);
        assertThat(testSite.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testSite.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testSite.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testSite.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateSiteWithPatch() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        int databaseSizeBeforeUpdate = siteRepository.findAll().size();

        // Update the site using partial update
        Site partialUpdatedSite = new Site();
        partialUpdatedSite.setId(site.getId());

        partialUpdatedSite
            .reference(UPDATED_REFERENCE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .email(UPDATED_EMAIL)
            .telephone(UPDATED_TELEPHONE)
            .address(UPDATED_ADDRESS)
            .postalCode(UPDATED_POSTAL_CODE)
            .region(UPDATED_REGION)
            .locality(UPDATED_LOCALITY)
            .country(UPDATED_COUNTRY)
            .location(UPDATED_LOCATION)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restSiteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSite.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSite))
            )
            .andExpect(status().isOk());

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll();
        assertThat(siteList).hasSize(databaseSizeBeforeUpdate);
        Site testSite = siteList.get(siteList.size() - 1);
        assertThat(testSite.getReference()).isEqualTo(UPDATED_REFERENCE);
        assertThat(testSite.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSite.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSite.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testSite.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testSite.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testSite.getPostalCode()).isEqualTo(UPDATED_POSTAL_CODE);
        assertThat(testSite.getRegion()).isEqualTo(UPDATED_REGION);
        assertThat(testSite.getLocality()).isEqualTo(UPDATED_LOCALITY);
        assertThat(testSite.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testSite.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testSite.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testSite.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingSite() throws Exception {
        int databaseSizeBeforeUpdate = siteRepository.findAll().size();
        site.setId(longCount.incrementAndGet());

        // Create the Site
        SiteDTO siteDTO = siteMapper.toDto(site);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSiteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, siteDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(siteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll();
        assertThat(siteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSite() throws Exception {
        int databaseSizeBeforeUpdate = siteRepository.findAll().size();
        site.setId(longCount.incrementAndGet());

        // Create the Site
        SiteDTO siteDTO = siteMapper.toDto(site);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSiteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(siteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll();
        assertThat(siteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSite() throws Exception {
        int databaseSizeBeforeUpdate = siteRepository.findAll().size();
        site.setId(longCount.incrementAndGet());

        // Create the Site
        SiteDTO siteDTO = siteMapper.toDto(site);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSiteMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(siteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll();
        assertThat(siteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSite() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        int databaseSizeBeforeDelete = siteRepository.findAll().size();

        // Delete the site
        restSiteMockMvc
            .perform(delete(ENTITY_API_URL_ID, site.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Site> siteList = siteRepository.findAll();
        assertThat(siteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
