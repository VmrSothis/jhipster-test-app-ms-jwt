package com.sothis.tech.web.rest;

import static com.sothis.tech.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sothis.tech.IntegrationTest;
import com.sothis.tech.domain.Plant;
import com.sothis.tech.domain.PlantArea;
import com.sothis.tech.repository.PlantAreaRepository;
import com.sothis.tech.service.PlantAreaService;
import com.sothis.tech.service.dto.PlantAreaDTO;
import com.sothis.tech.service.mapper.PlantAreaMapper;
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
 * Integration tests for the {@link PlantAreaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PlantAreaResourceIT {

    private static final String DEFAULT_REFERENCE = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/plant-areas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PlantAreaRepository plantAreaRepository;

    @Mock
    private PlantAreaRepository plantAreaRepositoryMock;

    @Autowired
    private PlantAreaMapper plantAreaMapper;

    @Mock
    private PlantAreaService plantAreaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPlantAreaMockMvc;

    private PlantArea plantArea;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PlantArea createEntity(EntityManager em) {
        PlantArea plantArea = new PlantArea()
            .reference(DEFAULT_REFERENCE)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        // Add required entity
        Plant plant;
        if (TestUtil.findAll(em, Plant.class).isEmpty()) {
            plant = PlantResourceIT.createEntity(em);
            em.persist(plant);
            em.flush();
        } else {
            plant = TestUtil.findAll(em, Plant.class).get(0);
        }
        plantArea.setPlant(plant);
        return plantArea;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PlantArea createUpdatedEntity(EntityManager em) {
        PlantArea plantArea = new PlantArea()
            .reference(UPDATED_REFERENCE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        // Add required entity
        Plant plant;
        if (TestUtil.findAll(em, Plant.class).isEmpty()) {
            plant = PlantResourceIT.createUpdatedEntity(em);
            em.persist(plant);
            em.flush();
        } else {
            plant = TestUtil.findAll(em, Plant.class).get(0);
        }
        plantArea.setPlant(plant);
        return plantArea;
    }

    @BeforeEach
    public void initTest() {
        plantArea = createEntity(em);
    }

    @Test
    @Transactional
    void createPlantArea() throws Exception {
        int databaseSizeBeforeCreate = plantAreaRepository.findAll().size();
        // Create the PlantArea
        PlantAreaDTO plantAreaDTO = plantAreaMapper.toDto(plantArea);
        restPlantAreaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(plantAreaDTO)))
            .andExpect(status().isCreated());

        // Validate the PlantArea in the database
        List<PlantArea> plantAreaList = plantAreaRepository.findAll();
        assertThat(plantAreaList).hasSize(databaseSizeBeforeCreate + 1);
        PlantArea testPlantArea = plantAreaList.get(plantAreaList.size() - 1);
        assertThat(testPlantArea.getReference()).isEqualTo(DEFAULT_REFERENCE);
        assertThat(testPlantArea.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPlantArea.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPlantArea.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testPlantArea.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createPlantAreaWithExistingId() throws Exception {
        // Create the PlantArea with an existing ID
        plantArea.setId(1L);
        PlantAreaDTO plantAreaDTO = plantAreaMapper.toDto(plantArea);

        int databaseSizeBeforeCreate = plantAreaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlantAreaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(plantAreaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PlantArea in the database
        List<PlantArea> plantAreaList = plantAreaRepository.findAll();
        assertThat(plantAreaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkReferenceIsRequired() throws Exception {
        int databaseSizeBeforeTest = plantAreaRepository.findAll().size();
        // set the field null
        plantArea.setReference(null);

        // Create the PlantArea, which fails.
        PlantAreaDTO plantAreaDTO = plantAreaMapper.toDto(plantArea);

        restPlantAreaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(plantAreaDTO)))
            .andExpect(status().isBadRequest());

        List<PlantArea> plantAreaList = plantAreaRepository.findAll();
        assertThat(plantAreaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = plantAreaRepository.findAll().size();
        // set the field null
        plantArea.setName(null);

        // Create the PlantArea, which fails.
        PlantAreaDTO plantAreaDTO = plantAreaMapper.toDto(plantArea);

        restPlantAreaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(plantAreaDTO)))
            .andExpect(status().isBadRequest());

        List<PlantArea> plantAreaList = plantAreaRepository.findAll();
        assertThat(plantAreaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPlantAreas() throws Exception {
        // Initialize the database
        plantAreaRepository.saveAndFlush(plantArea);

        // Get all the plantAreaList
        restPlantAreaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(plantArea.getId().intValue())))
            .andExpect(jsonPath("$.[*].reference").value(hasItem(DEFAULT_REFERENCE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPlantAreasWithEagerRelationshipsIsEnabled() throws Exception {
        when(plantAreaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPlantAreaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(plantAreaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPlantAreasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(plantAreaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPlantAreaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(plantAreaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPlantArea() throws Exception {
        // Initialize the database
        plantAreaRepository.saveAndFlush(plantArea);

        // Get the plantArea
        restPlantAreaMockMvc
            .perform(get(ENTITY_API_URL_ID, plantArea.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(plantArea.getId().intValue()))
            .andExpect(jsonPath("$.reference").value(DEFAULT_REFERENCE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.updatedAt").value(sameInstant(DEFAULT_UPDATED_AT)));
    }

    @Test
    @Transactional
    void getPlantAreasByIdFiltering() throws Exception {
        // Initialize the database
        plantAreaRepository.saveAndFlush(plantArea);

        Long id = plantArea.getId();

        defaultPlantAreaShouldBeFound("id.equals=" + id);
        defaultPlantAreaShouldNotBeFound("id.notEquals=" + id);

        defaultPlantAreaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPlantAreaShouldNotBeFound("id.greaterThan=" + id);

        defaultPlantAreaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPlantAreaShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPlantAreasByReferenceIsEqualToSomething() throws Exception {
        // Initialize the database
        plantAreaRepository.saveAndFlush(plantArea);

        // Get all the plantAreaList where reference equals to DEFAULT_REFERENCE
        defaultPlantAreaShouldBeFound("reference.equals=" + DEFAULT_REFERENCE);

        // Get all the plantAreaList where reference equals to UPDATED_REFERENCE
        defaultPlantAreaShouldNotBeFound("reference.equals=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    void getAllPlantAreasByReferenceIsInShouldWork() throws Exception {
        // Initialize the database
        plantAreaRepository.saveAndFlush(plantArea);

        // Get all the plantAreaList where reference in DEFAULT_REFERENCE or UPDATED_REFERENCE
        defaultPlantAreaShouldBeFound("reference.in=" + DEFAULT_REFERENCE + "," + UPDATED_REFERENCE);

        // Get all the plantAreaList where reference equals to UPDATED_REFERENCE
        defaultPlantAreaShouldNotBeFound("reference.in=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    void getAllPlantAreasByReferenceIsNullOrNotNull() throws Exception {
        // Initialize the database
        plantAreaRepository.saveAndFlush(plantArea);

        // Get all the plantAreaList where reference is not null
        defaultPlantAreaShouldBeFound("reference.specified=true");

        // Get all the plantAreaList where reference is null
        defaultPlantAreaShouldNotBeFound("reference.specified=false");
    }

    @Test
    @Transactional
    void getAllPlantAreasByReferenceContainsSomething() throws Exception {
        // Initialize the database
        plantAreaRepository.saveAndFlush(plantArea);

        // Get all the plantAreaList where reference contains DEFAULT_REFERENCE
        defaultPlantAreaShouldBeFound("reference.contains=" + DEFAULT_REFERENCE);

        // Get all the plantAreaList where reference contains UPDATED_REFERENCE
        defaultPlantAreaShouldNotBeFound("reference.contains=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    void getAllPlantAreasByReferenceNotContainsSomething() throws Exception {
        // Initialize the database
        plantAreaRepository.saveAndFlush(plantArea);

        // Get all the plantAreaList where reference does not contain DEFAULT_REFERENCE
        defaultPlantAreaShouldNotBeFound("reference.doesNotContain=" + DEFAULT_REFERENCE);

        // Get all the plantAreaList where reference does not contain UPDATED_REFERENCE
        defaultPlantAreaShouldBeFound("reference.doesNotContain=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    void getAllPlantAreasByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        plantAreaRepository.saveAndFlush(plantArea);

        // Get all the plantAreaList where name equals to DEFAULT_NAME
        defaultPlantAreaShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the plantAreaList where name equals to UPDATED_NAME
        defaultPlantAreaShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPlantAreasByNameIsInShouldWork() throws Exception {
        // Initialize the database
        plantAreaRepository.saveAndFlush(plantArea);

        // Get all the plantAreaList where name in DEFAULT_NAME or UPDATED_NAME
        defaultPlantAreaShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the plantAreaList where name equals to UPDATED_NAME
        defaultPlantAreaShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPlantAreasByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        plantAreaRepository.saveAndFlush(plantArea);

        // Get all the plantAreaList where name is not null
        defaultPlantAreaShouldBeFound("name.specified=true");

        // Get all the plantAreaList where name is null
        defaultPlantAreaShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllPlantAreasByNameContainsSomething() throws Exception {
        // Initialize the database
        plantAreaRepository.saveAndFlush(plantArea);

        // Get all the plantAreaList where name contains DEFAULT_NAME
        defaultPlantAreaShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the plantAreaList where name contains UPDATED_NAME
        defaultPlantAreaShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPlantAreasByNameNotContainsSomething() throws Exception {
        // Initialize the database
        plantAreaRepository.saveAndFlush(plantArea);

        // Get all the plantAreaList where name does not contain DEFAULT_NAME
        defaultPlantAreaShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the plantAreaList where name does not contain UPDATED_NAME
        defaultPlantAreaShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPlantAreasByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        plantAreaRepository.saveAndFlush(plantArea);

        // Get all the plantAreaList where description equals to DEFAULT_DESCRIPTION
        defaultPlantAreaShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the plantAreaList where description equals to UPDATED_DESCRIPTION
        defaultPlantAreaShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPlantAreasByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        plantAreaRepository.saveAndFlush(plantArea);

        // Get all the plantAreaList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultPlantAreaShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the plantAreaList where description equals to UPDATED_DESCRIPTION
        defaultPlantAreaShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPlantAreasByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        plantAreaRepository.saveAndFlush(plantArea);

        // Get all the plantAreaList where description is not null
        defaultPlantAreaShouldBeFound("description.specified=true");

        // Get all the plantAreaList where description is null
        defaultPlantAreaShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllPlantAreasByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        plantAreaRepository.saveAndFlush(plantArea);

        // Get all the plantAreaList where description contains DEFAULT_DESCRIPTION
        defaultPlantAreaShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the plantAreaList where description contains UPDATED_DESCRIPTION
        defaultPlantAreaShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPlantAreasByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        plantAreaRepository.saveAndFlush(plantArea);

        // Get all the plantAreaList where description does not contain DEFAULT_DESCRIPTION
        defaultPlantAreaShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the plantAreaList where description does not contain UPDATED_DESCRIPTION
        defaultPlantAreaShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPlantAreasByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        plantAreaRepository.saveAndFlush(plantArea);

        // Get all the plantAreaList where createdAt equals to DEFAULT_CREATED_AT
        defaultPlantAreaShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the plantAreaList where createdAt equals to UPDATED_CREATED_AT
        defaultPlantAreaShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllPlantAreasByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        plantAreaRepository.saveAndFlush(plantArea);

        // Get all the plantAreaList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultPlantAreaShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the plantAreaList where createdAt equals to UPDATED_CREATED_AT
        defaultPlantAreaShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllPlantAreasByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        plantAreaRepository.saveAndFlush(plantArea);

        // Get all the plantAreaList where createdAt is not null
        defaultPlantAreaShouldBeFound("createdAt.specified=true");

        // Get all the plantAreaList where createdAt is null
        defaultPlantAreaShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllPlantAreasByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        plantAreaRepository.saveAndFlush(plantArea);

        // Get all the plantAreaList where createdAt is greater than or equal to DEFAULT_CREATED_AT
        defaultPlantAreaShouldBeFound("createdAt.greaterThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the plantAreaList where createdAt is greater than or equal to UPDATED_CREATED_AT
        defaultPlantAreaShouldNotBeFound("createdAt.greaterThanOrEqual=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllPlantAreasByCreatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        plantAreaRepository.saveAndFlush(plantArea);

        // Get all the plantAreaList where createdAt is less than or equal to DEFAULT_CREATED_AT
        defaultPlantAreaShouldBeFound("createdAt.lessThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the plantAreaList where createdAt is less than or equal to SMALLER_CREATED_AT
        defaultPlantAreaShouldNotBeFound("createdAt.lessThanOrEqual=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllPlantAreasByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        plantAreaRepository.saveAndFlush(plantArea);

        // Get all the plantAreaList where createdAt is less than DEFAULT_CREATED_AT
        defaultPlantAreaShouldNotBeFound("createdAt.lessThan=" + DEFAULT_CREATED_AT);

        // Get all the plantAreaList where createdAt is less than UPDATED_CREATED_AT
        defaultPlantAreaShouldBeFound("createdAt.lessThan=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllPlantAreasByCreatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        plantAreaRepository.saveAndFlush(plantArea);

        // Get all the plantAreaList where createdAt is greater than DEFAULT_CREATED_AT
        defaultPlantAreaShouldNotBeFound("createdAt.greaterThan=" + DEFAULT_CREATED_AT);

        // Get all the plantAreaList where createdAt is greater than SMALLER_CREATED_AT
        defaultPlantAreaShouldBeFound("createdAt.greaterThan=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllPlantAreasByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        plantAreaRepository.saveAndFlush(plantArea);

        // Get all the plantAreaList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultPlantAreaShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the plantAreaList where updatedAt equals to UPDATED_UPDATED_AT
        defaultPlantAreaShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllPlantAreasByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        plantAreaRepository.saveAndFlush(plantArea);

        // Get all the plantAreaList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultPlantAreaShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the plantAreaList where updatedAt equals to UPDATED_UPDATED_AT
        defaultPlantAreaShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllPlantAreasByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        plantAreaRepository.saveAndFlush(plantArea);

        // Get all the plantAreaList where updatedAt is not null
        defaultPlantAreaShouldBeFound("updatedAt.specified=true");

        // Get all the plantAreaList where updatedAt is null
        defaultPlantAreaShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllPlantAreasByUpdatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        plantAreaRepository.saveAndFlush(plantArea);

        // Get all the plantAreaList where updatedAt is greater than or equal to DEFAULT_UPDATED_AT
        defaultPlantAreaShouldBeFound("updatedAt.greaterThanOrEqual=" + DEFAULT_UPDATED_AT);

        // Get all the plantAreaList where updatedAt is greater than or equal to UPDATED_UPDATED_AT
        defaultPlantAreaShouldNotBeFound("updatedAt.greaterThanOrEqual=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllPlantAreasByUpdatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        plantAreaRepository.saveAndFlush(plantArea);

        // Get all the plantAreaList where updatedAt is less than or equal to DEFAULT_UPDATED_AT
        defaultPlantAreaShouldBeFound("updatedAt.lessThanOrEqual=" + DEFAULT_UPDATED_AT);

        // Get all the plantAreaList where updatedAt is less than or equal to SMALLER_UPDATED_AT
        defaultPlantAreaShouldNotBeFound("updatedAt.lessThanOrEqual=" + SMALLER_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllPlantAreasByUpdatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        plantAreaRepository.saveAndFlush(plantArea);

        // Get all the plantAreaList where updatedAt is less than DEFAULT_UPDATED_AT
        defaultPlantAreaShouldNotBeFound("updatedAt.lessThan=" + DEFAULT_UPDATED_AT);

        // Get all the plantAreaList where updatedAt is less than UPDATED_UPDATED_AT
        defaultPlantAreaShouldBeFound("updatedAt.lessThan=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllPlantAreasByUpdatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        plantAreaRepository.saveAndFlush(plantArea);

        // Get all the plantAreaList where updatedAt is greater than DEFAULT_UPDATED_AT
        defaultPlantAreaShouldNotBeFound("updatedAt.greaterThan=" + DEFAULT_UPDATED_AT);

        // Get all the plantAreaList where updatedAt is greater than SMALLER_UPDATED_AT
        defaultPlantAreaShouldBeFound("updatedAt.greaterThan=" + SMALLER_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllPlantAreasByPlantIsEqualToSomething() throws Exception {
        Plant plant;
        if (TestUtil.findAll(em, Plant.class).isEmpty()) {
            plantAreaRepository.saveAndFlush(plantArea);
            plant = PlantResourceIT.createEntity(em);
        } else {
            plant = TestUtil.findAll(em, Plant.class).get(0);
        }
        em.persist(plant);
        em.flush();
        plantArea.setPlant(plant);
        plantAreaRepository.saveAndFlush(plantArea);
        Long plantId = plant.getId();
        // Get all the plantAreaList where plant equals to plantId
        defaultPlantAreaShouldBeFound("plantId.equals=" + plantId);

        // Get all the plantAreaList where plant equals to (plantId + 1)
        defaultPlantAreaShouldNotBeFound("plantId.equals=" + (plantId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPlantAreaShouldBeFound(String filter) throws Exception {
        restPlantAreaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(plantArea.getId().intValue())))
            .andExpect(jsonPath("$.[*].reference").value(hasItem(DEFAULT_REFERENCE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));

        // Check, that the count call also returns 1
        restPlantAreaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPlantAreaShouldNotBeFound(String filter) throws Exception {
        restPlantAreaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPlantAreaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPlantArea() throws Exception {
        // Get the plantArea
        restPlantAreaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPlantArea() throws Exception {
        // Initialize the database
        plantAreaRepository.saveAndFlush(plantArea);

        int databaseSizeBeforeUpdate = plantAreaRepository.findAll().size();

        // Update the plantArea
        PlantArea updatedPlantArea = plantAreaRepository.findById(plantArea.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPlantArea are not directly saved in db
        em.detach(updatedPlantArea);
        updatedPlantArea
            .reference(UPDATED_REFERENCE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        PlantAreaDTO plantAreaDTO = plantAreaMapper.toDto(updatedPlantArea);

        restPlantAreaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, plantAreaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(plantAreaDTO))
            )
            .andExpect(status().isOk());

        // Validate the PlantArea in the database
        List<PlantArea> plantAreaList = plantAreaRepository.findAll();
        assertThat(plantAreaList).hasSize(databaseSizeBeforeUpdate);
        PlantArea testPlantArea = plantAreaList.get(plantAreaList.size() - 1);
        assertThat(testPlantArea.getReference()).isEqualTo(UPDATED_REFERENCE);
        assertThat(testPlantArea.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPlantArea.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPlantArea.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testPlantArea.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingPlantArea() throws Exception {
        int databaseSizeBeforeUpdate = plantAreaRepository.findAll().size();
        plantArea.setId(longCount.incrementAndGet());

        // Create the PlantArea
        PlantAreaDTO plantAreaDTO = plantAreaMapper.toDto(plantArea);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlantAreaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, plantAreaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(plantAreaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlantArea in the database
        List<PlantArea> plantAreaList = plantAreaRepository.findAll();
        assertThat(plantAreaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPlantArea() throws Exception {
        int databaseSizeBeforeUpdate = plantAreaRepository.findAll().size();
        plantArea.setId(longCount.incrementAndGet());

        // Create the PlantArea
        PlantAreaDTO plantAreaDTO = plantAreaMapper.toDto(plantArea);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlantAreaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(plantAreaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlantArea in the database
        List<PlantArea> plantAreaList = plantAreaRepository.findAll();
        assertThat(plantAreaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPlantArea() throws Exception {
        int databaseSizeBeforeUpdate = plantAreaRepository.findAll().size();
        plantArea.setId(longCount.incrementAndGet());

        // Create the PlantArea
        PlantAreaDTO plantAreaDTO = plantAreaMapper.toDto(plantArea);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlantAreaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(plantAreaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PlantArea in the database
        List<PlantArea> plantAreaList = plantAreaRepository.findAll();
        assertThat(plantAreaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePlantAreaWithPatch() throws Exception {
        // Initialize the database
        plantAreaRepository.saveAndFlush(plantArea);

        int databaseSizeBeforeUpdate = plantAreaRepository.findAll().size();

        // Update the plantArea using partial update
        PlantArea partialUpdatedPlantArea = new PlantArea();
        partialUpdatedPlantArea.setId(plantArea.getId());

        partialUpdatedPlantArea.reference(UPDATED_REFERENCE).updatedAt(UPDATED_UPDATED_AT);

        restPlantAreaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlantArea.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPlantArea))
            )
            .andExpect(status().isOk());

        // Validate the PlantArea in the database
        List<PlantArea> plantAreaList = plantAreaRepository.findAll();
        assertThat(plantAreaList).hasSize(databaseSizeBeforeUpdate);
        PlantArea testPlantArea = plantAreaList.get(plantAreaList.size() - 1);
        assertThat(testPlantArea.getReference()).isEqualTo(UPDATED_REFERENCE);
        assertThat(testPlantArea.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPlantArea.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPlantArea.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testPlantArea.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdatePlantAreaWithPatch() throws Exception {
        // Initialize the database
        plantAreaRepository.saveAndFlush(plantArea);

        int databaseSizeBeforeUpdate = plantAreaRepository.findAll().size();

        // Update the plantArea using partial update
        PlantArea partialUpdatedPlantArea = new PlantArea();
        partialUpdatedPlantArea.setId(plantArea.getId());

        partialUpdatedPlantArea
            .reference(UPDATED_REFERENCE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restPlantAreaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlantArea.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPlantArea))
            )
            .andExpect(status().isOk());

        // Validate the PlantArea in the database
        List<PlantArea> plantAreaList = plantAreaRepository.findAll();
        assertThat(plantAreaList).hasSize(databaseSizeBeforeUpdate);
        PlantArea testPlantArea = plantAreaList.get(plantAreaList.size() - 1);
        assertThat(testPlantArea.getReference()).isEqualTo(UPDATED_REFERENCE);
        assertThat(testPlantArea.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPlantArea.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPlantArea.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testPlantArea.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingPlantArea() throws Exception {
        int databaseSizeBeforeUpdate = plantAreaRepository.findAll().size();
        plantArea.setId(longCount.incrementAndGet());

        // Create the PlantArea
        PlantAreaDTO plantAreaDTO = plantAreaMapper.toDto(plantArea);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlantAreaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, plantAreaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(plantAreaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlantArea in the database
        List<PlantArea> plantAreaList = plantAreaRepository.findAll();
        assertThat(plantAreaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPlantArea() throws Exception {
        int databaseSizeBeforeUpdate = plantAreaRepository.findAll().size();
        plantArea.setId(longCount.incrementAndGet());

        // Create the PlantArea
        PlantAreaDTO plantAreaDTO = plantAreaMapper.toDto(plantArea);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlantAreaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(plantAreaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlantArea in the database
        List<PlantArea> plantAreaList = plantAreaRepository.findAll();
        assertThat(plantAreaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPlantArea() throws Exception {
        int databaseSizeBeforeUpdate = plantAreaRepository.findAll().size();
        plantArea.setId(longCount.incrementAndGet());

        // Create the PlantArea
        PlantAreaDTO plantAreaDTO = plantAreaMapper.toDto(plantArea);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlantAreaMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(plantAreaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PlantArea in the database
        List<PlantArea> plantAreaList = plantAreaRepository.findAll();
        assertThat(plantAreaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePlantArea() throws Exception {
        // Initialize the database
        plantAreaRepository.saveAndFlush(plantArea);

        int databaseSizeBeforeDelete = plantAreaRepository.findAll().size();

        // Delete the plantArea
        restPlantAreaMockMvc
            .perform(delete(ENTITY_API_URL_ID, plantArea.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PlantArea> plantAreaList = plantAreaRepository.findAll();
        assertThat(plantAreaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
