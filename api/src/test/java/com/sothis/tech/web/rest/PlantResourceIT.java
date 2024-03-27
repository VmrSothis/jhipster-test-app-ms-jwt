package com.sothis.tech.web.rest;

import static com.sothis.tech.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sothis.tech.IntegrationTest;
import com.sothis.tech.domain.Plant;
import com.sothis.tech.domain.Site;
import com.sothis.tech.repository.PlantRepository;
import com.sothis.tech.service.PlantService;
import com.sothis.tech.service.dto.PlantDTO;
import com.sothis.tech.service.mapper.PlantMapper;
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
 * Integration tests for the {@link PlantResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PlantResourceIT {

    private static final String DEFAULT_REFERENCE = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/plants";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PlantRepository plantRepository;

    @Mock
    private PlantRepository plantRepositoryMock;

    @Autowired
    private PlantMapper plantMapper;

    @Mock
    private PlantService plantServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPlantMockMvc;

    private Plant plant;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Plant createEntity(EntityManager em) {
        Plant plant = new Plant()
            .reference(DEFAULT_REFERENCE)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .address(DEFAULT_ADDRESS)
            .location(DEFAULT_LOCATION)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        // Add required entity
        Site site;
        if (TestUtil.findAll(em, Site.class).isEmpty()) {
            site = SiteResourceIT.createEntity(em);
            em.persist(site);
            em.flush();
        } else {
            site = TestUtil.findAll(em, Site.class).get(0);
        }
        plant.setSite(site);
        return plant;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Plant createUpdatedEntity(EntityManager em) {
        Plant plant = new Plant()
            .reference(UPDATED_REFERENCE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .address(UPDATED_ADDRESS)
            .location(UPDATED_LOCATION)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        // Add required entity
        Site site;
        if (TestUtil.findAll(em, Site.class).isEmpty()) {
            site = SiteResourceIT.createUpdatedEntity(em);
            em.persist(site);
            em.flush();
        } else {
            site = TestUtil.findAll(em, Site.class).get(0);
        }
        plant.setSite(site);
        return plant;
    }

    @BeforeEach
    public void initTest() {
        plant = createEntity(em);
    }

    @Test
    @Transactional
    void createPlant() throws Exception {
        int databaseSizeBeforeCreate = plantRepository.findAll().size();
        // Create the Plant
        PlantDTO plantDTO = plantMapper.toDto(plant);
        restPlantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(plantDTO)))
            .andExpect(status().isCreated());

        // Validate the Plant in the database
        List<Plant> plantList = plantRepository.findAll();
        assertThat(plantList).hasSize(databaseSizeBeforeCreate + 1);
        Plant testPlant = plantList.get(plantList.size() - 1);
        assertThat(testPlant.getReference()).isEqualTo(DEFAULT_REFERENCE);
        assertThat(testPlant.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPlant.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPlant.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testPlant.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testPlant.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testPlant.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createPlantWithExistingId() throws Exception {
        // Create the Plant with an existing ID
        plant.setId(1L);
        PlantDTO plantDTO = plantMapper.toDto(plant);

        int databaseSizeBeforeCreate = plantRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(plantDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Plant in the database
        List<Plant> plantList = plantRepository.findAll();
        assertThat(plantList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkReferenceIsRequired() throws Exception {
        int databaseSizeBeforeTest = plantRepository.findAll().size();
        // set the field null
        plant.setReference(null);

        // Create the Plant, which fails.
        PlantDTO plantDTO = plantMapper.toDto(plant);

        restPlantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(plantDTO)))
            .andExpect(status().isBadRequest());

        List<Plant> plantList = plantRepository.findAll();
        assertThat(plantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = plantRepository.findAll().size();
        // set the field null
        plant.setName(null);

        // Create the Plant, which fails.
        PlantDTO plantDTO = plantMapper.toDto(plant);

        restPlantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(plantDTO)))
            .andExpect(status().isBadRequest());

        List<Plant> plantList = plantRepository.findAll();
        assertThat(plantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = plantRepository.findAll().size();
        // set the field null
        plant.setDescription(null);

        // Create the Plant, which fails.
        PlantDTO plantDTO = plantMapper.toDto(plant);

        restPlantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(plantDTO)))
            .andExpect(status().isBadRequest());

        List<Plant> plantList = plantRepository.findAll();
        assertThat(plantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPlants() throws Exception {
        // Initialize the database
        plantRepository.saveAndFlush(plant);

        // Get all the plantList
        restPlantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(plant.getId().intValue())))
            .andExpect(jsonPath("$.[*].reference").value(hasItem(DEFAULT_REFERENCE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPlantsWithEagerRelationshipsIsEnabled() throws Exception {
        when(plantServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPlantMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(plantServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPlantsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(plantServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPlantMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(plantRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPlant() throws Exception {
        // Initialize the database
        plantRepository.saveAndFlush(plant);

        // Get the plant
        restPlantMockMvc
            .perform(get(ENTITY_API_URL_ID, plant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(plant.getId().intValue()))
            .andExpect(jsonPath("$.reference").value(DEFAULT_REFERENCE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.updatedAt").value(sameInstant(DEFAULT_UPDATED_AT)));
    }

    @Test
    @Transactional
    void getPlantsByIdFiltering() throws Exception {
        // Initialize the database
        plantRepository.saveAndFlush(plant);

        Long id = plant.getId();

        defaultPlantShouldBeFound("id.equals=" + id);
        defaultPlantShouldNotBeFound("id.notEquals=" + id);

        defaultPlantShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPlantShouldNotBeFound("id.greaterThan=" + id);

        defaultPlantShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPlantShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPlantsByReferenceIsEqualToSomething() throws Exception {
        // Initialize the database
        plantRepository.saveAndFlush(plant);

        // Get all the plantList where reference equals to DEFAULT_REFERENCE
        defaultPlantShouldBeFound("reference.equals=" + DEFAULT_REFERENCE);

        // Get all the plantList where reference equals to UPDATED_REFERENCE
        defaultPlantShouldNotBeFound("reference.equals=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    void getAllPlantsByReferenceIsInShouldWork() throws Exception {
        // Initialize the database
        plantRepository.saveAndFlush(plant);

        // Get all the plantList where reference in DEFAULT_REFERENCE or UPDATED_REFERENCE
        defaultPlantShouldBeFound("reference.in=" + DEFAULT_REFERENCE + "," + UPDATED_REFERENCE);

        // Get all the plantList where reference equals to UPDATED_REFERENCE
        defaultPlantShouldNotBeFound("reference.in=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    void getAllPlantsByReferenceIsNullOrNotNull() throws Exception {
        // Initialize the database
        plantRepository.saveAndFlush(plant);

        // Get all the plantList where reference is not null
        defaultPlantShouldBeFound("reference.specified=true");

        // Get all the plantList where reference is null
        defaultPlantShouldNotBeFound("reference.specified=false");
    }

    @Test
    @Transactional
    void getAllPlantsByReferenceContainsSomething() throws Exception {
        // Initialize the database
        plantRepository.saveAndFlush(plant);

        // Get all the plantList where reference contains DEFAULT_REFERENCE
        defaultPlantShouldBeFound("reference.contains=" + DEFAULT_REFERENCE);

        // Get all the plantList where reference contains UPDATED_REFERENCE
        defaultPlantShouldNotBeFound("reference.contains=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    void getAllPlantsByReferenceNotContainsSomething() throws Exception {
        // Initialize the database
        plantRepository.saveAndFlush(plant);

        // Get all the plantList where reference does not contain DEFAULT_REFERENCE
        defaultPlantShouldNotBeFound("reference.doesNotContain=" + DEFAULT_REFERENCE);

        // Get all the plantList where reference does not contain UPDATED_REFERENCE
        defaultPlantShouldBeFound("reference.doesNotContain=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    void getAllPlantsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        plantRepository.saveAndFlush(plant);

        // Get all the plantList where name equals to DEFAULT_NAME
        defaultPlantShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the plantList where name equals to UPDATED_NAME
        defaultPlantShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPlantsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        plantRepository.saveAndFlush(plant);

        // Get all the plantList where name in DEFAULT_NAME or UPDATED_NAME
        defaultPlantShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the plantList where name equals to UPDATED_NAME
        defaultPlantShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPlantsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        plantRepository.saveAndFlush(plant);

        // Get all the plantList where name is not null
        defaultPlantShouldBeFound("name.specified=true");

        // Get all the plantList where name is null
        defaultPlantShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllPlantsByNameContainsSomething() throws Exception {
        // Initialize the database
        plantRepository.saveAndFlush(plant);

        // Get all the plantList where name contains DEFAULT_NAME
        defaultPlantShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the plantList where name contains UPDATED_NAME
        defaultPlantShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPlantsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        plantRepository.saveAndFlush(plant);

        // Get all the plantList where name does not contain DEFAULT_NAME
        defaultPlantShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the plantList where name does not contain UPDATED_NAME
        defaultPlantShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPlantsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        plantRepository.saveAndFlush(plant);

        // Get all the plantList where description equals to DEFAULT_DESCRIPTION
        defaultPlantShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the plantList where description equals to UPDATED_DESCRIPTION
        defaultPlantShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPlantsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        plantRepository.saveAndFlush(plant);

        // Get all the plantList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultPlantShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the plantList where description equals to UPDATED_DESCRIPTION
        defaultPlantShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPlantsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        plantRepository.saveAndFlush(plant);

        // Get all the plantList where description is not null
        defaultPlantShouldBeFound("description.specified=true");

        // Get all the plantList where description is null
        defaultPlantShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllPlantsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        plantRepository.saveAndFlush(plant);

        // Get all the plantList where description contains DEFAULT_DESCRIPTION
        defaultPlantShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the plantList where description contains UPDATED_DESCRIPTION
        defaultPlantShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPlantsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        plantRepository.saveAndFlush(plant);

        // Get all the plantList where description does not contain DEFAULT_DESCRIPTION
        defaultPlantShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the plantList where description does not contain UPDATED_DESCRIPTION
        defaultPlantShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPlantsByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        plantRepository.saveAndFlush(plant);

        // Get all the plantList where address equals to DEFAULT_ADDRESS
        defaultPlantShouldBeFound("address.equals=" + DEFAULT_ADDRESS);

        // Get all the plantList where address equals to UPDATED_ADDRESS
        defaultPlantShouldNotBeFound("address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllPlantsByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        plantRepository.saveAndFlush(plant);

        // Get all the plantList where address in DEFAULT_ADDRESS or UPDATED_ADDRESS
        defaultPlantShouldBeFound("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS);

        // Get all the plantList where address equals to UPDATED_ADDRESS
        defaultPlantShouldNotBeFound("address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllPlantsByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        plantRepository.saveAndFlush(plant);

        // Get all the plantList where address is not null
        defaultPlantShouldBeFound("address.specified=true");

        // Get all the plantList where address is null
        defaultPlantShouldNotBeFound("address.specified=false");
    }

    @Test
    @Transactional
    void getAllPlantsByAddressContainsSomething() throws Exception {
        // Initialize the database
        plantRepository.saveAndFlush(plant);

        // Get all the plantList where address contains DEFAULT_ADDRESS
        defaultPlantShouldBeFound("address.contains=" + DEFAULT_ADDRESS);

        // Get all the plantList where address contains UPDATED_ADDRESS
        defaultPlantShouldNotBeFound("address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllPlantsByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        plantRepository.saveAndFlush(plant);

        // Get all the plantList where address does not contain DEFAULT_ADDRESS
        defaultPlantShouldNotBeFound("address.doesNotContain=" + DEFAULT_ADDRESS);

        // Get all the plantList where address does not contain UPDATED_ADDRESS
        defaultPlantShouldBeFound("address.doesNotContain=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllPlantsByLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        plantRepository.saveAndFlush(plant);

        // Get all the plantList where location equals to DEFAULT_LOCATION
        defaultPlantShouldBeFound("location.equals=" + DEFAULT_LOCATION);

        // Get all the plantList where location equals to UPDATED_LOCATION
        defaultPlantShouldNotBeFound("location.equals=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllPlantsByLocationIsInShouldWork() throws Exception {
        // Initialize the database
        plantRepository.saveAndFlush(plant);

        // Get all the plantList where location in DEFAULT_LOCATION or UPDATED_LOCATION
        defaultPlantShouldBeFound("location.in=" + DEFAULT_LOCATION + "," + UPDATED_LOCATION);

        // Get all the plantList where location equals to UPDATED_LOCATION
        defaultPlantShouldNotBeFound("location.in=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllPlantsByLocationIsNullOrNotNull() throws Exception {
        // Initialize the database
        plantRepository.saveAndFlush(plant);

        // Get all the plantList where location is not null
        defaultPlantShouldBeFound("location.specified=true");

        // Get all the plantList where location is null
        defaultPlantShouldNotBeFound("location.specified=false");
    }

    @Test
    @Transactional
    void getAllPlantsByLocationContainsSomething() throws Exception {
        // Initialize the database
        plantRepository.saveAndFlush(plant);

        // Get all the plantList where location contains DEFAULT_LOCATION
        defaultPlantShouldBeFound("location.contains=" + DEFAULT_LOCATION);

        // Get all the plantList where location contains UPDATED_LOCATION
        defaultPlantShouldNotBeFound("location.contains=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllPlantsByLocationNotContainsSomething() throws Exception {
        // Initialize the database
        plantRepository.saveAndFlush(plant);

        // Get all the plantList where location does not contain DEFAULT_LOCATION
        defaultPlantShouldNotBeFound("location.doesNotContain=" + DEFAULT_LOCATION);

        // Get all the plantList where location does not contain UPDATED_LOCATION
        defaultPlantShouldBeFound("location.doesNotContain=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllPlantsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        plantRepository.saveAndFlush(plant);

        // Get all the plantList where createdAt equals to DEFAULT_CREATED_AT
        defaultPlantShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the plantList where createdAt equals to UPDATED_CREATED_AT
        defaultPlantShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllPlantsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        plantRepository.saveAndFlush(plant);

        // Get all the plantList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultPlantShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the plantList where createdAt equals to UPDATED_CREATED_AT
        defaultPlantShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllPlantsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        plantRepository.saveAndFlush(plant);

        // Get all the plantList where createdAt is not null
        defaultPlantShouldBeFound("createdAt.specified=true");

        // Get all the plantList where createdAt is null
        defaultPlantShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllPlantsByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        plantRepository.saveAndFlush(plant);

        // Get all the plantList where createdAt is greater than or equal to DEFAULT_CREATED_AT
        defaultPlantShouldBeFound("createdAt.greaterThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the plantList where createdAt is greater than or equal to UPDATED_CREATED_AT
        defaultPlantShouldNotBeFound("createdAt.greaterThanOrEqual=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllPlantsByCreatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        plantRepository.saveAndFlush(plant);

        // Get all the plantList where createdAt is less than or equal to DEFAULT_CREATED_AT
        defaultPlantShouldBeFound("createdAt.lessThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the plantList where createdAt is less than or equal to SMALLER_CREATED_AT
        defaultPlantShouldNotBeFound("createdAt.lessThanOrEqual=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllPlantsByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        plantRepository.saveAndFlush(plant);

        // Get all the plantList where createdAt is less than DEFAULT_CREATED_AT
        defaultPlantShouldNotBeFound("createdAt.lessThan=" + DEFAULT_CREATED_AT);

        // Get all the plantList where createdAt is less than UPDATED_CREATED_AT
        defaultPlantShouldBeFound("createdAt.lessThan=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllPlantsByCreatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        plantRepository.saveAndFlush(plant);

        // Get all the plantList where createdAt is greater than DEFAULT_CREATED_AT
        defaultPlantShouldNotBeFound("createdAt.greaterThan=" + DEFAULT_CREATED_AT);

        // Get all the plantList where createdAt is greater than SMALLER_CREATED_AT
        defaultPlantShouldBeFound("createdAt.greaterThan=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllPlantsByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        plantRepository.saveAndFlush(plant);

        // Get all the plantList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultPlantShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the plantList where updatedAt equals to UPDATED_UPDATED_AT
        defaultPlantShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllPlantsByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        plantRepository.saveAndFlush(plant);

        // Get all the plantList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultPlantShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the plantList where updatedAt equals to UPDATED_UPDATED_AT
        defaultPlantShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllPlantsByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        plantRepository.saveAndFlush(plant);

        // Get all the plantList where updatedAt is not null
        defaultPlantShouldBeFound("updatedAt.specified=true");

        // Get all the plantList where updatedAt is null
        defaultPlantShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllPlantsByUpdatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        plantRepository.saveAndFlush(plant);

        // Get all the plantList where updatedAt is greater than or equal to DEFAULT_UPDATED_AT
        defaultPlantShouldBeFound("updatedAt.greaterThanOrEqual=" + DEFAULT_UPDATED_AT);

        // Get all the plantList where updatedAt is greater than or equal to UPDATED_UPDATED_AT
        defaultPlantShouldNotBeFound("updatedAt.greaterThanOrEqual=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllPlantsByUpdatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        plantRepository.saveAndFlush(plant);

        // Get all the plantList where updatedAt is less than or equal to DEFAULT_UPDATED_AT
        defaultPlantShouldBeFound("updatedAt.lessThanOrEqual=" + DEFAULT_UPDATED_AT);

        // Get all the plantList where updatedAt is less than or equal to SMALLER_UPDATED_AT
        defaultPlantShouldNotBeFound("updatedAt.lessThanOrEqual=" + SMALLER_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllPlantsByUpdatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        plantRepository.saveAndFlush(plant);

        // Get all the plantList where updatedAt is less than DEFAULT_UPDATED_AT
        defaultPlantShouldNotBeFound("updatedAt.lessThan=" + DEFAULT_UPDATED_AT);

        // Get all the plantList where updatedAt is less than UPDATED_UPDATED_AT
        defaultPlantShouldBeFound("updatedAt.lessThan=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllPlantsByUpdatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        plantRepository.saveAndFlush(plant);

        // Get all the plantList where updatedAt is greater than DEFAULT_UPDATED_AT
        defaultPlantShouldNotBeFound("updatedAt.greaterThan=" + DEFAULT_UPDATED_AT);

        // Get all the plantList where updatedAt is greater than SMALLER_UPDATED_AT
        defaultPlantShouldBeFound("updatedAt.greaterThan=" + SMALLER_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllPlantsBySiteIsEqualToSomething() throws Exception {
        Site site;
        if (TestUtil.findAll(em, Site.class).isEmpty()) {
            plantRepository.saveAndFlush(plant);
            site = SiteResourceIT.createEntity(em);
        } else {
            site = TestUtil.findAll(em, Site.class).get(0);
        }
        em.persist(site);
        em.flush();
        plant.setSite(site);
        plantRepository.saveAndFlush(plant);
        Long siteId = site.getId();
        // Get all the plantList where site equals to siteId
        defaultPlantShouldBeFound("siteId.equals=" + siteId);

        // Get all the plantList where site equals to (siteId + 1)
        defaultPlantShouldNotBeFound("siteId.equals=" + (siteId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPlantShouldBeFound(String filter) throws Exception {
        restPlantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(plant.getId().intValue())))
            .andExpect(jsonPath("$.[*].reference").value(hasItem(DEFAULT_REFERENCE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));

        // Check, that the count call also returns 1
        restPlantMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPlantShouldNotBeFound(String filter) throws Exception {
        restPlantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPlantMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPlant() throws Exception {
        // Get the plant
        restPlantMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPlant() throws Exception {
        // Initialize the database
        plantRepository.saveAndFlush(plant);

        int databaseSizeBeforeUpdate = plantRepository.findAll().size();

        // Update the plant
        Plant updatedPlant = plantRepository.findById(plant.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPlant are not directly saved in db
        em.detach(updatedPlant);
        updatedPlant
            .reference(UPDATED_REFERENCE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .address(UPDATED_ADDRESS)
            .location(UPDATED_LOCATION)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        PlantDTO plantDTO = plantMapper.toDto(updatedPlant);

        restPlantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, plantDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(plantDTO))
            )
            .andExpect(status().isOk());

        // Validate the Plant in the database
        List<Plant> plantList = plantRepository.findAll();
        assertThat(plantList).hasSize(databaseSizeBeforeUpdate);
        Plant testPlant = plantList.get(plantList.size() - 1);
        assertThat(testPlant.getReference()).isEqualTo(UPDATED_REFERENCE);
        assertThat(testPlant.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPlant.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPlant.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testPlant.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testPlant.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testPlant.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingPlant() throws Exception {
        int databaseSizeBeforeUpdate = plantRepository.findAll().size();
        plant.setId(longCount.incrementAndGet());

        // Create the Plant
        PlantDTO plantDTO = plantMapper.toDto(plant);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, plantDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(plantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Plant in the database
        List<Plant> plantList = plantRepository.findAll();
        assertThat(plantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPlant() throws Exception {
        int databaseSizeBeforeUpdate = plantRepository.findAll().size();
        plant.setId(longCount.incrementAndGet());

        // Create the Plant
        PlantDTO plantDTO = plantMapper.toDto(plant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(plantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Plant in the database
        List<Plant> plantList = plantRepository.findAll();
        assertThat(plantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPlant() throws Exception {
        int databaseSizeBeforeUpdate = plantRepository.findAll().size();
        plant.setId(longCount.incrementAndGet());

        // Create the Plant
        PlantDTO plantDTO = plantMapper.toDto(plant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlantMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(plantDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Plant in the database
        List<Plant> plantList = plantRepository.findAll();
        assertThat(plantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePlantWithPatch() throws Exception {
        // Initialize the database
        plantRepository.saveAndFlush(plant);

        int databaseSizeBeforeUpdate = plantRepository.findAll().size();

        // Update the plant using partial update
        Plant partialUpdatedPlant = new Plant();
        partialUpdatedPlant.setId(plant.getId());

        partialUpdatedPlant.reference(UPDATED_REFERENCE).name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restPlantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlant.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPlant))
            )
            .andExpect(status().isOk());

        // Validate the Plant in the database
        List<Plant> plantList = plantRepository.findAll();
        assertThat(plantList).hasSize(databaseSizeBeforeUpdate);
        Plant testPlant = plantList.get(plantList.size() - 1);
        assertThat(testPlant.getReference()).isEqualTo(UPDATED_REFERENCE);
        assertThat(testPlant.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPlant.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPlant.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testPlant.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testPlant.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testPlant.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdatePlantWithPatch() throws Exception {
        // Initialize the database
        plantRepository.saveAndFlush(plant);

        int databaseSizeBeforeUpdate = plantRepository.findAll().size();

        // Update the plant using partial update
        Plant partialUpdatedPlant = new Plant();
        partialUpdatedPlant.setId(plant.getId());

        partialUpdatedPlant
            .reference(UPDATED_REFERENCE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .address(UPDATED_ADDRESS)
            .location(UPDATED_LOCATION)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restPlantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlant.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPlant))
            )
            .andExpect(status().isOk());

        // Validate the Plant in the database
        List<Plant> plantList = plantRepository.findAll();
        assertThat(plantList).hasSize(databaseSizeBeforeUpdate);
        Plant testPlant = plantList.get(plantList.size() - 1);
        assertThat(testPlant.getReference()).isEqualTo(UPDATED_REFERENCE);
        assertThat(testPlant.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPlant.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPlant.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testPlant.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testPlant.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testPlant.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingPlant() throws Exception {
        int databaseSizeBeforeUpdate = plantRepository.findAll().size();
        plant.setId(longCount.incrementAndGet());

        // Create the Plant
        PlantDTO plantDTO = plantMapper.toDto(plant);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, plantDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(plantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Plant in the database
        List<Plant> plantList = plantRepository.findAll();
        assertThat(plantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPlant() throws Exception {
        int databaseSizeBeforeUpdate = plantRepository.findAll().size();
        plant.setId(longCount.incrementAndGet());

        // Create the Plant
        PlantDTO plantDTO = plantMapper.toDto(plant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(plantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Plant in the database
        List<Plant> plantList = plantRepository.findAll();
        assertThat(plantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPlant() throws Exception {
        int databaseSizeBeforeUpdate = plantRepository.findAll().size();
        plant.setId(longCount.incrementAndGet());

        // Create the Plant
        PlantDTO plantDTO = plantMapper.toDto(plant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlantMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(plantDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Plant in the database
        List<Plant> plantList = plantRepository.findAll();
        assertThat(plantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePlant() throws Exception {
        // Initialize the database
        plantRepository.saveAndFlush(plant);

        int databaseSizeBeforeDelete = plantRepository.findAll().size();

        // Delete the plant
        restPlantMockMvc
            .perform(delete(ENTITY_API_URL_ID, plant.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Plant> plantList = plantRepository.findAll();
        assertThat(plantList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
