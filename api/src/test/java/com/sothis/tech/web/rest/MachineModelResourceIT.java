package com.sothis.tech.web.rest;

import static com.sothis.tech.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sothis.tech.IntegrationTest;
import com.sothis.tech.domain.MachineModel;
import com.sothis.tech.repository.MachineModelRepository;
import com.sothis.tech.service.dto.MachineModelDTO;
import com.sothis.tech.service.mapper.MachineModelMapper;
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
 * Integration tests for the {@link MachineModelResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MachineModelResourceIT {

    private static final String DEFAULT_REFERENCE = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_BRAND_NAME = "AAAAAAAAAA";
    private static final String UPDATED_BRAND_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_MANUFACURER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MANUFACURER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_VERSION = "AAAAAAAAAA";
    private static final String UPDATED_VERSION = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/machine-models";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MachineModelRepository machineModelRepository;

    @Autowired
    private MachineModelMapper machineModelMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMachineModelMockMvc;

    private MachineModel machineModel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MachineModel createEntity(EntityManager em) {
        MachineModel machineModel = new MachineModel()
            .reference(DEFAULT_REFERENCE)
            .name(DEFAULT_NAME)
            .brandName(DEFAULT_BRAND_NAME)
            .description(DEFAULT_DESCRIPTION)
            .type(DEFAULT_TYPE)
            .manufacurerName(DEFAULT_MANUFACURER_NAME)
            .version(DEFAULT_VERSION)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return machineModel;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MachineModel createUpdatedEntity(EntityManager em) {
        MachineModel machineModel = new MachineModel()
            .reference(UPDATED_REFERENCE)
            .name(UPDATED_NAME)
            .brandName(UPDATED_BRAND_NAME)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .manufacurerName(UPDATED_MANUFACURER_NAME)
            .version(UPDATED_VERSION)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return machineModel;
    }

    @BeforeEach
    public void initTest() {
        machineModel = createEntity(em);
    }

    @Test
    @Transactional
    void createMachineModel() throws Exception {
        int databaseSizeBeforeCreate = machineModelRepository.findAll().size();
        // Create the MachineModel
        MachineModelDTO machineModelDTO = machineModelMapper.toDto(machineModel);
        restMachineModelMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(machineModelDTO))
            )
            .andExpect(status().isCreated());

        // Validate the MachineModel in the database
        List<MachineModel> machineModelList = machineModelRepository.findAll();
        assertThat(machineModelList).hasSize(databaseSizeBeforeCreate + 1);
        MachineModel testMachineModel = machineModelList.get(machineModelList.size() - 1);
        assertThat(testMachineModel.getReference()).isEqualTo(DEFAULT_REFERENCE);
        assertThat(testMachineModel.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMachineModel.getBrandName()).isEqualTo(DEFAULT_BRAND_NAME);
        assertThat(testMachineModel.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testMachineModel.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testMachineModel.getManufacurerName()).isEqualTo(DEFAULT_MANUFACURER_NAME);
        assertThat(testMachineModel.getVersion()).isEqualTo(DEFAULT_VERSION);
        assertThat(testMachineModel.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testMachineModel.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createMachineModelWithExistingId() throws Exception {
        // Create the MachineModel with an existing ID
        machineModel.setId(1L);
        MachineModelDTO machineModelDTO = machineModelMapper.toDto(machineModel);

        int databaseSizeBeforeCreate = machineModelRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMachineModelMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(machineModelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MachineModel in the database
        List<MachineModel> machineModelList = machineModelRepository.findAll();
        assertThat(machineModelList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkReferenceIsRequired() throws Exception {
        int databaseSizeBeforeTest = machineModelRepository.findAll().size();
        // set the field null
        machineModel.setReference(null);

        // Create the MachineModel, which fails.
        MachineModelDTO machineModelDTO = machineModelMapper.toDto(machineModel);

        restMachineModelMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(machineModelDTO))
            )
            .andExpect(status().isBadRequest());

        List<MachineModel> machineModelList = machineModelRepository.findAll();
        assertThat(machineModelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = machineModelRepository.findAll().size();
        // set the field null
        machineModel.setName(null);

        // Create the MachineModel, which fails.
        MachineModelDTO machineModelDTO = machineModelMapper.toDto(machineModel);

        restMachineModelMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(machineModelDTO))
            )
            .andExpect(status().isBadRequest());

        List<MachineModel> machineModelList = machineModelRepository.findAll();
        assertThat(machineModelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBrandNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = machineModelRepository.findAll().size();
        // set the field null
        machineModel.setBrandName(null);

        // Create the MachineModel, which fails.
        MachineModelDTO machineModelDTO = machineModelMapper.toDto(machineModel);

        restMachineModelMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(machineModelDTO))
            )
            .andExpect(status().isBadRequest());

        List<MachineModel> machineModelList = machineModelRepository.findAll();
        assertThat(machineModelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMachineModels() throws Exception {
        // Initialize the database
        machineModelRepository.saveAndFlush(machineModel);

        // Get all the machineModelList
        restMachineModelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(machineModel.getId().intValue())))
            .andExpect(jsonPath("$.[*].reference").value(hasItem(DEFAULT_REFERENCE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].brandName").value(hasItem(DEFAULT_BRAND_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].manufacurerName").value(hasItem(DEFAULT_MANUFACURER_NAME)))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));
    }

    @Test
    @Transactional
    void getMachineModel() throws Exception {
        // Initialize the database
        machineModelRepository.saveAndFlush(machineModel);

        // Get the machineModel
        restMachineModelMockMvc
            .perform(get(ENTITY_API_URL_ID, machineModel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(machineModel.getId().intValue()))
            .andExpect(jsonPath("$.reference").value(DEFAULT_REFERENCE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.brandName").value(DEFAULT_BRAND_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.manufacurerName").value(DEFAULT_MANUFACURER_NAME))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.updatedAt").value(sameInstant(DEFAULT_UPDATED_AT)));
    }

    @Test
    @Transactional
    void getMachineModelsByIdFiltering() throws Exception {
        // Initialize the database
        machineModelRepository.saveAndFlush(machineModel);

        Long id = machineModel.getId();

        defaultMachineModelShouldBeFound("id.equals=" + id);
        defaultMachineModelShouldNotBeFound("id.notEquals=" + id);

        defaultMachineModelShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMachineModelShouldNotBeFound("id.greaterThan=" + id);

        defaultMachineModelShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMachineModelShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMachineModelsByReferenceIsEqualToSomething() throws Exception {
        // Initialize the database
        machineModelRepository.saveAndFlush(machineModel);

        // Get all the machineModelList where reference equals to DEFAULT_REFERENCE
        defaultMachineModelShouldBeFound("reference.equals=" + DEFAULT_REFERENCE);

        // Get all the machineModelList where reference equals to UPDATED_REFERENCE
        defaultMachineModelShouldNotBeFound("reference.equals=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    void getAllMachineModelsByReferenceIsInShouldWork() throws Exception {
        // Initialize the database
        machineModelRepository.saveAndFlush(machineModel);

        // Get all the machineModelList where reference in DEFAULT_REFERENCE or UPDATED_REFERENCE
        defaultMachineModelShouldBeFound("reference.in=" + DEFAULT_REFERENCE + "," + UPDATED_REFERENCE);

        // Get all the machineModelList where reference equals to UPDATED_REFERENCE
        defaultMachineModelShouldNotBeFound("reference.in=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    void getAllMachineModelsByReferenceIsNullOrNotNull() throws Exception {
        // Initialize the database
        machineModelRepository.saveAndFlush(machineModel);

        // Get all the machineModelList where reference is not null
        defaultMachineModelShouldBeFound("reference.specified=true");

        // Get all the machineModelList where reference is null
        defaultMachineModelShouldNotBeFound("reference.specified=false");
    }

    @Test
    @Transactional
    void getAllMachineModelsByReferenceContainsSomething() throws Exception {
        // Initialize the database
        machineModelRepository.saveAndFlush(machineModel);

        // Get all the machineModelList where reference contains DEFAULT_REFERENCE
        defaultMachineModelShouldBeFound("reference.contains=" + DEFAULT_REFERENCE);

        // Get all the machineModelList where reference contains UPDATED_REFERENCE
        defaultMachineModelShouldNotBeFound("reference.contains=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    void getAllMachineModelsByReferenceNotContainsSomething() throws Exception {
        // Initialize the database
        machineModelRepository.saveAndFlush(machineModel);

        // Get all the machineModelList where reference does not contain DEFAULT_REFERENCE
        defaultMachineModelShouldNotBeFound("reference.doesNotContain=" + DEFAULT_REFERENCE);

        // Get all the machineModelList where reference does not contain UPDATED_REFERENCE
        defaultMachineModelShouldBeFound("reference.doesNotContain=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    void getAllMachineModelsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        machineModelRepository.saveAndFlush(machineModel);

        // Get all the machineModelList where name equals to DEFAULT_NAME
        defaultMachineModelShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the machineModelList where name equals to UPDATED_NAME
        defaultMachineModelShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMachineModelsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        machineModelRepository.saveAndFlush(machineModel);

        // Get all the machineModelList where name in DEFAULT_NAME or UPDATED_NAME
        defaultMachineModelShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the machineModelList where name equals to UPDATED_NAME
        defaultMachineModelShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMachineModelsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        machineModelRepository.saveAndFlush(machineModel);

        // Get all the machineModelList where name is not null
        defaultMachineModelShouldBeFound("name.specified=true");

        // Get all the machineModelList where name is null
        defaultMachineModelShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllMachineModelsByNameContainsSomething() throws Exception {
        // Initialize the database
        machineModelRepository.saveAndFlush(machineModel);

        // Get all the machineModelList where name contains DEFAULT_NAME
        defaultMachineModelShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the machineModelList where name contains UPDATED_NAME
        defaultMachineModelShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMachineModelsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        machineModelRepository.saveAndFlush(machineModel);

        // Get all the machineModelList where name does not contain DEFAULT_NAME
        defaultMachineModelShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the machineModelList where name does not contain UPDATED_NAME
        defaultMachineModelShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMachineModelsByBrandNameIsEqualToSomething() throws Exception {
        // Initialize the database
        machineModelRepository.saveAndFlush(machineModel);

        // Get all the machineModelList where brandName equals to DEFAULT_BRAND_NAME
        defaultMachineModelShouldBeFound("brandName.equals=" + DEFAULT_BRAND_NAME);

        // Get all the machineModelList where brandName equals to UPDATED_BRAND_NAME
        defaultMachineModelShouldNotBeFound("brandName.equals=" + UPDATED_BRAND_NAME);
    }

    @Test
    @Transactional
    void getAllMachineModelsByBrandNameIsInShouldWork() throws Exception {
        // Initialize the database
        machineModelRepository.saveAndFlush(machineModel);

        // Get all the machineModelList where brandName in DEFAULT_BRAND_NAME or UPDATED_BRAND_NAME
        defaultMachineModelShouldBeFound("brandName.in=" + DEFAULT_BRAND_NAME + "," + UPDATED_BRAND_NAME);

        // Get all the machineModelList where brandName equals to UPDATED_BRAND_NAME
        defaultMachineModelShouldNotBeFound("brandName.in=" + UPDATED_BRAND_NAME);
    }

    @Test
    @Transactional
    void getAllMachineModelsByBrandNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        machineModelRepository.saveAndFlush(machineModel);

        // Get all the machineModelList where brandName is not null
        defaultMachineModelShouldBeFound("brandName.specified=true");

        // Get all the machineModelList where brandName is null
        defaultMachineModelShouldNotBeFound("brandName.specified=false");
    }

    @Test
    @Transactional
    void getAllMachineModelsByBrandNameContainsSomething() throws Exception {
        // Initialize the database
        machineModelRepository.saveAndFlush(machineModel);

        // Get all the machineModelList where brandName contains DEFAULT_BRAND_NAME
        defaultMachineModelShouldBeFound("brandName.contains=" + DEFAULT_BRAND_NAME);

        // Get all the machineModelList where brandName contains UPDATED_BRAND_NAME
        defaultMachineModelShouldNotBeFound("brandName.contains=" + UPDATED_BRAND_NAME);
    }

    @Test
    @Transactional
    void getAllMachineModelsByBrandNameNotContainsSomething() throws Exception {
        // Initialize the database
        machineModelRepository.saveAndFlush(machineModel);

        // Get all the machineModelList where brandName does not contain DEFAULT_BRAND_NAME
        defaultMachineModelShouldNotBeFound("brandName.doesNotContain=" + DEFAULT_BRAND_NAME);

        // Get all the machineModelList where brandName does not contain UPDATED_BRAND_NAME
        defaultMachineModelShouldBeFound("brandName.doesNotContain=" + UPDATED_BRAND_NAME);
    }

    @Test
    @Transactional
    void getAllMachineModelsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        machineModelRepository.saveAndFlush(machineModel);

        // Get all the machineModelList where description equals to DEFAULT_DESCRIPTION
        defaultMachineModelShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the machineModelList where description equals to UPDATED_DESCRIPTION
        defaultMachineModelShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMachineModelsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        machineModelRepository.saveAndFlush(machineModel);

        // Get all the machineModelList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultMachineModelShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the machineModelList where description equals to UPDATED_DESCRIPTION
        defaultMachineModelShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMachineModelsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        machineModelRepository.saveAndFlush(machineModel);

        // Get all the machineModelList where description is not null
        defaultMachineModelShouldBeFound("description.specified=true");

        // Get all the machineModelList where description is null
        defaultMachineModelShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllMachineModelsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        machineModelRepository.saveAndFlush(machineModel);

        // Get all the machineModelList where description contains DEFAULT_DESCRIPTION
        defaultMachineModelShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the machineModelList where description contains UPDATED_DESCRIPTION
        defaultMachineModelShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMachineModelsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        machineModelRepository.saveAndFlush(machineModel);

        // Get all the machineModelList where description does not contain DEFAULT_DESCRIPTION
        defaultMachineModelShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the machineModelList where description does not contain UPDATED_DESCRIPTION
        defaultMachineModelShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMachineModelsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        machineModelRepository.saveAndFlush(machineModel);

        // Get all the machineModelList where type equals to DEFAULT_TYPE
        defaultMachineModelShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the machineModelList where type equals to UPDATED_TYPE
        defaultMachineModelShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllMachineModelsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        machineModelRepository.saveAndFlush(machineModel);

        // Get all the machineModelList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultMachineModelShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the machineModelList where type equals to UPDATED_TYPE
        defaultMachineModelShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllMachineModelsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        machineModelRepository.saveAndFlush(machineModel);

        // Get all the machineModelList where type is not null
        defaultMachineModelShouldBeFound("type.specified=true");

        // Get all the machineModelList where type is null
        defaultMachineModelShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    void getAllMachineModelsByTypeContainsSomething() throws Exception {
        // Initialize the database
        machineModelRepository.saveAndFlush(machineModel);

        // Get all the machineModelList where type contains DEFAULT_TYPE
        defaultMachineModelShouldBeFound("type.contains=" + DEFAULT_TYPE);

        // Get all the machineModelList where type contains UPDATED_TYPE
        defaultMachineModelShouldNotBeFound("type.contains=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllMachineModelsByTypeNotContainsSomething() throws Exception {
        // Initialize the database
        machineModelRepository.saveAndFlush(machineModel);

        // Get all the machineModelList where type does not contain DEFAULT_TYPE
        defaultMachineModelShouldNotBeFound("type.doesNotContain=" + DEFAULT_TYPE);

        // Get all the machineModelList where type does not contain UPDATED_TYPE
        defaultMachineModelShouldBeFound("type.doesNotContain=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllMachineModelsByManufacurerNameIsEqualToSomething() throws Exception {
        // Initialize the database
        machineModelRepository.saveAndFlush(machineModel);

        // Get all the machineModelList where manufacurerName equals to DEFAULT_MANUFACURER_NAME
        defaultMachineModelShouldBeFound("manufacurerName.equals=" + DEFAULT_MANUFACURER_NAME);

        // Get all the machineModelList where manufacurerName equals to UPDATED_MANUFACURER_NAME
        defaultMachineModelShouldNotBeFound("manufacurerName.equals=" + UPDATED_MANUFACURER_NAME);
    }

    @Test
    @Transactional
    void getAllMachineModelsByManufacurerNameIsInShouldWork() throws Exception {
        // Initialize the database
        machineModelRepository.saveAndFlush(machineModel);

        // Get all the machineModelList where manufacurerName in DEFAULT_MANUFACURER_NAME or UPDATED_MANUFACURER_NAME
        defaultMachineModelShouldBeFound("manufacurerName.in=" + DEFAULT_MANUFACURER_NAME + "," + UPDATED_MANUFACURER_NAME);

        // Get all the machineModelList where manufacurerName equals to UPDATED_MANUFACURER_NAME
        defaultMachineModelShouldNotBeFound("manufacurerName.in=" + UPDATED_MANUFACURER_NAME);
    }

    @Test
    @Transactional
    void getAllMachineModelsByManufacurerNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        machineModelRepository.saveAndFlush(machineModel);

        // Get all the machineModelList where manufacurerName is not null
        defaultMachineModelShouldBeFound("manufacurerName.specified=true");

        // Get all the machineModelList where manufacurerName is null
        defaultMachineModelShouldNotBeFound("manufacurerName.specified=false");
    }

    @Test
    @Transactional
    void getAllMachineModelsByManufacurerNameContainsSomething() throws Exception {
        // Initialize the database
        machineModelRepository.saveAndFlush(machineModel);

        // Get all the machineModelList where manufacurerName contains DEFAULT_MANUFACURER_NAME
        defaultMachineModelShouldBeFound("manufacurerName.contains=" + DEFAULT_MANUFACURER_NAME);

        // Get all the machineModelList where manufacurerName contains UPDATED_MANUFACURER_NAME
        defaultMachineModelShouldNotBeFound("manufacurerName.contains=" + UPDATED_MANUFACURER_NAME);
    }

    @Test
    @Transactional
    void getAllMachineModelsByManufacurerNameNotContainsSomething() throws Exception {
        // Initialize the database
        machineModelRepository.saveAndFlush(machineModel);

        // Get all the machineModelList where manufacurerName does not contain DEFAULT_MANUFACURER_NAME
        defaultMachineModelShouldNotBeFound("manufacurerName.doesNotContain=" + DEFAULT_MANUFACURER_NAME);

        // Get all the machineModelList where manufacurerName does not contain UPDATED_MANUFACURER_NAME
        defaultMachineModelShouldBeFound("manufacurerName.doesNotContain=" + UPDATED_MANUFACURER_NAME);
    }

    @Test
    @Transactional
    void getAllMachineModelsByVersionIsEqualToSomething() throws Exception {
        // Initialize the database
        machineModelRepository.saveAndFlush(machineModel);

        // Get all the machineModelList where version equals to DEFAULT_VERSION
        defaultMachineModelShouldBeFound("version.equals=" + DEFAULT_VERSION);

        // Get all the machineModelList where version equals to UPDATED_VERSION
        defaultMachineModelShouldNotBeFound("version.equals=" + UPDATED_VERSION);
    }

    @Test
    @Transactional
    void getAllMachineModelsByVersionIsInShouldWork() throws Exception {
        // Initialize the database
        machineModelRepository.saveAndFlush(machineModel);

        // Get all the machineModelList where version in DEFAULT_VERSION or UPDATED_VERSION
        defaultMachineModelShouldBeFound("version.in=" + DEFAULT_VERSION + "," + UPDATED_VERSION);

        // Get all the machineModelList where version equals to UPDATED_VERSION
        defaultMachineModelShouldNotBeFound("version.in=" + UPDATED_VERSION);
    }

    @Test
    @Transactional
    void getAllMachineModelsByVersionIsNullOrNotNull() throws Exception {
        // Initialize the database
        machineModelRepository.saveAndFlush(machineModel);

        // Get all the machineModelList where version is not null
        defaultMachineModelShouldBeFound("version.specified=true");

        // Get all the machineModelList where version is null
        defaultMachineModelShouldNotBeFound("version.specified=false");
    }

    @Test
    @Transactional
    void getAllMachineModelsByVersionContainsSomething() throws Exception {
        // Initialize the database
        machineModelRepository.saveAndFlush(machineModel);

        // Get all the machineModelList where version contains DEFAULT_VERSION
        defaultMachineModelShouldBeFound("version.contains=" + DEFAULT_VERSION);

        // Get all the machineModelList where version contains UPDATED_VERSION
        defaultMachineModelShouldNotBeFound("version.contains=" + UPDATED_VERSION);
    }

    @Test
    @Transactional
    void getAllMachineModelsByVersionNotContainsSomething() throws Exception {
        // Initialize the database
        machineModelRepository.saveAndFlush(machineModel);

        // Get all the machineModelList where version does not contain DEFAULT_VERSION
        defaultMachineModelShouldNotBeFound("version.doesNotContain=" + DEFAULT_VERSION);

        // Get all the machineModelList where version does not contain UPDATED_VERSION
        defaultMachineModelShouldBeFound("version.doesNotContain=" + UPDATED_VERSION);
    }

    @Test
    @Transactional
    void getAllMachineModelsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        machineModelRepository.saveAndFlush(machineModel);

        // Get all the machineModelList where createdAt equals to DEFAULT_CREATED_AT
        defaultMachineModelShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the machineModelList where createdAt equals to UPDATED_CREATED_AT
        defaultMachineModelShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllMachineModelsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        machineModelRepository.saveAndFlush(machineModel);

        // Get all the machineModelList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultMachineModelShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the machineModelList where createdAt equals to UPDATED_CREATED_AT
        defaultMachineModelShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllMachineModelsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        machineModelRepository.saveAndFlush(machineModel);

        // Get all the machineModelList where createdAt is not null
        defaultMachineModelShouldBeFound("createdAt.specified=true");

        // Get all the machineModelList where createdAt is null
        defaultMachineModelShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllMachineModelsByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        machineModelRepository.saveAndFlush(machineModel);

        // Get all the machineModelList where createdAt is greater than or equal to DEFAULT_CREATED_AT
        defaultMachineModelShouldBeFound("createdAt.greaterThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the machineModelList where createdAt is greater than or equal to UPDATED_CREATED_AT
        defaultMachineModelShouldNotBeFound("createdAt.greaterThanOrEqual=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllMachineModelsByCreatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        machineModelRepository.saveAndFlush(machineModel);

        // Get all the machineModelList where createdAt is less than or equal to DEFAULT_CREATED_AT
        defaultMachineModelShouldBeFound("createdAt.lessThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the machineModelList where createdAt is less than or equal to SMALLER_CREATED_AT
        defaultMachineModelShouldNotBeFound("createdAt.lessThanOrEqual=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllMachineModelsByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        machineModelRepository.saveAndFlush(machineModel);

        // Get all the machineModelList where createdAt is less than DEFAULT_CREATED_AT
        defaultMachineModelShouldNotBeFound("createdAt.lessThan=" + DEFAULT_CREATED_AT);

        // Get all the machineModelList where createdAt is less than UPDATED_CREATED_AT
        defaultMachineModelShouldBeFound("createdAt.lessThan=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllMachineModelsByCreatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        machineModelRepository.saveAndFlush(machineModel);

        // Get all the machineModelList where createdAt is greater than DEFAULT_CREATED_AT
        defaultMachineModelShouldNotBeFound("createdAt.greaterThan=" + DEFAULT_CREATED_AT);

        // Get all the machineModelList where createdAt is greater than SMALLER_CREATED_AT
        defaultMachineModelShouldBeFound("createdAt.greaterThan=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllMachineModelsByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        machineModelRepository.saveAndFlush(machineModel);

        // Get all the machineModelList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultMachineModelShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the machineModelList where updatedAt equals to UPDATED_UPDATED_AT
        defaultMachineModelShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllMachineModelsByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        machineModelRepository.saveAndFlush(machineModel);

        // Get all the machineModelList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultMachineModelShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the machineModelList where updatedAt equals to UPDATED_UPDATED_AT
        defaultMachineModelShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllMachineModelsByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        machineModelRepository.saveAndFlush(machineModel);

        // Get all the machineModelList where updatedAt is not null
        defaultMachineModelShouldBeFound("updatedAt.specified=true");

        // Get all the machineModelList where updatedAt is null
        defaultMachineModelShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllMachineModelsByUpdatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        machineModelRepository.saveAndFlush(machineModel);

        // Get all the machineModelList where updatedAt is greater than or equal to DEFAULT_UPDATED_AT
        defaultMachineModelShouldBeFound("updatedAt.greaterThanOrEqual=" + DEFAULT_UPDATED_AT);

        // Get all the machineModelList where updatedAt is greater than or equal to UPDATED_UPDATED_AT
        defaultMachineModelShouldNotBeFound("updatedAt.greaterThanOrEqual=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllMachineModelsByUpdatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        machineModelRepository.saveAndFlush(machineModel);

        // Get all the machineModelList where updatedAt is less than or equal to DEFAULT_UPDATED_AT
        defaultMachineModelShouldBeFound("updatedAt.lessThanOrEqual=" + DEFAULT_UPDATED_AT);

        // Get all the machineModelList where updatedAt is less than or equal to SMALLER_UPDATED_AT
        defaultMachineModelShouldNotBeFound("updatedAt.lessThanOrEqual=" + SMALLER_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllMachineModelsByUpdatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        machineModelRepository.saveAndFlush(machineModel);

        // Get all the machineModelList where updatedAt is less than DEFAULT_UPDATED_AT
        defaultMachineModelShouldNotBeFound("updatedAt.lessThan=" + DEFAULT_UPDATED_AT);

        // Get all the machineModelList where updatedAt is less than UPDATED_UPDATED_AT
        defaultMachineModelShouldBeFound("updatedAt.lessThan=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllMachineModelsByUpdatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        machineModelRepository.saveAndFlush(machineModel);

        // Get all the machineModelList where updatedAt is greater than DEFAULT_UPDATED_AT
        defaultMachineModelShouldNotBeFound("updatedAt.greaterThan=" + DEFAULT_UPDATED_AT);

        // Get all the machineModelList where updatedAt is greater than SMALLER_UPDATED_AT
        defaultMachineModelShouldBeFound("updatedAt.greaterThan=" + SMALLER_UPDATED_AT);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMachineModelShouldBeFound(String filter) throws Exception {
        restMachineModelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(machineModel.getId().intValue())))
            .andExpect(jsonPath("$.[*].reference").value(hasItem(DEFAULT_REFERENCE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].brandName").value(hasItem(DEFAULT_BRAND_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].manufacurerName").value(hasItem(DEFAULT_MANUFACURER_NAME)))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));

        // Check, that the count call also returns 1
        restMachineModelMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMachineModelShouldNotBeFound(String filter) throws Exception {
        restMachineModelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMachineModelMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMachineModel() throws Exception {
        // Get the machineModel
        restMachineModelMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMachineModel() throws Exception {
        // Initialize the database
        machineModelRepository.saveAndFlush(machineModel);

        int databaseSizeBeforeUpdate = machineModelRepository.findAll().size();

        // Update the machineModel
        MachineModel updatedMachineModel = machineModelRepository.findById(machineModel.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMachineModel are not directly saved in db
        em.detach(updatedMachineModel);
        updatedMachineModel
            .reference(UPDATED_REFERENCE)
            .name(UPDATED_NAME)
            .brandName(UPDATED_BRAND_NAME)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .manufacurerName(UPDATED_MANUFACURER_NAME)
            .version(UPDATED_VERSION)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        MachineModelDTO machineModelDTO = machineModelMapper.toDto(updatedMachineModel);

        restMachineModelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, machineModelDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(machineModelDTO))
            )
            .andExpect(status().isOk());

        // Validate the MachineModel in the database
        List<MachineModel> machineModelList = machineModelRepository.findAll();
        assertThat(machineModelList).hasSize(databaseSizeBeforeUpdate);
        MachineModel testMachineModel = machineModelList.get(machineModelList.size() - 1);
        assertThat(testMachineModel.getReference()).isEqualTo(UPDATED_REFERENCE);
        assertThat(testMachineModel.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMachineModel.getBrandName()).isEqualTo(UPDATED_BRAND_NAME);
        assertThat(testMachineModel.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMachineModel.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testMachineModel.getManufacurerName()).isEqualTo(UPDATED_MANUFACURER_NAME);
        assertThat(testMachineModel.getVersion()).isEqualTo(UPDATED_VERSION);
        assertThat(testMachineModel.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testMachineModel.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingMachineModel() throws Exception {
        int databaseSizeBeforeUpdate = machineModelRepository.findAll().size();
        machineModel.setId(longCount.incrementAndGet());

        // Create the MachineModel
        MachineModelDTO machineModelDTO = machineModelMapper.toDto(machineModel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMachineModelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, machineModelDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(machineModelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MachineModel in the database
        List<MachineModel> machineModelList = machineModelRepository.findAll();
        assertThat(machineModelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMachineModel() throws Exception {
        int databaseSizeBeforeUpdate = machineModelRepository.findAll().size();
        machineModel.setId(longCount.incrementAndGet());

        // Create the MachineModel
        MachineModelDTO machineModelDTO = machineModelMapper.toDto(machineModel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMachineModelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(machineModelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MachineModel in the database
        List<MachineModel> machineModelList = machineModelRepository.findAll();
        assertThat(machineModelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMachineModel() throws Exception {
        int databaseSizeBeforeUpdate = machineModelRepository.findAll().size();
        machineModel.setId(longCount.incrementAndGet());

        // Create the MachineModel
        MachineModelDTO machineModelDTO = machineModelMapper.toDto(machineModel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMachineModelMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(machineModelDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MachineModel in the database
        List<MachineModel> machineModelList = machineModelRepository.findAll();
        assertThat(machineModelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMachineModelWithPatch() throws Exception {
        // Initialize the database
        machineModelRepository.saveAndFlush(machineModel);

        int databaseSizeBeforeUpdate = machineModelRepository.findAll().size();

        // Update the machineModel using partial update
        MachineModel partialUpdatedMachineModel = new MachineModel();
        partialUpdatedMachineModel.setId(machineModel.getId());

        partialUpdatedMachineModel
            .reference(UPDATED_REFERENCE)
            .brandName(UPDATED_BRAND_NAME)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .version(UPDATED_VERSION)
            .createdAt(UPDATED_CREATED_AT);

        restMachineModelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMachineModel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMachineModel))
            )
            .andExpect(status().isOk());

        // Validate the MachineModel in the database
        List<MachineModel> machineModelList = machineModelRepository.findAll();
        assertThat(machineModelList).hasSize(databaseSizeBeforeUpdate);
        MachineModel testMachineModel = machineModelList.get(machineModelList.size() - 1);
        assertThat(testMachineModel.getReference()).isEqualTo(UPDATED_REFERENCE);
        assertThat(testMachineModel.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMachineModel.getBrandName()).isEqualTo(UPDATED_BRAND_NAME);
        assertThat(testMachineModel.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMachineModel.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testMachineModel.getManufacurerName()).isEqualTo(DEFAULT_MANUFACURER_NAME);
        assertThat(testMachineModel.getVersion()).isEqualTo(UPDATED_VERSION);
        assertThat(testMachineModel.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testMachineModel.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateMachineModelWithPatch() throws Exception {
        // Initialize the database
        machineModelRepository.saveAndFlush(machineModel);

        int databaseSizeBeforeUpdate = machineModelRepository.findAll().size();

        // Update the machineModel using partial update
        MachineModel partialUpdatedMachineModel = new MachineModel();
        partialUpdatedMachineModel.setId(machineModel.getId());

        partialUpdatedMachineModel
            .reference(UPDATED_REFERENCE)
            .name(UPDATED_NAME)
            .brandName(UPDATED_BRAND_NAME)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .manufacurerName(UPDATED_MANUFACURER_NAME)
            .version(UPDATED_VERSION)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restMachineModelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMachineModel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMachineModel))
            )
            .andExpect(status().isOk());

        // Validate the MachineModel in the database
        List<MachineModel> machineModelList = machineModelRepository.findAll();
        assertThat(machineModelList).hasSize(databaseSizeBeforeUpdate);
        MachineModel testMachineModel = machineModelList.get(machineModelList.size() - 1);
        assertThat(testMachineModel.getReference()).isEqualTo(UPDATED_REFERENCE);
        assertThat(testMachineModel.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMachineModel.getBrandName()).isEqualTo(UPDATED_BRAND_NAME);
        assertThat(testMachineModel.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMachineModel.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testMachineModel.getManufacurerName()).isEqualTo(UPDATED_MANUFACURER_NAME);
        assertThat(testMachineModel.getVersion()).isEqualTo(UPDATED_VERSION);
        assertThat(testMachineModel.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testMachineModel.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingMachineModel() throws Exception {
        int databaseSizeBeforeUpdate = machineModelRepository.findAll().size();
        machineModel.setId(longCount.incrementAndGet());

        // Create the MachineModel
        MachineModelDTO machineModelDTO = machineModelMapper.toDto(machineModel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMachineModelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, machineModelDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(machineModelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MachineModel in the database
        List<MachineModel> machineModelList = machineModelRepository.findAll();
        assertThat(machineModelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMachineModel() throws Exception {
        int databaseSizeBeforeUpdate = machineModelRepository.findAll().size();
        machineModel.setId(longCount.incrementAndGet());

        // Create the MachineModel
        MachineModelDTO machineModelDTO = machineModelMapper.toDto(machineModel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMachineModelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(machineModelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MachineModel in the database
        List<MachineModel> machineModelList = machineModelRepository.findAll();
        assertThat(machineModelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMachineModel() throws Exception {
        int databaseSizeBeforeUpdate = machineModelRepository.findAll().size();
        machineModel.setId(longCount.incrementAndGet());

        // Create the MachineModel
        MachineModelDTO machineModelDTO = machineModelMapper.toDto(machineModel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMachineModelMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(machineModelDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MachineModel in the database
        List<MachineModel> machineModelList = machineModelRepository.findAll();
        assertThat(machineModelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMachineModel() throws Exception {
        // Initialize the database
        machineModelRepository.saveAndFlush(machineModel);

        int databaseSizeBeforeDelete = machineModelRepository.findAll().size();

        // Delete the machineModel
        restMachineModelMockMvc
            .perform(delete(ENTITY_API_URL_ID, machineModel.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MachineModel> machineModelList = machineModelRepository.findAll();
        assertThat(machineModelList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
