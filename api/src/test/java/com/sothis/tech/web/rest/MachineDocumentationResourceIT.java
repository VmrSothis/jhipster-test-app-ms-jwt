package com.sothis.tech.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sothis.tech.IntegrationTest;
import com.sothis.tech.domain.Machine;
import com.sothis.tech.domain.MachineDocumentation;
import com.sothis.tech.domain.enumeration.AttachedType;
import com.sothis.tech.repository.MachineDocumentationRepository;
import com.sothis.tech.service.MachineDocumentationService;
import com.sothis.tech.service.dto.MachineDocumentationDTO;
import com.sothis.tech.service.mapper.MachineDocumentationMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link MachineDocumentationResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MachineDocumentationResourceIT {

    private static final String DEFAULT_REFERENCE = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final AttachedType DEFAULT_TYPE = AttachedType.PDF;
    private static final AttachedType UPDATED_TYPE = AttachedType.IMAGE;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/machine-documentations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MachineDocumentationRepository machineDocumentationRepository;

    @Mock
    private MachineDocumentationRepository machineDocumentationRepositoryMock;

    @Autowired
    private MachineDocumentationMapper machineDocumentationMapper;

    @Mock
    private MachineDocumentationService machineDocumentationServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMachineDocumentationMockMvc;

    private MachineDocumentation machineDocumentation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MachineDocumentation createEntity(EntityManager em) {
        MachineDocumentation machineDocumentation = new MachineDocumentation()
            .reference(DEFAULT_REFERENCE)
            .name(DEFAULT_NAME)
            .type(DEFAULT_TYPE)
            .description(DEFAULT_DESCRIPTION)
            .url(DEFAULT_URL);
        // Add required entity
        Machine machine;
        if (TestUtil.findAll(em, Machine.class).isEmpty()) {
            machine = MachineResourceIT.createEntity(em);
            em.persist(machine);
            em.flush();
        } else {
            machine = TestUtil.findAll(em, Machine.class).get(0);
        }
        machineDocumentation.setMachine(machine);
        return machineDocumentation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MachineDocumentation createUpdatedEntity(EntityManager em) {
        MachineDocumentation machineDocumentation = new MachineDocumentation()
            .reference(UPDATED_REFERENCE)
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .description(UPDATED_DESCRIPTION)
            .url(UPDATED_URL);
        // Add required entity
        Machine machine;
        if (TestUtil.findAll(em, Machine.class).isEmpty()) {
            machine = MachineResourceIT.createUpdatedEntity(em);
            em.persist(machine);
            em.flush();
        } else {
            machine = TestUtil.findAll(em, Machine.class).get(0);
        }
        machineDocumentation.setMachine(machine);
        return machineDocumentation;
    }

    @BeforeEach
    public void initTest() {
        machineDocumentation = createEntity(em);
    }

    @Test
    @Transactional
    void createMachineDocumentation() throws Exception {
        int databaseSizeBeforeCreate = machineDocumentationRepository.findAll().size();
        // Create the MachineDocumentation
        MachineDocumentationDTO machineDocumentationDTO = machineDocumentationMapper.toDto(machineDocumentation);
        restMachineDocumentationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(machineDocumentationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the MachineDocumentation in the database
        List<MachineDocumentation> machineDocumentationList = machineDocumentationRepository.findAll();
        assertThat(machineDocumentationList).hasSize(databaseSizeBeforeCreate + 1);
        MachineDocumentation testMachineDocumentation = machineDocumentationList.get(machineDocumentationList.size() - 1);
        assertThat(testMachineDocumentation.getReference()).isEqualTo(DEFAULT_REFERENCE);
        assertThat(testMachineDocumentation.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMachineDocumentation.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testMachineDocumentation.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testMachineDocumentation.getUrl()).isEqualTo(DEFAULT_URL);
    }

    @Test
    @Transactional
    void createMachineDocumentationWithExistingId() throws Exception {
        // Create the MachineDocumentation with an existing ID
        machineDocumentation.setId(1L);
        MachineDocumentationDTO machineDocumentationDTO = machineDocumentationMapper.toDto(machineDocumentation);

        int databaseSizeBeforeCreate = machineDocumentationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMachineDocumentationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(machineDocumentationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MachineDocumentation in the database
        List<MachineDocumentation> machineDocumentationList = machineDocumentationRepository.findAll();
        assertThat(machineDocumentationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkReferenceIsRequired() throws Exception {
        int databaseSizeBeforeTest = machineDocumentationRepository.findAll().size();
        // set the field null
        machineDocumentation.setReference(null);

        // Create the MachineDocumentation, which fails.
        MachineDocumentationDTO machineDocumentationDTO = machineDocumentationMapper.toDto(machineDocumentation);

        restMachineDocumentationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(machineDocumentationDTO))
            )
            .andExpect(status().isBadRequest());

        List<MachineDocumentation> machineDocumentationList = machineDocumentationRepository.findAll();
        assertThat(machineDocumentationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = machineDocumentationRepository.findAll().size();
        // set the field null
        machineDocumentation.setName(null);

        // Create the MachineDocumentation, which fails.
        MachineDocumentationDTO machineDocumentationDTO = machineDocumentationMapper.toDto(machineDocumentation);

        restMachineDocumentationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(machineDocumentationDTO))
            )
            .andExpect(status().isBadRequest());

        List<MachineDocumentation> machineDocumentationList = machineDocumentationRepository.findAll();
        assertThat(machineDocumentationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMachineDocumentations() throws Exception {
        // Initialize the database
        machineDocumentationRepository.saveAndFlush(machineDocumentation);

        // Get all the machineDocumentationList
        restMachineDocumentationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(machineDocumentation.getId().intValue())))
            .andExpect(jsonPath("$.[*].reference").value(hasItem(DEFAULT_REFERENCE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMachineDocumentationsWithEagerRelationshipsIsEnabled() throws Exception {
        when(machineDocumentationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMachineDocumentationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(machineDocumentationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMachineDocumentationsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(machineDocumentationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMachineDocumentationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(machineDocumentationRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getMachineDocumentation() throws Exception {
        // Initialize the database
        machineDocumentationRepository.saveAndFlush(machineDocumentation);

        // Get the machineDocumentation
        restMachineDocumentationMockMvc
            .perform(get(ENTITY_API_URL_ID, machineDocumentation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(machineDocumentation.getId().intValue()))
            .andExpect(jsonPath("$.reference").value(DEFAULT_REFERENCE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL));
    }

    @Test
    @Transactional
    void getMachineDocumentationsByIdFiltering() throws Exception {
        // Initialize the database
        machineDocumentationRepository.saveAndFlush(machineDocumentation);

        Long id = machineDocumentation.getId();

        defaultMachineDocumentationShouldBeFound("id.equals=" + id);
        defaultMachineDocumentationShouldNotBeFound("id.notEquals=" + id);

        defaultMachineDocumentationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMachineDocumentationShouldNotBeFound("id.greaterThan=" + id);

        defaultMachineDocumentationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMachineDocumentationShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMachineDocumentationsByReferenceIsEqualToSomething() throws Exception {
        // Initialize the database
        machineDocumentationRepository.saveAndFlush(machineDocumentation);

        // Get all the machineDocumentationList where reference equals to DEFAULT_REFERENCE
        defaultMachineDocumentationShouldBeFound("reference.equals=" + DEFAULT_REFERENCE);

        // Get all the machineDocumentationList where reference equals to UPDATED_REFERENCE
        defaultMachineDocumentationShouldNotBeFound("reference.equals=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    void getAllMachineDocumentationsByReferenceIsInShouldWork() throws Exception {
        // Initialize the database
        machineDocumentationRepository.saveAndFlush(machineDocumentation);

        // Get all the machineDocumentationList where reference in DEFAULT_REFERENCE or UPDATED_REFERENCE
        defaultMachineDocumentationShouldBeFound("reference.in=" + DEFAULT_REFERENCE + "," + UPDATED_REFERENCE);

        // Get all the machineDocumentationList where reference equals to UPDATED_REFERENCE
        defaultMachineDocumentationShouldNotBeFound("reference.in=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    void getAllMachineDocumentationsByReferenceIsNullOrNotNull() throws Exception {
        // Initialize the database
        machineDocumentationRepository.saveAndFlush(machineDocumentation);

        // Get all the machineDocumentationList where reference is not null
        defaultMachineDocumentationShouldBeFound("reference.specified=true");

        // Get all the machineDocumentationList where reference is null
        defaultMachineDocumentationShouldNotBeFound("reference.specified=false");
    }

    @Test
    @Transactional
    void getAllMachineDocumentationsByReferenceContainsSomething() throws Exception {
        // Initialize the database
        machineDocumentationRepository.saveAndFlush(machineDocumentation);

        // Get all the machineDocumentationList where reference contains DEFAULT_REFERENCE
        defaultMachineDocumentationShouldBeFound("reference.contains=" + DEFAULT_REFERENCE);

        // Get all the machineDocumentationList where reference contains UPDATED_REFERENCE
        defaultMachineDocumentationShouldNotBeFound("reference.contains=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    void getAllMachineDocumentationsByReferenceNotContainsSomething() throws Exception {
        // Initialize the database
        machineDocumentationRepository.saveAndFlush(machineDocumentation);

        // Get all the machineDocumentationList where reference does not contain DEFAULT_REFERENCE
        defaultMachineDocumentationShouldNotBeFound("reference.doesNotContain=" + DEFAULT_REFERENCE);

        // Get all the machineDocumentationList where reference does not contain UPDATED_REFERENCE
        defaultMachineDocumentationShouldBeFound("reference.doesNotContain=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    void getAllMachineDocumentationsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        machineDocumentationRepository.saveAndFlush(machineDocumentation);

        // Get all the machineDocumentationList where name equals to DEFAULT_NAME
        defaultMachineDocumentationShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the machineDocumentationList where name equals to UPDATED_NAME
        defaultMachineDocumentationShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMachineDocumentationsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        machineDocumentationRepository.saveAndFlush(machineDocumentation);

        // Get all the machineDocumentationList where name in DEFAULT_NAME or UPDATED_NAME
        defaultMachineDocumentationShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the machineDocumentationList where name equals to UPDATED_NAME
        defaultMachineDocumentationShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMachineDocumentationsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        machineDocumentationRepository.saveAndFlush(machineDocumentation);

        // Get all the machineDocumentationList where name is not null
        defaultMachineDocumentationShouldBeFound("name.specified=true");

        // Get all the machineDocumentationList where name is null
        defaultMachineDocumentationShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllMachineDocumentationsByNameContainsSomething() throws Exception {
        // Initialize the database
        machineDocumentationRepository.saveAndFlush(machineDocumentation);

        // Get all the machineDocumentationList where name contains DEFAULT_NAME
        defaultMachineDocumentationShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the machineDocumentationList where name contains UPDATED_NAME
        defaultMachineDocumentationShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMachineDocumentationsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        machineDocumentationRepository.saveAndFlush(machineDocumentation);

        // Get all the machineDocumentationList where name does not contain DEFAULT_NAME
        defaultMachineDocumentationShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the machineDocumentationList where name does not contain UPDATED_NAME
        defaultMachineDocumentationShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMachineDocumentationsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        machineDocumentationRepository.saveAndFlush(machineDocumentation);

        // Get all the machineDocumentationList where type equals to DEFAULT_TYPE
        defaultMachineDocumentationShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the machineDocumentationList where type equals to UPDATED_TYPE
        defaultMachineDocumentationShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllMachineDocumentationsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        machineDocumentationRepository.saveAndFlush(machineDocumentation);

        // Get all the machineDocumentationList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultMachineDocumentationShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the machineDocumentationList where type equals to UPDATED_TYPE
        defaultMachineDocumentationShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllMachineDocumentationsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        machineDocumentationRepository.saveAndFlush(machineDocumentation);

        // Get all the machineDocumentationList where type is not null
        defaultMachineDocumentationShouldBeFound("type.specified=true");

        // Get all the machineDocumentationList where type is null
        defaultMachineDocumentationShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    void getAllMachineDocumentationsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        machineDocumentationRepository.saveAndFlush(machineDocumentation);

        // Get all the machineDocumentationList where description equals to DEFAULT_DESCRIPTION
        defaultMachineDocumentationShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the machineDocumentationList where description equals to UPDATED_DESCRIPTION
        defaultMachineDocumentationShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMachineDocumentationsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        machineDocumentationRepository.saveAndFlush(machineDocumentation);

        // Get all the machineDocumentationList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultMachineDocumentationShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the machineDocumentationList where description equals to UPDATED_DESCRIPTION
        defaultMachineDocumentationShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMachineDocumentationsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        machineDocumentationRepository.saveAndFlush(machineDocumentation);

        // Get all the machineDocumentationList where description is not null
        defaultMachineDocumentationShouldBeFound("description.specified=true");

        // Get all the machineDocumentationList where description is null
        defaultMachineDocumentationShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllMachineDocumentationsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        machineDocumentationRepository.saveAndFlush(machineDocumentation);

        // Get all the machineDocumentationList where description contains DEFAULT_DESCRIPTION
        defaultMachineDocumentationShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the machineDocumentationList where description contains UPDATED_DESCRIPTION
        defaultMachineDocumentationShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMachineDocumentationsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        machineDocumentationRepository.saveAndFlush(machineDocumentation);

        // Get all the machineDocumentationList where description does not contain DEFAULT_DESCRIPTION
        defaultMachineDocumentationShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the machineDocumentationList where description does not contain UPDATED_DESCRIPTION
        defaultMachineDocumentationShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMachineDocumentationsByUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        machineDocumentationRepository.saveAndFlush(machineDocumentation);

        // Get all the machineDocumentationList where url equals to DEFAULT_URL
        defaultMachineDocumentationShouldBeFound("url.equals=" + DEFAULT_URL);

        // Get all the machineDocumentationList where url equals to UPDATED_URL
        defaultMachineDocumentationShouldNotBeFound("url.equals=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllMachineDocumentationsByUrlIsInShouldWork() throws Exception {
        // Initialize the database
        machineDocumentationRepository.saveAndFlush(machineDocumentation);

        // Get all the machineDocumentationList where url in DEFAULT_URL or UPDATED_URL
        defaultMachineDocumentationShouldBeFound("url.in=" + DEFAULT_URL + "," + UPDATED_URL);

        // Get all the machineDocumentationList where url equals to UPDATED_URL
        defaultMachineDocumentationShouldNotBeFound("url.in=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllMachineDocumentationsByUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        machineDocumentationRepository.saveAndFlush(machineDocumentation);

        // Get all the machineDocumentationList where url is not null
        defaultMachineDocumentationShouldBeFound("url.specified=true");

        // Get all the machineDocumentationList where url is null
        defaultMachineDocumentationShouldNotBeFound("url.specified=false");
    }

    @Test
    @Transactional
    void getAllMachineDocumentationsByUrlContainsSomething() throws Exception {
        // Initialize the database
        machineDocumentationRepository.saveAndFlush(machineDocumentation);

        // Get all the machineDocumentationList where url contains DEFAULT_URL
        defaultMachineDocumentationShouldBeFound("url.contains=" + DEFAULT_URL);

        // Get all the machineDocumentationList where url contains UPDATED_URL
        defaultMachineDocumentationShouldNotBeFound("url.contains=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllMachineDocumentationsByUrlNotContainsSomething() throws Exception {
        // Initialize the database
        machineDocumentationRepository.saveAndFlush(machineDocumentation);

        // Get all the machineDocumentationList where url does not contain DEFAULT_URL
        defaultMachineDocumentationShouldNotBeFound("url.doesNotContain=" + DEFAULT_URL);

        // Get all the machineDocumentationList where url does not contain UPDATED_URL
        defaultMachineDocumentationShouldBeFound("url.doesNotContain=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllMachineDocumentationsByMachineIsEqualToSomething() throws Exception {
        Machine machine;
        if (TestUtil.findAll(em, Machine.class).isEmpty()) {
            machineDocumentationRepository.saveAndFlush(machineDocumentation);
            machine = MachineResourceIT.createEntity(em);
        } else {
            machine = TestUtil.findAll(em, Machine.class).get(0);
        }
        em.persist(machine);
        em.flush();
        machineDocumentation.setMachine(machine);
        machineDocumentationRepository.saveAndFlush(machineDocumentation);
        Long machineId = machine.getId();
        // Get all the machineDocumentationList where machine equals to machineId
        defaultMachineDocumentationShouldBeFound("machineId.equals=" + machineId);

        // Get all the machineDocumentationList where machine equals to (machineId + 1)
        defaultMachineDocumentationShouldNotBeFound("machineId.equals=" + (machineId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMachineDocumentationShouldBeFound(String filter) throws Exception {
        restMachineDocumentationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(machineDocumentation.getId().intValue())))
            .andExpect(jsonPath("$.[*].reference").value(hasItem(DEFAULT_REFERENCE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)));

        // Check, that the count call also returns 1
        restMachineDocumentationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMachineDocumentationShouldNotBeFound(String filter) throws Exception {
        restMachineDocumentationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMachineDocumentationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMachineDocumentation() throws Exception {
        // Get the machineDocumentation
        restMachineDocumentationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMachineDocumentation() throws Exception {
        // Initialize the database
        machineDocumentationRepository.saveAndFlush(machineDocumentation);

        int databaseSizeBeforeUpdate = machineDocumentationRepository.findAll().size();

        // Update the machineDocumentation
        MachineDocumentation updatedMachineDocumentation = machineDocumentationRepository
            .findById(machineDocumentation.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedMachineDocumentation are not directly saved in db
        em.detach(updatedMachineDocumentation);
        updatedMachineDocumentation
            .reference(UPDATED_REFERENCE)
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .description(UPDATED_DESCRIPTION)
            .url(UPDATED_URL);
        MachineDocumentationDTO machineDocumentationDTO = machineDocumentationMapper.toDto(updatedMachineDocumentation);

        restMachineDocumentationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, machineDocumentationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(machineDocumentationDTO))
            )
            .andExpect(status().isOk());

        // Validate the MachineDocumentation in the database
        List<MachineDocumentation> machineDocumentationList = machineDocumentationRepository.findAll();
        assertThat(machineDocumentationList).hasSize(databaseSizeBeforeUpdate);
        MachineDocumentation testMachineDocumentation = machineDocumentationList.get(machineDocumentationList.size() - 1);
        assertThat(testMachineDocumentation.getReference()).isEqualTo(UPDATED_REFERENCE);
        assertThat(testMachineDocumentation.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMachineDocumentation.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testMachineDocumentation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMachineDocumentation.getUrl()).isEqualTo(UPDATED_URL);
    }

    @Test
    @Transactional
    void putNonExistingMachineDocumentation() throws Exception {
        int databaseSizeBeforeUpdate = machineDocumentationRepository.findAll().size();
        machineDocumentation.setId(longCount.incrementAndGet());

        // Create the MachineDocumentation
        MachineDocumentationDTO machineDocumentationDTO = machineDocumentationMapper.toDto(machineDocumentation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMachineDocumentationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, machineDocumentationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(machineDocumentationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MachineDocumentation in the database
        List<MachineDocumentation> machineDocumentationList = machineDocumentationRepository.findAll();
        assertThat(machineDocumentationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMachineDocumentation() throws Exception {
        int databaseSizeBeforeUpdate = machineDocumentationRepository.findAll().size();
        machineDocumentation.setId(longCount.incrementAndGet());

        // Create the MachineDocumentation
        MachineDocumentationDTO machineDocumentationDTO = machineDocumentationMapper.toDto(machineDocumentation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMachineDocumentationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(machineDocumentationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MachineDocumentation in the database
        List<MachineDocumentation> machineDocumentationList = machineDocumentationRepository.findAll();
        assertThat(machineDocumentationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMachineDocumentation() throws Exception {
        int databaseSizeBeforeUpdate = machineDocumentationRepository.findAll().size();
        machineDocumentation.setId(longCount.incrementAndGet());

        // Create the MachineDocumentation
        MachineDocumentationDTO machineDocumentationDTO = machineDocumentationMapper.toDto(machineDocumentation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMachineDocumentationMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(machineDocumentationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MachineDocumentation in the database
        List<MachineDocumentation> machineDocumentationList = machineDocumentationRepository.findAll();
        assertThat(machineDocumentationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMachineDocumentationWithPatch() throws Exception {
        // Initialize the database
        machineDocumentationRepository.saveAndFlush(machineDocumentation);

        int databaseSizeBeforeUpdate = machineDocumentationRepository.findAll().size();

        // Update the machineDocumentation using partial update
        MachineDocumentation partialUpdatedMachineDocumentation = new MachineDocumentation();
        partialUpdatedMachineDocumentation.setId(machineDocumentation.getId());

        partialUpdatedMachineDocumentation.reference(UPDATED_REFERENCE).type(UPDATED_TYPE).url(UPDATED_URL);

        restMachineDocumentationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMachineDocumentation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMachineDocumentation))
            )
            .andExpect(status().isOk());

        // Validate the MachineDocumentation in the database
        List<MachineDocumentation> machineDocumentationList = machineDocumentationRepository.findAll();
        assertThat(machineDocumentationList).hasSize(databaseSizeBeforeUpdate);
        MachineDocumentation testMachineDocumentation = machineDocumentationList.get(machineDocumentationList.size() - 1);
        assertThat(testMachineDocumentation.getReference()).isEqualTo(UPDATED_REFERENCE);
        assertThat(testMachineDocumentation.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMachineDocumentation.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testMachineDocumentation.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testMachineDocumentation.getUrl()).isEqualTo(UPDATED_URL);
    }

    @Test
    @Transactional
    void fullUpdateMachineDocumentationWithPatch() throws Exception {
        // Initialize the database
        machineDocumentationRepository.saveAndFlush(machineDocumentation);

        int databaseSizeBeforeUpdate = machineDocumentationRepository.findAll().size();

        // Update the machineDocumentation using partial update
        MachineDocumentation partialUpdatedMachineDocumentation = new MachineDocumentation();
        partialUpdatedMachineDocumentation.setId(machineDocumentation.getId());

        partialUpdatedMachineDocumentation
            .reference(UPDATED_REFERENCE)
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .description(UPDATED_DESCRIPTION)
            .url(UPDATED_URL);

        restMachineDocumentationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMachineDocumentation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMachineDocumentation))
            )
            .andExpect(status().isOk());

        // Validate the MachineDocumentation in the database
        List<MachineDocumentation> machineDocumentationList = machineDocumentationRepository.findAll();
        assertThat(machineDocumentationList).hasSize(databaseSizeBeforeUpdate);
        MachineDocumentation testMachineDocumentation = machineDocumentationList.get(machineDocumentationList.size() - 1);
        assertThat(testMachineDocumentation.getReference()).isEqualTo(UPDATED_REFERENCE);
        assertThat(testMachineDocumentation.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMachineDocumentation.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testMachineDocumentation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMachineDocumentation.getUrl()).isEqualTo(UPDATED_URL);
    }

    @Test
    @Transactional
    void patchNonExistingMachineDocumentation() throws Exception {
        int databaseSizeBeforeUpdate = machineDocumentationRepository.findAll().size();
        machineDocumentation.setId(longCount.incrementAndGet());

        // Create the MachineDocumentation
        MachineDocumentationDTO machineDocumentationDTO = machineDocumentationMapper.toDto(machineDocumentation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMachineDocumentationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, machineDocumentationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(machineDocumentationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MachineDocumentation in the database
        List<MachineDocumentation> machineDocumentationList = machineDocumentationRepository.findAll();
        assertThat(machineDocumentationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMachineDocumentation() throws Exception {
        int databaseSizeBeforeUpdate = machineDocumentationRepository.findAll().size();
        machineDocumentation.setId(longCount.incrementAndGet());

        // Create the MachineDocumentation
        MachineDocumentationDTO machineDocumentationDTO = machineDocumentationMapper.toDto(machineDocumentation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMachineDocumentationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(machineDocumentationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MachineDocumentation in the database
        List<MachineDocumentation> machineDocumentationList = machineDocumentationRepository.findAll();
        assertThat(machineDocumentationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMachineDocumentation() throws Exception {
        int databaseSizeBeforeUpdate = machineDocumentationRepository.findAll().size();
        machineDocumentation.setId(longCount.incrementAndGet());

        // Create the MachineDocumentation
        MachineDocumentationDTO machineDocumentationDTO = machineDocumentationMapper.toDto(machineDocumentation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMachineDocumentationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(machineDocumentationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MachineDocumentation in the database
        List<MachineDocumentation> machineDocumentationList = machineDocumentationRepository.findAll();
        assertThat(machineDocumentationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMachineDocumentation() throws Exception {
        // Initialize the database
        machineDocumentationRepository.saveAndFlush(machineDocumentation);

        int databaseSizeBeforeDelete = machineDocumentationRepository.findAll().size();

        // Delete the machineDocumentation
        restMachineDocumentationMockMvc
            .perform(delete(ENTITY_API_URL_ID, machineDocumentation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MachineDocumentation> machineDocumentationList = machineDocumentationRepository.findAll();
        assertThat(machineDocumentationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
