package com.sothis.tech.web.rest;

import static com.sothis.tech.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sothis.tech.IntegrationTest;
import com.sothis.tech.domain.Machine;
import com.sothis.tech.domain.Organization;
import com.sothis.tech.repository.OrganizationRepository;
import com.sothis.tech.service.dto.OrganizationDTO;
import com.sothis.tech.service.mapper.OrganizationMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link OrganizationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrganizationResourceIT {

    private static final String DEFAULT_REFERENCE = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LEGAL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LEGAL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_TAX_ID = "AAAAAAAAAA";
    private static final String UPDATED_TAX_ID = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_TELEPHONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEPHONE = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

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

    private static final String ENTITY_API_URL = "/api/organizations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private OrganizationMapper organizationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrganizationMockMvc;

    private Organization organization;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Organization createEntity(EntityManager em) {
        Organization organization = new Organization()
            .reference(DEFAULT_REFERENCE)
            .name(DEFAULT_NAME)
            .legalName(DEFAULT_LEGAL_NAME)
            .description(DEFAULT_DESCRIPTION)
            .taxId(DEFAULT_TAX_ID)
            .email(DEFAULT_EMAIL)
            .telephone(DEFAULT_TELEPHONE)
            .url(DEFAULT_URL)
            .address(DEFAULT_ADDRESS)
            .postalCode(DEFAULT_POSTAL_CODE)
            .region(DEFAULT_REGION)
            .locality(DEFAULT_LOCALITY)
            .country(DEFAULT_COUNTRY)
            .location(DEFAULT_LOCATION)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return organization;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Organization createUpdatedEntity(EntityManager em) {
        Organization organization = new Organization()
            .reference(UPDATED_REFERENCE)
            .name(UPDATED_NAME)
            .legalName(UPDATED_LEGAL_NAME)
            .description(UPDATED_DESCRIPTION)
            .taxId(UPDATED_TAX_ID)
            .email(UPDATED_EMAIL)
            .telephone(UPDATED_TELEPHONE)
            .url(UPDATED_URL)
            .address(UPDATED_ADDRESS)
            .postalCode(UPDATED_POSTAL_CODE)
            .region(UPDATED_REGION)
            .locality(UPDATED_LOCALITY)
            .country(UPDATED_COUNTRY)
            .location(UPDATED_LOCATION)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return organization;
    }

    @BeforeEach
    public void initTest() {
        organization = createEntity(em);
    }

    @Test
    @Transactional
    void createOrganization() throws Exception {
        int databaseSizeBeforeCreate = organizationRepository.findAll().size();
        // Create the Organization
        OrganizationDTO organizationDTO = organizationMapper.toDto(organization);
        restOrganizationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(organizationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Organization in the database
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeCreate + 1);
        Organization testOrganization = organizationList.get(organizationList.size() - 1);
        assertThat(testOrganization.getReference()).isEqualTo(DEFAULT_REFERENCE);
        assertThat(testOrganization.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOrganization.getLegalName()).isEqualTo(DEFAULT_LEGAL_NAME);
        assertThat(testOrganization.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testOrganization.getTaxId()).isEqualTo(DEFAULT_TAX_ID);
        assertThat(testOrganization.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testOrganization.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
        assertThat(testOrganization.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testOrganization.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testOrganization.getPostalCode()).isEqualTo(DEFAULT_POSTAL_CODE);
        assertThat(testOrganization.getRegion()).isEqualTo(DEFAULT_REGION);
        assertThat(testOrganization.getLocality()).isEqualTo(DEFAULT_LOCALITY);
        assertThat(testOrganization.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testOrganization.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testOrganization.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testOrganization.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createOrganizationWithExistingId() throws Exception {
        // Create the Organization with an existing ID
        organization.setId(1L);
        OrganizationDTO organizationDTO = organizationMapper.toDto(organization);

        int databaseSizeBeforeCreate = organizationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrganizationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(organizationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Organization in the database
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkReferenceIsRequired() throws Exception {
        int databaseSizeBeforeTest = organizationRepository.findAll().size();
        // set the field null
        organization.setReference(null);

        // Create the Organization, which fails.
        OrganizationDTO organizationDTO = organizationMapper.toDto(organization);

        restOrganizationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(organizationDTO))
            )
            .andExpect(status().isBadRequest());

        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = organizationRepository.findAll().size();
        // set the field null
        organization.setName(null);

        // Create the Organization, which fails.
        OrganizationDTO organizationDTO = organizationMapper.toDto(organization);

        restOrganizationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(organizationDTO))
            )
            .andExpect(status().isBadRequest());

        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOrganizations() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList
        restOrganizationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(organization.getId().intValue())))
            .andExpect(jsonPath("$.[*].reference").value(hasItem(DEFAULT_REFERENCE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].legalName").value(hasItem(DEFAULT_LEGAL_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].taxId").value(hasItem(DEFAULT_TAX_ID)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE)))
            .andExpect(jsonPath("$.[*].region").value(hasItem(DEFAULT_REGION)))
            .andExpect(jsonPath("$.[*].locality").value(hasItem(DEFAULT_LOCALITY)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));
    }

    @Test
    @Transactional
    void getOrganization() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get the organization
        restOrganizationMockMvc
            .perform(get(ENTITY_API_URL_ID, organization.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(organization.getId().intValue()))
            .andExpect(jsonPath("$.reference").value(DEFAULT_REFERENCE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.legalName").value(DEFAULT_LEGAL_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.taxId").value(DEFAULT_TAX_ID))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL))
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
    void getOrganizationsByIdFiltering() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        Long id = organization.getId();

        defaultOrganizationShouldBeFound("id.equals=" + id);
        defaultOrganizationShouldNotBeFound("id.notEquals=" + id);

        defaultOrganizationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultOrganizationShouldNotBeFound("id.greaterThan=" + id);

        defaultOrganizationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultOrganizationShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOrganizationsByReferenceIsEqualToSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where reference equals to DEFAULT_REFERENCE
        defaultOrganizationShouldBeFound("reference.equals=" + DEFAULT_REFERENCE);

        // Get all the organizationList where reference equals to UPDATED_REFERENCE
        defaultOrganizationShouldNotBeFound("reference.equals=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    void getAllOrganizationsByReferenceIsInShouldWork() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where reference in DEFAULT_REFERENCE or UPDATED_REFERENCE
        defaultOrganizationShouldBeFound("reference.in=" + DEFAULT_REFERENCE + "," + UPDATED_REFERENCE);

        // Get all the organizationList where reference equals to UPDATED_REFERENCE
        defaultOrganizationShouldNotBeFound("reference.in=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    void getAllOrganizationsByReferenceIsNullOrNotNull() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where reference is not null
        defaultOrganizationShouldBeFound("reference.specified=true");

        // Get all the organizationList where reference is null
        defaultOrganizationShouldNotBeFound("reference.specified=false");
    }

    @Test
    @Transactional
    void getAllOrganizationsByReferenceContainsSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where reference contains DEFAULT_REFERENCE
        defaultOrganizationShouldBeFound("reference.contains=" + DEFAULT_REFERENCE);

        // Get all the organizationList where reference contains UPDATED_REFERENCE
        defaultOrganizationShouldNotBeFound("reference.contains=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    void getAllOrganizationsByReferenceNotContainsSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where reference does not contain DEFAULT_REFERENCE
        defaultOrganizationShouldNotBeFound("reference.doesNotContain=" + DEFAULT_REFERENCE);

        // Get all the organizationList where reference does not contain UPDATED_REFERENCE
        defaultOrganizationShouldBeFound("reference.doesNotContain=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    void getAllOrganizationsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where name equals to DEFAULT_NAME
        defaultOrganizationShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the organizationList where name equals to UPDATED_NAME
        defaultOrganizationShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllOrganizationsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where name in DEFAULT_NAME or UPDATED_NAME
        defaultOrganizationShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the organizationList where name equals to UPDATED_NAME
        defaultOrganizationShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllOrganizationsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where name is not null
        defaultOrganizationShouldBeFound("name.specified=true");

        // Get all the organizationList where name is null
        defaultOrganizationShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllOrganizationsByNameContainsSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where name contains DEFAULT_NAME
        defaultOrganizationShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the organizationList where name contains UPDATED_NAME
        defaultOrganizationShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllOrganizationsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where name does not contain DEFAULT_NAME
        defaultOrganizationShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the organizationList where name does not contain UPDATED_NAME
        defaultOrganizationShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllOrganizationsByLegalNameIsEqualToSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where legalName equals to DEFAULT_LEGAL_NAME
        defaultOrganizationShouldBeFound("legalName.equals=" + DEFAULT_LEGAL_NAME);

        // Get all the organizationList where legalName equals to UPDATED_LEGAL_NAME
        defaultOrganizationShouldNotBeFound("legalName.equals=" + UPDATED_LEGAL_NAME);
    }

    @Test
    @Transactional
    void getAllOrganizationsByLegalNameIsInShouldWork() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where legalName in DEFAULT_LEGAL_NAME or UPDATED_LEGAL_NAME
        defaultOrganizationShouldBeFound("legalName.in=" + DEFAULT_LEGAL_NAME + "," + UPDATED_LEGAL_NAME);

        // Get all the organizationList where legalName equals to UPDATED_LEGAL_NAME
        defaultOrganizationShouldNotBeFound("legalName.in=" + UPDATED_LEGAL_NAME);
    }

    @Test
    @Transactional
    void getAllOrganizationsByLegalNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where legalName is not null
        defaultOrganizationShouldBeFound("legalName.specified=true");

        // Get all the organizationList where legalName is null
        defaultOrganizationShouldNotBeFound("legalName.specified=false");
    }

    @Test
    @Transactional
    void getAllOrganizationsByLegalNameContainsSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where legalName contains DEFAULT_LEGAL_NAME
        defaultOrganizationShouldBeFound("legalName.contains=" + DEFAULT_LEGAL_NAME);

        // Get all the organizationList where legalName contains UPDATED_LEGAL_NAME
        defaultOrganizationShouldNotBeFound("legalName.contains=" + UPDATED_LEGAL_NAME);
    }

    @Test
    @Transactional
    void getAllOrganizationsByLegalNameNotContainsSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where legalName does not contain DEFAULT_LEGAL_NAME
        defaultOrganizationShouldNotBeFound("legalName.doesNotContain=" + DEFAULT_LEGAL_NAME);

        // Get all the organizationList where legalName does not contain UPDATED_LEGAL_NAME
        defaultOrganizationShouldBeFound("legalName.doesNotContain=" + UPDATED_LEGAL_NAME);
    }

    @Test
    @Transactional
    void getAllOrganizationsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where description equals to DEFAULT_DESCRIPTION
        defaultOrganizationShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the organizationList where description equals to UPDATED_DESCRIPTION
        defaultOrganizationShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllOrganizationsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultOrganizationShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the organizationList where description equals to UPDATED_DESCRIPTION
        defaultOrganizationShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllOrganizationsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where description is not null
        defaultOrganizationShouldBeFound("description.specified=true");

        // Get all the organizationList where description is null
        defaultOrganizationShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllOrganizationsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where description contains DEFAULT_DESCRIPTION
        defaultOrganizationShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the organizationList where description contains UPDATED_DESCRIPTION
        defaultOrganizationShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllOrganizationsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where description does not contain DEFAULT_DESCRIPTION
        defaultOrganizationShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the organizationList where description does not contain UPDATED_DESCRIPTION
        defaultOrganizationShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllOrganizationsByTaxIdIsEqualToSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where taxId equals to DEFAULT_TAX_ID
        defaultOrganizationShouldBeFound("taxId.equals=" + DEFAULT_TAX_ID);

        // Get all the organizationList where taxId equals to UPDATED_TAX_ID
        defaultOrganizationShouldNotBeFound("taxId.equals=" + UPDATED_TAX_ID);
    }

    @Test
    @Transactional
    void getAllOrganizationsByTaxIdIsInShouldWork() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where taxId in DEFAULT_TAX_ID or UPDATED_TAX_ID
        defaultOrganizationShouldBeFound("taxId.in=" + DEFAULT_TAX_ID + "," + UPDATED_TAX_ID);

        // Get all the organizationList where taxId equals to UPDATED_TAX_ID
        defaultOrganizationShouldNotBeFound("taxId.in=" + UPDATED_TAX_ID);
    }

    @Test
    @Transactional
    void getAllOrganizationsByTaxIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where taxId is not null
        defaultOrganizationShouldBeFound("taxId.specified=true");

        // Get all the organizationList where taxId is null
        defaultOrganizationShouldNotBeFound("taxId.specified=false");
    }

    @Test
    @Transactional
    void getAllOrganizationsByTaxIdContainsSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where taxId contains DEFAULT_TAX_ID
        defaultOrganizationShouldBeFound("taxId.contains=" + DEFAULT_TAX_ID);

        // Get all the organizationList where taxId contains UPDATED_TAX_ID
        defaultOrganizationShouldNotBeFound("taxId.contains=" + UPDATED_TAX_ID);
    }

    @Test
    @Transactional
    void getAllOrganizationsByTaxIdNotContainsSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where taxId does not contain DEFAULT_TAX_ID
        defaultOrganizationShouldNotBeFound("taxId.doesNotContain=" + DEFAULT_TAX_ID);

        // Get all the organizationList where taxId does not contain UPDATED_TAX_ID
        defaultOrganizationShouldBeFound("taxId.doesNotContain=" + UPDATED_TAX_ID);
    }

    @Test
    @Transactional
    void getAllOrganizationsByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where email equals to DEFAULT_EMAIL
        defaultOrganizationShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the organizationList where email equals to UPDATED_EMAIL
        defaultOrganizationShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllOrganizationsByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultOrganizationShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the organizationList where email equals to UPDATED_EMAIL
        defaultOrganizationShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllOrganizationsByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where email is not null
        defaultOrganizationShouldBeFound("email.specified=true");

        // Get all the organizationList where email is null
        defaultOrganizationShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllOrganizationsByEmailContainsSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where email contains DEFAULT_EMAIL
        defaultOrganizationShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the organizationList where email contains UPDATED_EMAIL
        defaultOrganizationShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllOrganizationsByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where email does not contain DEFAULT_EMAIL
        defaultOrganizationShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the organizationList where email does not contain UPDATED_EMAIL
        defaultOrganizationShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllOrganizationsByTelephoneIsEqualToSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where telephone equals to DEFAULT_TELEPHONE
        defaultOrganizationShouldBeFound("telephone.equals=" + DEFAULT_TELEPHONE);

        // Get all the organizationList where telephone equals to UPDATED_TELEPHONE
        defaultOrganizationShouldNotBeFound("telephone.equals=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllOrganizationsByTelephoneIsInShouldWork() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where telephone in DEFAULT_TELEPHONE or UPDATED_TELEPHONE
        defaultOrganizationShouldBeFound("telephone.in=" + DEFAULT_TELEPHONE + "," + UPDATED_TELEPHONE);

        // Get all the organizationList where telephone equals to UPDATED_TELEPHONE
        defaultOrganizationShouldNotBeFound("telephone.in=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllOrganizationsByTelephoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where telephone is not null
        defaultOrganizationShouldBeFound("telephone.specified=true");

        // Get all the organizationList where telephone is null
        defaultOrganizationShouldNotBeFound("telephone.specified=false");
    }

    @Test
    @Transactional
    void getAllOrganizationsByTelephoneContainsSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where telephone contains DEFAULT_TELEPHONE
        defaultOrganizationShouldBeFound("telephone.contains=" + DEFAULT_TELEPHONE);

        // Get all the organizationList where telephone contains UPDATED_TELEPHONE
        defaultOrganizationShouldNotBeFound("telephone.contains=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllOrganizationsByTelephoneNotContainsSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where telephone does not contain DEFAULT_TELEPHONE
        defaultOrganizationShouldNotBeFound("telephone.doesNotContain=" + DEFAULT_TELEPHONE);

        // Get all the organizationList where telephone does not contain UPDATED_TELEPHONE
        defaultOrganizationShouldBeFound("telephone.doesNotContain=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllOrganizationsByUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where url equals to DEFAULT_URL
        defaultOrganizationShouldBeFound("url.equals=" + DEFAULT_URL);

        // Get all the organizationList where url equals to UPDATED_URL
        defaultOrganizationShouldNotBeFound("url.equals=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllOrganizationsByUrlIsInShouldWork() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where url in DEFAULT_URL or UPDATED_URL
        defaultOrganizationShouldBeFound("url.in=" + DEFAULT_URL + "," + UPDATED_URL);

        // Get all the organizationList where url equals to UPDATED_URL
        defaultOrganizationShouldNotBeFound("url.in=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllOrganizationsByUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where url is not null
        defaultOrganizationShouldBeFound("url.specified=true");

        // Get all the organizationList where url is null
        defaultOrganizationShouldNotBeFound("url.specified=false");
    }

    @Test
    @Transactional
    void getAllOrganizationsByUrlContainsSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where url contains DEFAULT_URL
        defaultOrganizationShouldBeFound("url.contains=" + DEFAULT_URL);

        // Get all the organizationList where url contains UPDATED_URL
        defaultOrganizationShouldNotBeFound("url.contains=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllOrganizationsByUrlNotContainsSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where url does not contain DEFAULT_URL
        defaultOrganizationShouldNotBeFound("url.doesNotContain=" + DEFAULT_URL);

        // Get all the organizationList where url does not contain UPDATED_URL
        defaultOrganizationShouldBeFound("url.doesNotContain=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllOrganizationsByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where address equals to DEFAULT_ADDRESS
        defaultOrganizationShouldBeFound("address.equals=" + DEFAULT_ADDRESS);

        // Get all the organizationList where address equals to UPDATED_ADDRESS
        defaultOrganizationShouldNotBeFound("address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllOrganizationsByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where address in DEFAULT_ADDRESS or UPDATED_ADDRESS
        defaultOrganizationShouldBeFound("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS);

        // Get all the organizationList where address equals to UPDATED_ADDRESS
        defaultOrganizationShouldNotBeFound("address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllOrganizationsByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where address is not null
        defaultOrganizationShouldBeFound("address.specified=true");

        // Get all the organizationList where address is null
        defaultOrganizationShouldNotBeFound("address.specified=false");
    }

    @Test
    @Transactional
    void getAllOrganizationsByAddressContainsSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where address contains DEFAULT_ADDRESS
        defaultOrganizationShouldBeFound("address.contains=" + DEFAULT_ADDRESS);

        // Get all the organizationList where address contains UPDATED_ADDRESS
        defaultOrganizationShouldNotBeFound("address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllOrganizationsByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where address does not contain DEFAULT_ADDRESS
        defaultOrganizationShouldNotBeFound("address.doesNotContain=" + DEFAULT_ADDRESS);

        // Get all the organizationList where address does not contain UPDATED_ADDRESS
        defaultOrganizationShouldBeFound("address.doesNotContain=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllOrganizationsByPostalCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where postalCode equals to DEFAULT_POSTAL_CODE
        defaultOrganizationShouldBeFound("postalCode.equals=" + DEFAULT_POSTAL_CODE);

        // Get all the organizationList where postalCode equals to UPDATED_POSTAL_CODE
        defaultOrganizationShouldNotBeFound("postalCode.equals=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllOrganizationsByPostalCodeIsInShouldWork() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where postalCode in DEFAULT_POSTAL_CODE or UPDATED_POSTAL_CODE
        defaultOrganizationShouldBeFound("postalCode.in=" + DEFAULT_POSTAL_CODE + "," + UPDATED_POSTAL_CODE);

        // Get all the organizationList where postalCode equals to UPDATED_POSTAL_CODE
        defaultOrganizationShouldNotBeFound("postalCode.in=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllOrganizationsByPostalCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where postalCode is not null
        defaultOrganizationShouldBeFound("postalCode.specified=true");

        // Get all the organizationList where postalCode is null
        defaultOrganizationShouldNotBeFound("postalCode.specified=false");
    }

    @Test
    @Transactional
    void getAllOrganizationsByPostalCodeContainsSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where postalCode contains DEFAULT_POSTAL_CODE
        defaultOrganizationShouldBeFound("postalCode.contains=" + DEFAULT_POSTAL_CODE);

        // Get all the organizationList where postalCode contains UPDATED_POSTAL_CODE
        defaultOrganizationShouldNotBeFound("postalCode.contains=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllOrganizationsByPostalCodeNotContainsSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where postalCode does not contain DEFAULT_POSTAL_CODE
        defaultOrganizationShouldNotBeFound("postalCode.doesNotContain=" + DEFAULT_POSTAL_CODE);

        // Get all the organizationList where postalCode does not contain UPDATED_POSTAL_CODE
        defaultOrganizationShouldBeFound("postalCode.doesNotContain=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllOrganizationsByRegionIsEqualToSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where region equals to DEFAULT_REGION
        defaultOrganizationShouldBeFound("region.equals=" + DEFAULT_REGION);

        // Get all the organizationList where region equals to UPDATED_REGION
        defaultOrganizationShouldNotBeFound("region.equals=" + UPDATED_REGION);
    }

    @Test
    @Transactional
    void getAllOrganizationsByRegionIsInShouldWork() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where region in DEFAULT_REGION or UPDATED_REGION
        defaultOrganizationShouldBeFound("region.in=" + DEFAULT_REGION + "," + UPDATED_REGION);

        // Get all the organizationList where region equals to UPDATED_REGION
        defaultOrganizationShouldNotBeFound("region.in=" + UPDATED_REGION);
    }

    @Test
    @Transactional
    void getAllOrganizationsByRegionIsNullOrNotNull() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where region is not null
        defaultOrganizationShouldBeFound("region.specified=true");

        // Get all the organizationList where region is null
        defaultOrganizationShouldNotBeFound("region.specified=false");
    }

    @Test
    @Transactional
    void getAllOrganizationsByRegionContainsSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where region contains DEFAULT_REGION
        defaultOrganizationShouldBeFound("region.contains=" + DEFAULT_REGION);

        // Get all the organizationList where region contains UPDATED_REGION
        defaultOrganizationShouldNotBeFound("region.contains=" + UPDATED_REGION);
    }

    @Test
    @Transactional
    void getAllOrganizationsByRegionNotContainsSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where region does not contain DEFAULT_REGION
        defaultOrganizationShouldNotBeFound("region.doesNotContain=" + DEFAULT_REGION);

        // Get all the organizationList where region does not contain UPDATED_REGION
        defaultOrganizationShouldBeFound("region.doesNotContain=" + UPDATED_REGION);
    }

    @Test
    @Transactional
    void getAllOrganizationsByLocalityIsEqualToSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where locality equals to DEFAULT_LOCALITY
        defaultOrganizationShouldBeFound("locality.equals=" + DEFAULT_LOCALITY);

        // Get all the organizationList where locality equals to UPDATED_LOCALITY
        defaultOrganizationShouldNotBeFound("locality.equals=" + UPDATED_LOCALITY);
    }

    @Test
    @Transactional
    void getAllOrganizationsByLocalityIsInShouldWork() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where locality in DEFAULT_LOCALITY or UPDATED_LOCALITY
        defaultOrganizationShouldBeFound("locality.in=" + DEFAULT_LOCALITY + "," + UPDATED_LOCALITY);

        // Get all the organizationList where locality equals to UPDATED_LOCALITY
        defaultOrganizationShouldNotBeFound("locality.in=" + UPDATED_LOCALITY);
    }

    @Test
    @Transactional
    void getAllOrganizationsByLocalityIsNullOrNotNull() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where locality is not null
        defaultOrganizationShouldBeFound("locality.specified=true");

        // Get all the organizationList where locality is null
        defaultOrganizationShouldNotBeFound("locality.specified=false");
    }

    @Test
    @Transactional
    void getAllOrganizationsByLocalityContainsSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where locality contains DEFAULT_LOCALITY
        defaultOrganizationShouldBeFound("locality.contains=" + DEFAULT_LOCALITY);

        // Get all the organizationList where locality contains UPDATED_LOCALITY
        defaultOrganizationShouldNotBeFound("locality.contains=" + UPDATED_LOCALITY);
    }

    @Test
    @Transactional
    void getAllOrganizationsByLocalityNotContainsSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where locality does not contain DEFAULT_LOCALITY
        defaultOrganizationShouldNotBeFound("locality.doesNotContain=" + DEFAULT_LOCALITY);

        // Get all the organizationList where locality does not contain UPDATED_LOCALITY
        defaultOrganizationShouldBeFound("locality.doesNotContain=" + UPDATED_LOCALITY);
    }

    @Test
    @Transactional
    void getAllOrganizationsByCountryIsEqualToSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where country equals to DEFAULT_COUNTRY
        defaultOrganizationShouldBeFound("country.equals=" + DEFAULT_COUNTRY);

        // Get all the organizationList where country equals to UPDATED_COUNTRY
        defaultOrganizationShouldNotBeFound("country.equals=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllOrganizationsByCountryIsInShouldWork() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where country in DEFAULT_COUNTRY or UPDATED_COUNTRY
        defaultOrganizationShouldBeFound("country.in=" + DEFAULT_COUNTRY + "," + UPDATED_COUNTRY);

        // Get all the organizationList where country equals to UPDATED_COUNTRY
        defaultOrganizationShouldNotBeFound("country.in=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllOrganizationsByCountryIsNullOrNotNull() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where country is not null
        defaultOrganizationShouldBeFound("country.specified=true");

        // Get all the organizationList where country is null
        defaultOrganizationShouldNotBeFound("country.specified=false");
    }

    @Test
    @Transactional
    void getAllOrganizationsByCountryContainsSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where country contains DEFAULT_COUNTRY
        defaultOrganizationShouldBeFound("country.contains=" + DEFAULT_COUNTRY);

        // Get all the organizationList where country contains UPDATED_COUNTRY
        defaultOrganizationShouldNotBeFound("country.contains=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllOrganizationsByCountryNotContainsSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where country does not contain DEFAULT_COUNTRY
        defaultOrganizationShouldNotBeFound("country.doesNotContain=" + DEFAULT_COUNTRY);

        // Get all the organizationList where country does not contain UPDATED_COUNTRY
        defaultOrganizationShouldBeFound("country.doesNotContain=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllOrganizationsByLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where location equals to DEFAULT_LOCATION
        defaultOrganizationShouldBeFound("location.equals=" + DEFAULT_LOCATION);

        // Get all the organizationList where location equals to UPDATED_LOCATION
        defaultOrganizationShouldNotBeFound("location.equals=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllOrganizationsByLocationIsInShouldWork() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where location in DEFAULT_LOCATION or UPDATED_LOCATION
        defaultOrganizationShouldBeFound("location.in=" + DEFAULT_LOCATION + "," + UPDATED_LOCATION);

        // Get all the organizationList where location equals to UPDATED_LOCATION
        defaultOrganizationShouldNotBeFound("location.in=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllOrganizationsByLocationIsNullOrNotNull() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where location is not null
        defaultOrganizationShouldBeFound("location.specified=true");

        // Get all the organizationList where location is null
        defaultOrganizationShouldNotBeFound("location.specified=false");
    }

    @Test
    @Transactional
    void getAllOrganizationsByLocationContainsSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where location contains DEFAULT_LOCATION
        defaultOrganizationShouldBeFound("location.contains=" + DEFAULT_LOCATION);

        // Get all the organizationList where location contains UPDATED_LOCATION
        defaultOrganizationShouldNotBeFound("location.contains=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllOrganizationsByLocationNotContainsSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where location does not contain DEFAULT_LOCATION
        defaultOrganizationShouldNotBeFound("location.doesNotContain=" + DEFAULT_LOCATION);

        // Get all the organizationList where location does not contain UPDATED_LOCATION
        defaultOrganizationShouldBeFound("location.doesNotContain=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllOrganizationsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where createdAt equals to DEFAULT_CREATED_AT
        defaultOrganizationShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the organizationList where createdAt equals to UPDATED_CREATED_AT
        defaultOrganizationShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllOrganizationsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultOrganizationShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the organizationList where createdAt equals to UPDATED_CREATED_AT
        defaultOrganizationShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllOrganizationsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where createdAt is not null
        defaultOrganizationShouldBeFound("createdAt.specified=true");

        // Get all the organizationList where createdAt is null
        defaultOrganizationShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllOrganizationsByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where createdAt is greater than or equal to DEFAULT_CREATED_AT
        defaultOrganizationShouldBeFound("createdAt.greaterThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the organizationList where createdAt is greater than or equal to UPDATED_CREATED_AT
        defaultOrganizationShouldNotBeFound("createdAt.greaterThanOrEqual=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllOrganizationsByCreatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where createdAt is less than or equal to DEFAULT_CREATED_AT
        defaultOrganizationShouldBeFound("createdAt.lessThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the organizationList where createdAt is less than or equal to SMALLER_CREATED_AT
        defaultOrganizationShouldNotBeFound("createdAt.lessThanOrEqual=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllOrganizationsByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where createdAt is less than DEFAULT_CREATED_AT
        defaultOrganizationShouldNotBeFound("createdAt.lessThan=" + DEFAULT_CREATED_AT);

        // Get all the organizationList where createdAt is less than UPDATED_CREATED_AT
        defaultOrganizationShouldBeFound("createdAt.lessThan=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllOrganizationsByCreatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where createdAt is greater than DEFAULT_CREATED_AT
        defaultOrganizationShouldNotBeFound("createdAt.greaterThan=" + DEFAULT_CREATED_AT);

        // Get all the organizationList where createdAt is greater than SMALLER_CREATED_AT
        defaultOrganizationShouldBeFound("createdAt.greaterThan=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllOrganizationsByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultOrganizationShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the organizationList where updatedAt equals to UPDATED_UPDATED_AT
        defaultOrganizationShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllOrganizationsByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultOrganizationShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the organizationList where updatedAt equals to UPDATED_UPDATED_AT
        defaultOrganizationShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllOrganizationsByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where updatedAt is not null
        defaultOrganizationShouldBeFound("updatedAt.specified=true");

        // Get all the organizationList where updatedAt is null
        defaultOrganizationShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllOrganizationsByUpdatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where updatedAt is greater than or equal to DEFAULT_UPDATED_AT
        defaultOrganizationShouldBeFound("updatedAt.greaterThanOrEqual=" + DEFAULT_UPDATED_AT);

        // Get all the organizationList where updatedAt is greater than or equal to UPDATED_UPDATED_AT
        defaultOrganizationShouldNotBeFound("updatedAt.greaterThanOrEqual=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllOrganizationsByUpdatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where updatedAt is less than or equal to DEFAULT_UPDATED_AT
        defaultOrganizationShouldBeFound("updatedAt.lessThanOrEqual=" + DEFAULT_UPDATED_AT);

        // Get all the organizationList where updatedAt is less than or equal to SMALLER_UPDATED_AT
        defaultOrganizationShouldNotBeFound("updatedAt.lessThanOrEqual=" + SMALLER_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllOrganizationsByUpdatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where updatedAt is less than DEFAULT_UPDATED_AT
        defaultOrganizationShouldNotBeFound("updatedAt.lessThan=" + DEFAULT_UPDATED_AT);

        // Get all the organizationList where updatedAt is less than UPDATED_UPDATED_AT
        defaultOrganizationShouldBeFound("updatedAt.lessThan=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllOrganizationsByUpdatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where updatedAt is greater than DEFAULT_UPDATED_AT
        defaultOrganizationShouldNotBeFound("updatedAt.greaterThan=" + DEFAULT_UPDATED_AT);

        // Get all the organizationList where updatedAt is greater than SMALLER_UPDATED_AT
        defaultOrganizationShouldBeFound("updatedAt.greaterThan=" + SMALLER_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllOrganizationsByMachineIsEqualToSomething() throws Exception {
        Machine machine;
        if (TestUtil.findAll(em, Machine.class).isEmpty()) {
            organizationRepository.saveAndFlush(organization);
            machine = MachineResourceIT.createEntity(em);
        } else {
            machine = TestUtil.findAll(em, Machine.class).get(0);
        }
        em.persist(machine);
        em.flush();
        organization.addMachine(machine);
        organizationRepository.saveAndFlush(organization);
        Long machineId = machine.getId();
        // Get all the organizationList where machine equals to machineId
        defaultOrganizationShouldBeFound("machineId.equals=" + machineId);

        // Get all the organizationList where machine equals to (machineId + 1)
        defaultOrganizationShouldNotBeFound("machineId.equals=" + (machineId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOrganizationShouldBeFound(String filter) throws Exception {
        restOrganizationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(organization.getId().intValue())))
            .andExpect(jsonPath("$.[*].reference").value(hasItem(DEFAULT_REFERENCE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].legalName").value(hasItem(DEFAULT_LEGAL_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].taxId").value(hasItem(DEFAULT_TAX_ID)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE)))
            .andExpect(jsonPath("$.[*].region").value(hasItem(DEFAULT_REGION)))
            .andExpect(jsonPath("$.[*].locality").value(hasItem(DEFAULT_LOCALITY)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));

        // Check, that the count call also returns 1
        restOrganizationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOrganizationShouldNotBeFound(String filter) throws Exception {
        restOrganizationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOrganizationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOrganization() throws Exception {
        // Get the organization
        restOrganizationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOrganization() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        int databaseSizeBeforeUpdate = organizationRepository.findAll().size();

        // Update the organization
        Organization updatedOrganization = organizationRepository.findById(organization.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOrganization are not directly saved in db
        em.detach(updatedOrganization);
        updatedOrganization
            .reference(UPDATED_REFERENCE)
            .name(UPDATED_NAME)
            .legalName(UPDATED_LEGAL_NAME)
            .description(UPDATED_DESCRIPTION)
            .taxId(UPDATED_TAX_ID)
            .email(UPDATED_EMAIL)
            .telephone(UPDATED_TELEPHONE)
            .url(UPDATED_URL)
            .address(UPDATED_ADDRESS)
            .postalCode(UPDATED_POSTAL_CODE)
            .region(UPDATED_REGION)
            .locality(UPDATED_LOCALITY)
            .country(UPDATED_COUNTRY)
            .location(UPDATED_LOCATION)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        OrganizationDTO organizationDTO = organizationMapper.toDto(updatedOrganization);

        restOrganizationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, organizationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(organizationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Organization in the database
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeUpdate);
        Organization testOrganization = organizationList.get(organizationList.size() - 1);
        assertThat(testOrganization.getReference()).isEqualTo(UPDATED_REFERENCE);
        assertThat(testOrganization.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrganization.getLegalName()).isEqualTo(UPDATED_LEGAL_NAME);
        assertThat(testOrganization.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testOrganization.getTaxId()).isEqualTo(UPDATED_TAX_ID);
        assertThat(testOrganization.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testOrganization.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testOrganization.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testOrganization.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testOrganization.getPostalCode()).isEqualTo(UPDATED_POSTAL_CODE);
        assertThat(testOrganization.getRegion()).isEqualTo(UPDATED_REGION);
        assertThat(testOrganization.getLocality()).isEqualTo(UPDATED_LOCALITY);
        assertThat(testOrganization.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testOrganization.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testOrganization.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testOrganization.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingOrganization() throws Exception {
        int databaseSizeBeforeUpdate = organizationRepository.findAll().size();
        organization.setId(longCount.incrementAndGet());

        // Create the Organization
        OrganizationDTO organizationDTO = organizationMapper.toDto(organization);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrganizationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, organizationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(organizationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Organization in the database
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrganization() throws Exception {
        int databaseSizeBeforeUpdate = organizationRepository.findAll().size();
        organization.setId(longCount.incrementAndGet());

        // Create the Organization
        OrganizationDTO organizationDTO = organizationMapper.toDto(organization);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganizationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(organizationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Organization in the database
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrganization() throws Exception {
        int databaseSizeBeforeUpdate = organizationRepository.findAll().size();
        organization.setId(longCount.incrementAndGet());

        // Create the Organization
        OrganizationDTO organizationDTO = organizationMapper.toDto(organization);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganizationMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(organizationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Organization in the database
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrganizationWithPatch() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        int databaseSizeBeforeUpdate = organizationRepository.findAll().size();

        // Update the organization using partial update
        Organization partialUpdatedOrganization = new Organization();
        partialUpdatedOrganization.setId(organization.getId());

        partialUpdatedOrganization
            .reference(UPDATED_REFERENCE)
            .description(UPDATED_DESCRIPTION)
            .email(UPDATED_EMAIL)
            .telephone(UPDATED_TELEPHONE)
            .address(UPDATED_ADDRESS)
            .postalCode(UPDATED_POSTAL_CODE)
            .region(UPDATED_REGION)
            .locality(UPDATED_LOCALITY);

        restOrganizationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrganization.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrganization))
            )
            .andExpect(status().isOk());

        // Validate the Organization in the database
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeUpdate);
        Organization testOrganization = organizationList.get(organizationList.size() - 1);
        assertThat(testOrganization.getReference()).isEqualTo(UPDATED_REFERENCE);
        assertThat(testOrganization.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOrganization.getLegalName()).isEqualTo(DEFAULT_LEGAL_NAME);
        assertThat(testOrganization.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testOrganization.getTaxId()).isEqualTo(DEFAULT_TAX_ID);
        assertThat(testOrganization.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testOrganization.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testOrganization.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testOrganization.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testOrganization.getPostalCode()).isEqualTo(UPDATED_POSTAL_CODE);
        assertThat(testOrganization.getRegion()).isEqualTo(UPDATED_REGION);
        assertThat(testOrganization.getLocality()).isEqualTo(UPDATED_LOCALITY);
        assertThat(testOrganization.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testOrganization.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testOrganization.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testOrganization.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateOrganizationWithPatch() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        int databaseSizeBeforeUpdate = organizationRepository.findAll().size();

        // Update the organization using partial update
        Organization partialUpdatedOrganization = new Organization();
        partialUpdatedOrganization.setId(organization.getId());

        partialUpdatedOrganization
            .reference(UPDATED_REFERENCE)
            .name(UPDATED_NAME)
            .legalName(UPDATED_LEGAL_NAME)
            .description(UPDATED_DESCRIPTION)
            .taxId(UPDATED_TAX_ID)
            .email(UPDATED_EMAIL)
            .telephone(UPDATED_TELEPHONE)
            .url(UPDATED_URL)
            .address(UPDATED_ADDRESS)
            .postalCode(UPDATED_POSTAL_CODE)
            .region(UPDATED_REGION)
            .locality(UPDATED_LOCALITY)
            .country(UPDATED_COUNTRY)
            .location(UPDATED_LOCATION)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restOrganizationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrganization.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrganization))
            )
            .andExpect(status().isOk());

        // Validate the Organization in the database
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeUpdate);
        Organization testOrganization = organizationList.get(organizationList.size() - 1);
        assertThat(testOrganization.getReference()).isEqualTo(UPDATED_REFERENCE);
        assertThat(testOrganization.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrganization.getLegalName()).isEqualTo(UPDATED_LEGAL_NAME);
        assertThat(testOrganization.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testOrganization.getTaxId()).isEqualTo(UPDATED_TAX_ID);
        assertThat(testOrganization.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testOrganization.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testOrganization.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testOrganization.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testOrganization.getPostalCode()).isEqualTo(UPDATED_POSTAL_CODE);
        assertThat(testOrganization.getRegion()).isEqualTo(UPDATED_REGION);
        assertThat(testOrganization.getLocality()).isEqualTo(UPDATED_LOCALITY);
        assertThat(testOrganization.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testOrganization.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testOrganization.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testOrganization.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingOrganization() throws Exception {
        int databaseSizeBeforeUpdate = organizationRepository.findAll().size();
        organization.setId(longCount.incrementAndGet());

        // Create the Organization
        OrganizationDTO organizationDTO = organizationMapper.toDto(organization);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrganizationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, organizationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(organizationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Organization in the database
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrganization() throws Exception {
        int databaseSizeBeforeUpdate = organizationRepository.findAll().size();
        organization.setId(longCount.incrementAndGet());

        // Create the Organization
        OrganizationDTO organizationDTO = organizationMapper.toDto(organization);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganizationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(organizationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Organization in the database
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrganization() throws Exception {
        int databaseSizeBeforeUpdate = organizationRepository.findAll().size();
        organization.setId(longCount.incrementAndGet());

        // Create the Organization
        OrganizationDTO organizationDTO = organizationMapper.toDto(organization);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganizationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(organizationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Organization in the database
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrganization() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        int databaseSizeBeforeDelete = organizationRepository.findAll().size();

        // Delete the organization
        restOrganizationMockMvc
            .perform(delete(ENTITY_API_URL_ID, organization.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
