package com.sothis.tech.web.rest;

import static com.sothis.tech.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sothis.tech.IntegrationTest;
import com.sothis.tech.domain.Machine;
import com.sothis.tech.domain.MachineModel;
import com.sothis.tech.domain.Organization;
import com.sothis.tech.domain.PlantArea;
import com.sothis.tech.repository.MachineRepository;
import com.sothis.tech.service.MachineService;
import com.sothis.tech.service.dto.MachineDTO;
import com.sothis.tech.service.mapper.MachineMapper;
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
 * Integration tests for the {@link MachineResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MachineResourceIT {

    private static final String DEFAULT_REFERENCE = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_FIRMWARE_VERSION = "AAAAAAAAAA";
    private static final String UPDATED_FIRMWARE_VERSION = "BBBBBBBBBB";

    private static final String DEFAULT_HARDWARE_VERSION = "AAAAAAAAAA";
    private static final String UPDATED_HARDWARE_VERSION = "BBBBBBBBBB";

    private static final String DEFAULT_SOFTWARE_VERSION = "AAAAAAAAAA";
    private static final String UPDATED_SOFTWARE_VERSION = "BBBBBBBBBB";

    private static final String DEFAULT_SERIAL_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_SERIAL_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_SUPPORTED_PROTOCOL = "AAAAAAAAAA";
    private static final String UPDATED_SUPPORTED_PROTOCOL = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/machines";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MachineRepository machineRepository;

    @Mock
    private MachineRepository machineRepositoryMock;

    @Autowired
    private MachineMapper machineMapper;

    @Mock
    private MachineService machineServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMachineMockMvc;

    private Machine machine;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Machine createEntity(EntityManager em) {
        Machine machine = new Machine()
            .reference(DEFAULT_REFERENCE)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .firmwareVersion(DEFAULT_FIRMWARE_VERSION)
            .hardwareVersion(DEFAULT_HARDWARE_VERSION)
            .softwareVersion(DEFAULT_SOFTWARE_VERSION)
            .serialNumber(DEFAULT_SERIAL_NUMBER)
            .supportedProtocol(DEFAULT_SUPPORTED_PROTOCOL)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        // Add required entity
        PlantArea plantArea;
        if (TestUtil.findAll(em, PlantArea.class).isEmpty()) {
            plantArea = PlantAreaResourceIT.createEntity(em);
            em.persist(plantArea);
            em.flush();
        } else {
            plantArea = TestUtil.findAll(em, PlantArea.class).get(0);
        }
        machine.setPlantArea(plantArea);
        // Add required entity
        MachineModel machineModel;
        if (TestUtil.findAll(em, MachineModel.class).isEmpty()) {
            machineModel = MachineModelResourceIT.createEntity(em);
            em.persist(machineModel);
            em.flush();
        } else {
            machineModel = TestUtil.findAll(em, MachineModel.class).get(0);
        }
        machine.setMachineModel(machineModel);
        return machine;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Machine createUpdatedEntity(EntityManager em) {
        Machine machine = new Machine()
            .reference(UPDATED_REFERENCE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .firmwareVersion(UPDATED_FIRMWARE_VERSION)
            .hardwareVersion(UPDATED_HARDWARE_VERSION)
            .softwareVersion(UPDATED_SOFTWARE_VERSION)
            .serialNumber(UPDATED_SERIAL_NUMBER)
            .supportedProtocol(UPDATED_SUPPORTED_PROTOCOL)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        // Add required entity
        PlantArea plantArea;
        if (TestUtil.findAll(em, PlantArea.class).isEmpty()) {
            plantArea = PlantAreaResourceIT.createUpdatedEntity(em);
            em.persist(plantArea);
            em.flush();
        } else {
            plantArea = TestUtil.findAll(em, PlantArea.class).get(0);
        }
        machine.setPlantArea(plantArea);
        // Add required entity
        MachineModel machineModel;
        if (TestUtil.findAll(em, MachineModel.class).isEmpty()) {
            machineModel = MachineModelResourceIT.createUpdatedEntity(em);
            em.persist(machineModel);
            em.flush();
        } else {
            machineModel = TestUtil.findAll(em, MachineModel.class).get(0);
        }
        machine.setMachineModel(machineModel);
        return machine;
    }

    @BeforeEach
    public void initTest() {
        machine = createEntity(em);
    }

    @Test
    @Transactional
    void createMachine() throws Exception {
        int databaseSizeBeforeCreate = machineRepository.findAll().size();
        // Create the Machine
        MachineDTO machineDTO = machineMapper.toDto(machine);
        restMachineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(machineDTO)))
            .andExpect(status().isCreated());

        // Validate the Machine in the database
        List<Machine> machineList = machineRepository.findAll();
        assertThat(machineList).hasSize(databaseSizeBeforeCreate + 1);
        Machine testMachine = machineList.get(machineList.size() - 1);
        assertThat(testMachine.getReference()).isEqualTo(DEFAULT_REFERENCE);
        assertThat(testMachine.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMachine.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testMachine.getFirmwareVersion()).isEqualTo(DEFAULT_FIRMWARE_VERSION);
        assertThat(testMachine.getHardwareVersion()).isEqualTo(DEFAULT_HARDWARE_VERSION);
        assertThat(testMachine.getSoftwareVersion()).isEqualTo(DEFAULT_SOFTWARE_VERSION);
        assertThat(testMachine.getSerialNumber()).isEqualTo(DEFAULT_SERIAL_NUMBER);
        assertThat(testMachine.getSupportedProtocol()).isEqualTo(DEFAULT_SUPPORTED_PROTOCOL);
        assertThat(testMachine.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testMachine.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createMachineWithExistingId() throws Exception {
        // Create the Machine with an existing ID
        machine.setId(1L);
        MachineDTO machineDTO = machineMapper.toDto(machine);

        int databaseSizeBeforeCreate = machineRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMachineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(machineDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Machine in the database
        List<Machine> machineList = machineRepository.findAll();
        assertThat(machineList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkReferenceIsRequired() throws Exception {
        int databaseSizeBeforeTest = machineRepository.findAll().size();
        // set the field null
        machine.setReference(null);

        // Create the Machine, which fails.
        MachineDTO machineDTO = machineMapper.toDto(machine);

        restMachineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(machineDTO)))
            .andExpect(status().isBadRequest());

        List<Machine> machineList = machineRepository.findAll();
        assertThat(machineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = machineRepository.findAll().size();
        // set the field null
        machine.setName(null);

        // Create the Machine, which fails.
        MachineDTO machineDTO = machineMapper.toDto(machine);

        restMachineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(machineDTO)))
            .andExpect(status().isBadRequest());

        List<Machine> machineList = machineRepository.findAll();
        assertThat(machineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMachines() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get all the machineList
        restMachineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(machine.getId().intValue())))
            .andExpect(jsonPath("$.[*].reference").value(hasItem(DEFAULT_REFERENCE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].firmwareVersion").value(hasItem(DEFAULT_FIRMWARE_VERSION)))
            .andExpect(jsonPath("$.[*].hardwareVersion").value(hasItem(DEFAULT_HARDWARE_VERSION)))
            .andExpect(jsonPath("$.[*].softwareVersion").value(hasItem(DEFAULT_SOFTWARE_VERSION)))
            .andExpect(jsonPath("$.[*].serialNumber").value(hasItem(DEFAULT_SERIAL_NUMBER)))
            .andExpect(jsonPath("$.[*].supportedProtocol").value(hasItem(DEFAULT_SUPPORTED_PROTOCOL)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMachinesWithEagerRelationshipsIsEnabled() throws Exception {
        when(machineServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMachineMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(machineServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMachinesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(machineServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMachineMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(machineRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getMachine() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get the machine
        restMachineMockMvc
            .perform(get(ENTITY_API_URL_ID, machine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(machine.getId().intValue()))
            .andExpect(jsonPath("$.reference").value(DEFAULT_REFERENCE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.firmwareVersion").value(DEFAULT_FIRMWARE_VERSION))
            .andExpect(jsonPath("$.hardwareVersion").value(DEFAULT_HARDWARE_VERSION))
            .andExpect(jsonPath("$.softwareVersion").value(DEFAULT_SOFTWARE_VERSION))
            .andExpect(jsonPath("$.serialNumber").value(DEFAULT_SERIAL_NUMBER))
            .andExpect(jsonPath("$.supportedProtocol").value(DEFAULT_SUPPORTED_PROTOCOL))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.updatedAt").value(sameInstant(DEFAULT_UPDATED_AT)));
    }

    @Test
    @Transactional
    void getMachinesByIdFiltering() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        Long id = machine.getId();

        defaultMachineShouldBeFound("id.equals=" + id);
        defaultMachineShouldNotBeFound("id.notEquals=" + id);

        defaultMachineShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMachineShouldNotBeFound("id.greaterThan=" + id);

        defaultMachineShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMachineShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMachinesByReferenceIsEqualToSomething() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get all the machineList where reference equals to DEFAULT_REFERENCE
        defaultMachineShouldBeFound("reference.equals=" + DEFAULT_REFERENCE);

        // Get all the machineList where reference equals to UPDATED_REFERENCE
        defaultMachineShouldNotBeFound("reference.equals=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    void getAllMachinesByReferenceIsInShouldWork() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get all the machineList where reference in DEFAULT_REFERENCE or UPDATED_REFERENCE
        defaultMachineShouldBeFound("reference.in=" + DEFAULT_REFERENCE + "," + UPDATED_REFERENCE);

        // Get all the machineList where reference equals to UPDATED_REFERENCE
        defaultMachineShouldNotBeFound("reference.in=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    void getAllMachinesByReferenceIsNullOrNotNull() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get all the machineList where reference is not null
        defaultMachineShouldBeFound("reference.specified=true");

        // Get all the machineList where reference is null
        defaultMachineShouldNotBeFound("reference.specified=false");
    }

    @Test
    @Transactional
    void getAllMachinesByReferenceContainsSomething() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get all the machineList where reference contains DEFAULT_REFERENCE
        defaultMachineShouldBeFound("reference.contains=" + DEFAULT_REFERENCE);

        // Get all the machineList where reference contains UPDATED_REFERENCE
        defaultMachineShouldNotBeFound("reference.contains=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    void getAllMachinesByReferenceNotContainsSomething() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get all the machineList where reference does not contain DEFAULT_REFERENCE
        defaultMachineShouldNotBeFound("reference.doesNotContain=" + DEFAULT_REFERENCE);

        // Get all the machineList where reference does not contain UPDATED_REFERENCE
        defaultMachineShouldBeFound("reference.doesNotContain=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    void getAllMachinesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get all the machineList where name equals to DEFAULT_NAME
        defaultMachineShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the machineList where name equals to UPDATED_NAME
        defaultMachineShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMachinesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get all the machineList where name in DEFAULT_NAME or UPDATED_NAME
        defaultMachineShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the machineList where name equals to UPDATED_NAME
        defaultMachineShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMachinesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get all the machineList where name is not null
        defaultMachineShouldBeFound("name.specified=true");

        // Get all the machineList where name is null
        defaultMachineShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllMachinesByNameContainsSomething() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get all the machineList where name contains DEFAULT_NAME
        defaultMachineShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the machineList where name contains UPDATED_NAME
        defaultMachineShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMachinesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get all the machineList where name does not contain DEFAULT_NAME
        defaultMachineShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the machineList where name does not contain UPDATED_NAME
        defaultMachineShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMachinesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get all the machineList where description equals to DEFAULT_DESCRIPTION
        defaultMachineShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the machineList where description equals to UPDATED_DESCRIPTION
        defaultMachineShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMachinesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get all the machineList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultMachineShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the machineList where description equals to UPDATED_DESCRIPTION
        defaultMachineShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMachinesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get all the machineList where description is not null
        defaultMachineShouldBeFound("description.specified=true");

        // Get all the machineList where description is null
        defaultMachineShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllMachinesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get all the machineList where description contains DEFAULT_DESCRIPTION
        defaultMachineShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the machineList where description contains UPDATED_DESCRIPTION
        defaultMachineShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMachinesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get all the machineList where description does not contain DEFAULT_DESCRIPTION
        defaultMachineShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the machineList where description does not contain UPDATED_DESCRIPTION
        defaultMachineShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMachinesByFirmwareVersionIsEqualToSomething() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get all the machineList where firmwareVersion equals to DEFAULT_FIRMWARE_VERSION
        defaultMachineShouldBeFound("firmwareVersion.equals=" + DEFAULT_FIRMWARE_VERSION);

        // Get all the machineList where firmwareVersion equals to UPDATED_FIRMWARE_VERSION
        defaultMachineShouldNotBeFound("firmwareVersion.equals=" + UPDATED_FIRMWARE_VERSION);
    }

    @Test
    @Transactional
    void getAllMachinesByFirmwareVersionIsInShouldWork() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get all the machineList where firmwareVersion in DEFAULT_FIRMWARE_VERSION or UPDATED_FIRMWARE_VERSION
        defaultMachineShouldBeFound("firmwareVersion.in=" + DEFAULT_FIRMWARE_VERSION + "," + UPDATED_FIRMWARE_VERSION);

        // Get all the machineList where firmwareVersion equals to UPDATED_FIRMWARE_VERSION
        defaultMachineShouldNotBeFound("firmwareVersion.in=" + UPDATED_FIRMWARE_VERSION);
    }

    @Test
    @Transactional
    void getAllMachinesByFirmwareVersionIsNullOrNotNull() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get all the machineList where firmwareVersion is not null
        defaultMachineShouldBeFound("firmwareVersion.specified=true");

        // Get all the machineList where firmwareVersion is null
        defaultMachineShouldNotBeFound("firmwareVersion.specified=false");
    }

    @Test
    @Transactional
    void getAllMachinesByFirmwareVersionContainsSomething() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get all the machineList where firmwareVersion contains DEFAULT_FIRMWARE_VERSION
        defaultMachineShouldBeFound("firmwareVersion.contains=" + DEFAULT_FIRMWARE_VERSION);

        // Get all the machineList where firmwareVersion contains UPDATED_FIRMWARE_VERSION
        defaultMachineShouldNotBeFound("firmwareVersion.contains=" + UPDATED_FIRMWARE_VERSION);
    }

    @Test
    @Transactional
    void getAllMachinesByFirmwareVersionNotContainsSomething() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get all the machineList where firmwareVersion does not contain DEFAULT_FIRMWARE_VERSION
        defaultMachineShouldNotBeFound("firmwareVersion.doesNotContain=" + DEFAULT_FIRMWARE_VERSION);

        // Get all the machineList where firmwareVersion does not contain UPDATED_FIRMWARE_VERSION
        defaultMachineShouldBeFound("firmwareVersion.doesNotContain=" + UPDATED_FIRMWARE_VERSION);
    }

    @Test
    @Transactional
    void getAllMachinesByHardwareVersionIsEqualToSomething() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get all the machineList where hardwareVersion equals to DEFAULT_HARDWARE_VERSION
        defaultMachineShouldBeFound("hardwareVersion.equals=" + DEFAULT_HARDWARE_VERSION);

        // Get all the machineList where hardwareVersion equals to UPDATED_HARDWARE_VERSION
        defaultMachineShouldNotBeFound("hardwareVersion.equals=" + UPDATED_HARDWARE_VERSION);
    }

    @Test
    @Transactional
    void getAllMachinesByHardwareVersionIsInShouldWork() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get all the machineList where hardwareVersion in DEFAULT_HARDWARE_VERSION or UPDATED_HARDWARE_VERSION
        defaultMachineShouldBeFound("hardwareVersion.in=" + DEFAULT_HARDWARE_VERSION + "," + UPDATED_HARDWARE_VERSION);

        // Get all the machineList where hardwareVersion equals to UPDATED_HARDWARE_VERSION
        defaultMachineShouldNotBeFound("hardwareVersion.in=" + UPDATED_HARDWARE_VERSION);
    }

    @Test
    @Transactional
    void getAllMachinesByHardwareVersionIsNullOrNotNull() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get all the machineList where hardwareVersion is not null
        defaultMachineShouldBeFound("hardwareVersion.specified=true");

        // Get all the machineList where hardwareVersion is null
        defaultMachineShouldNotBeFound("hardwareVersion.specified=false");
    }

    @Test
    @Transactional
    void getAllMachinesByHardwareVersionContainsSomething() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get all the machineList where hardwareVersion contains DEFAULT_HARDWARE_VERSION
        defaultMachineShouldBeFound("hardwareVersion.contains=" + DEFAULT_HARDWARE_VERSION);

        // Get all the machineList where hardwareVersion contains UPDATED_HARDWARE_VERSION
        defaultMachineShouldNotBeFound("hardwareVersion.contains=" + UPDATED_HARDWARE_VERSION);
    }

    @Test
    @Transactional
    void getAllMachinesByHardwareVersionNotContainsSomething() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get all the machineList where hardwareVersion does not contain DEFAULT_HARDWARE_VERSION
        defaultMachineShouldNotBeFound("hardwareVersion.doesNotContain=" + DEFAULT_HARDWARE_VERSION);

        // Get all the machineList where hardwareVersion does not contain UPDATED_HARDWARE_VERSION
        defaultMachineShouldBeFound("hardwareVersion.doesNotContain=" + UPDATED_HARDWARE_VERSION);
    }

    @Test
    @Transactional
    void getAllMachinesBySoftwareVersionIsEqualToSomething() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get all the machineList where softwareVersion equals to DEFAULT_SOFTWARE_VERSION
        defaultMachineShouldBeFound("softwareVersion.equals=" + DEFAULT_SOFTWARE_VERSION);

        // Get all the machineList where softwareVersion equals to UPDATED_SOFTWARE_VERSION
        defaultMachineShouldNotBeFound("softwareVersion.equals=" + UPDATED_SOFTWARE_VERSION);
    }

    @Test
    @Transactional
    void getAllMachinesBySoftwareVersionIsInShouldWork() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get all the machineList where softwareVersion in DEFAULT_SOFTWARE_VERSION or UPDATED_SOFTWARE_VERSION
        defaultMachineShouldBeFound("softwareVersion.in=" + DEFAULT_SOFTWARE_VERSION + "," + UPDATED_SOFTWARE_VERSION);

        // Get all the machineList where softwareVersion equals to UPDATED_SOFTWARE_VERSION
        defaultMachineShouldNotBeFound("softwareVersion.in=" + UPDATED_SOFTWARE_VERSION);
    }

    @Test
    @Transactional
    void getAllMachinesBySoftwareVersionIsNullOrNotNull() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get all the machineList where softwareVersion is not null
        defaultMachineShouldBeFound("softwareVersion.specified=true");

        // Get all the machineList where softwareVersion is null
        defaultMachineShouldNotBeFound("softwareVersion.specified=false");
    }

    @Test
    @Transactional
    void getAllMachinesBySoftwareVersionContainsSomething() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get all the machineList where softwareVersion contains DEFAULT_SOFTWARE_VERSION
        defaultMachineShouldBeFound("softwareVersion.contains=" + DEFAULT_SOFTWARE_VERSION);

        // Get all the machineList where softwareVersion contains UPDATED_SOFTWARE_VERSION
        defaultMachineShouldNotBeFound("softwareVersion.contains=" + UPDATED_SOFTWARE_VERSION);
    }

    @Test
    @Transactional
    void getAllMachinesBySoftwareVersionNotContainsSomething() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get all the machineList where softwareVersion does not contain DEFAULT_SOFTWARE_VERSION
        defaultMachineShouldNotBeFound("softwareVersion.doesNotContain=" + DEFAULT_SOFTWARE_VERSION);

        // Get all the machineList where softwareVersion does not contain UPDATED_SOFTWARE_VERSION
        defaultMachineShouldBeFound("softwareVersion.doesNotContain=" + UPDATED_SOFTWARE_VERSION);
    }

    @Test
    @Transactional
    void getAllMachinesBySerialNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get all the machineList where serialNumber equals to DEFAULT_SERIAL_NUMBER
        defaultMachineShouldBeFound("serialNumber.equals=" + DEFAULT_SERIAL_NUMBER);

        // Get all the machineList where serialNumber equals to UPDATED_SERIAL_NUMBER
        defaultMachineShouldNotBeFound("serialNumber.equals=" + UPDATED_SERIAL_NUMBER);
    }

    @Test
    @Transactional
    void getAllMachinesBySerialNumberIsInShouldWork() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get all the machineList where serialNumber in DEFAULT_SERIAL_NUMBER or UPDATED_SERIAL_NUMBER
        defaultMachineShouldBeFound("serialNumber.in=" + DEFAULT_SERIAL_NUMBER + "," + UPDATED_SERIAL_NUMBER);

        // Get all the machineList where serialNumber equals to UPDATED_SERIAL_NUMBER
        defaultMachineShouldNotBeFound("serialNumber.in=" + UPDATED_SERIAL_NUMBER);
    }

    @Test
    @Transactional
    void getAllMachinesBySerialNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get all the machineList where serialNumber is not null
        defaultMachineShouldBeFound("serialNumber.specified=true");

        // Get all the machineList where serialNumber is null
        defaultMachineShouldNotBeFound("serialNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllMachinesBySerialNumberContainsSomething() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get all the machineList where serialNumber contains DEFAULT_SERIAL_NUMBER
        defaultMachineShouldBeFound("serialNumber.contains=" + DEFAULT_SERIAL_NUMBER);

        // Get all the machineList where serialNumber contains UPDATED_SERIAL_NUMBER
        defaultMachineShouldNotBeFound("serialNumber.contains=" + UPDATED_SERIAL_NUMBER);
    }

    @Test
    @Transactional
    void getAllMachinesBySerialNumberNotContainsSomething() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get all the machineList where serialNumber does not contain DEFAULT_SERIAL_NUMBER
        defaultMachineShouldNotBeFound("serialNumber.doesNotContain=" + DEFAULT_SERIAL_NUMBER);

        // Get all the machineList where serialNumber does not contain UPDATED_SERIAL_NUMBER
        defaultMachineShouldBeFound("serialNumber.doesNotContain=" + UPDATED_SERIAL_NUMBER);
    }

    @Test
    @Transactional
    void getAllMachinesBySupportedProtocolIsEqualToSomething() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get all the machineList where supportedProtocol equals to DEFAULT_SUPPORTED_PROTOCOL
        defaultMachineShouldBeFound("supportedProtocol.equals=" + DEFAULT_SUPPORTED_PROTOCOL);

        // Get all the machineList where supportedProtocol equals to UPDATED_SUPPORTED_PROTOCOL
        defaultMachineShouldNotBeFound("supportedProtocol.equals=" + UPDATED_SUPPORTED_PROTOCOL);
    }

    @Test
    @Transactional
    void getAllMachinesBySupportedProtocolIsInShouldWork() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get all the machineList where supportedProtocol in DEFAULT_SUPPORTED_PROTOCOL or UPDATED_SUPPORTED_PROTOCOL
        defaultMachineShouldBeFound("supportedProtocol.in=" + DEFAULT_SUPPORTED_PROTOCOL + "," + UPDATED_SUPPORTED_PROTOCOL);

        // Get all the machineList where supportedProtocol equals to UPDATED_SUPPORTED_PROTOCOL
        defaultMachineShouldNotBeFound("supportedProtocol.in=" + UPDATED_SUPPORTED_PROTOCOL);
    }

    @Test
    @Transactional
    void getAllMachinesBySupportedProtocolIsNullOrNotNull() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get all the machineList where supportedProtocol is not null
        defaultMachineShouldBeFound("supportedProtocol.specified=true");

        // Get all the machineList where supportedProtocol is null
        defaultMachineShouldNotBeFound("supportedProtocol.specified=false");
    }

    @Test
    @Transactional
    void getAllMachinesBySupportedProtocolContainsSomething() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get all the machineList where supportedProtocol contains DEFAULT_SUPPORTED_PROTOCOL
        defaultMachineShouldBeFound("supportedProtocol.contains=" + DEFAULT_SUPPORTED_PROTOCOL);

        // Get all the machineList where supportedProtocol contains UPDATED_SUPPORTED_PROTOCOL
        defaultMachineShouldNotBeFound("supportedProtocol.contains=" + UPDATED_SUPPORTED_PROTOCOL);
    }

    @Test
    @Transactional
    void getAllMachinesBySupportedProtocolNotContainsSomething() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get all the machineList where supportedProtocol does not contain DEFAULT_SUPPORTED_PROTOCOL
        defaultMachineShouldNotBeFound("supportedProtocol.doesNotContain=" + DEFAULT_SUPPORTED_PROTOCOL);

        // Get all the machineList where supportedProtocol does not contain UPDATED_SUPPORTED_PROTOCOL
        defaultMachineShouldBeFound("supportedProtocol.doesNotContain=" + UPDATED_SUPPORTED_PROTOCOL);
    }

    @Test
    @Transactional
    void getAllMachinesByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get all the machineList where createdAt equals to DEFAULT_CREATED_AT
        defaultMachineShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the machineList where createdAt equals to UPDATED_CREATED_AT
        defaultMachineShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllMachinesByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get all the machineList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultMachineShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the machineList where createdAt equals to UPDATED_CREATED_AT
        defaultMachineShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllMachinesByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get all the machineList where createdAt is not null
        defaultMachineShouldBeFound("createdAt.specified=true");

        // Get all the machineList where createdAt is null
        defaultMachineShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllMachinesByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get all the machineList where createdAt is greater than or equal to DEFAULT_CREATED_AT
        defaultMachineShouldBeFound("createdAt.greaterThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the machineList where createdAt is greater than or equal to UPDATED_CREATED_AT
        defaultMachineShouldNotBeFound("createdAt.greaterThanOrEqual=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllMachinesByCreatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get all the machineList where createdAt is less than or equal to DEFAULT_CREATED_AT
        defaultMachineShouldBeFound("createdAt.lessThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the machineList where createdAt is less than or equal to SMALLER_CREATED_AT
        defaultMachineShouldNotBeFound("createdAt.lessThanOrEqual=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllMachinesByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get all the machineList where createdAt is less than DEFAULT_CREATED_AT
        defaultMachineShouldNotBeFound("createdAt.lessThan=" + DEFAULT_CREATED_AT);

        // Get all the machineList where createdAt is less than UPDATED_CREATED_AT
        defaultMachineShouldBeFound("createdAt.lessThan=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllMachinesByCreatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get all the machineList where createdAt is greater than DEFAULT_CREATED_AT
        defaultMachineShouldNotBeFound("createdAt.greaterThan=" + DEFAULT_CREATED_AT);

        // Get all the machineList where createdAt is greater than SMALLER_CREATED_AT
        defaultMachineShouldBeFound("createdAt.greaterThan=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllMachinesByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get all the machineList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultMachineShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the machineList where updatedAt equals to UPDATED_UPDATED_AT
        defaultMachineShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllMachinesByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get all the machineList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultMachineShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the machineList where updatedAt equals to UPDATED_UPDATED_AT
        defaultMachineShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllMachinesByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get all the machineList where updatedAt is not null
        defaultMachineShouldBeFound("updatedAt.specified=true");

        // Get all the machineList where updatedAt is null
        defaultMachineShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllMachinesByUpdatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get all the machineList where updatedAt is greater than or equal to DEFAULT_UPDATED_AT
        defaultMachineShouldBeFound("updatedAt.greaterThanOrEqual=" + DEFAULT_UPDATED_AT);

        // Get all the machineList where updatedAt is greater than or equal to UPDATED_UPDATED_AT
        defaultMachineShouldNotBeFound("updatedAt.greaterThanOrEqual=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllMachinesByUpdatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get all the machineList where updatedAt is less than or equal to DEFAULT_UPDATED_AT
        defaultMachineShouldBeFound("updatedAt.lessThanOrEqual=" + DEFAULT_UPDATED_AT);

        // Get all the machineList where updatedAt is less than or equal to SMALLER_UPDATED_AT
        defaultMachineShouldNotBeFound("updatedAt.lessThanOrEqual=" + SMALLER_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllMachinesByUpdatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get all the machineList where updatedAt is less than DEFAULT_UPDATED_AT
        defaultMachineShouldNotBeFound("updatedAt.lessThan=" + DEFAULT_UPDATED_AT);

        // Get all the machineList where updatedAt is less than UPDATED_UPDATED_AT
        defaultMachineShouldBeFound("updatedAt.lessThan=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllMachinesByUpdatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get all the machineList where updatedAt is greater than DEFAULT_UPDATED_AT
        defaultMachineShouldNotBeFound("updatedAt.greaterThan=" + DEFAULT_UPDATED_AT);

        // Get all the machineList where updatedAt is greater than SMALLER_UPDATED_AT
        defaultMachineShouldBeFound("updatedAt.greaterThan=" + SMALLER_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllMachinesByPlantAreaIsEqualToSomething() throws Exception {
        PlantArea plantArea;
        if (TestUtil.findAll(em, PlantArea.class).isEmpty()) {
            machineRepository.saveAndFlush(machine);
            plantArea = PlantAreaResourceIT.createEntity(em);
        } else {
            plantArea = TestUtil.findAll(em, PlantArea.class).get(0);
        }
        em.persist(plantArea);
        em.flush();
        machine.setPlantArea(plantArea);
        machineRepository.saveAndFlush(machine);
        Long plantAreaId = plantArea.getId();
        // Get all the machineList where plantArea equals to plantAreaId
        defaultMachineShouldBeFound("plantAreaId.equals=" + plantAreaId);

        // Get all the machineList where plantArea equals to (plantAreaId + 1)
        defaultMachineShouldNotBeFound("plantAreaId.equals=" + (plantAreaId + 1));
    }

    @Test
    @Transactional
    void getAllMachinesByMachineModelIsEqualToSomething() throws Exception {
        MachineModel machineModel;
        if (TestUtil.findAll(em, MachineModel.class).isEmpty()) {
            machineRepository.saveAndFlush(machine);
            machineModel = MachineModelResourceIT.createEntity(em);
        } else {
            machineModel = TestUtil.findAll(em, MachineModel.class).get(0);
        }
        em.persist(machineModel);
        em.flush();
        machine.setMachineModel(machineModel);
        machineRepository.saveAndFlush(machine);
        Long machineModelId = machineModel.getId();
        // Get all the machineList where machineModel equals to machineModelId
        defaultMachineShouldBeFound("machineModelId.equals=" + machineModelId);

        // Get all the machineList where machineModel equals to (machineModelId + 1)
        defaultMachineShouldNotBeFound("machineModelId.equals=" + (machineModelId + 1));
    }

    @Test
    @Transactional
    void getAllMachinesByOrganizationIsEqualToSomething() throws Exception {
        Organization organization;
        if (TestUtil.findAll(em, Organization.class).isEmpty()) {
            machineRepository.saveAndFlush(machine);
            organization = OrganizationResourceIT.createEntity(em);
        } else {
            organization = TestUtil.findAll(em, Organization.class).get(0);
        }
        em.persist(organization);
        em.flush();
        machine.setOrganization(organization);
        machineRepository.saveAndFlush(machine);
        Long organizationId = organization.getId();
        // Get all the machineList where organization equals to organizationId
        defaultMachineShouldBeFound("organizationId.equals=" + organizationId);

        // Get all the machineList where organization equals to (organizationId + 1)
        defaultMachineShouldNotBeFound("organizationId.equals=" + (organizationId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMachineShouldBeFound(String filter) throws Exception {
        restMachineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(machine.getId().intValue())))
            .andExpect(jsonPath("$.[*].reference").value(hasItem(DEFAULT_REFERENCE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].firmwareVersion").value(hasItem(DEFAULT_FIRMWARE_VERSION)))
            .andExpect(jsonPath("$.[*].hardwareVersion").value(hasItem(DEFAULT_HARDWARE_VERSION)))
            .andExpect(jsonPath("$.[*].softwareVersion").value(hasItem(DEFAULT_SOFTWARE_VERSION)))
            .andExpect(jsonPath("$.[*].serialNumber").value(hasItem(DEFAULT_SERIAL_NUMBER)))
            .andExpect(jsonPath("$.[*].supportedProtocol").value(hasItem(DEFAULT_SUPPORTED_PROTOCOL)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));

        // Check, that the count call also returns 1
        restMachineMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMachineShouldNotBeFound(String filter) throws Exception {
        restMachineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMachineMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMachine() throws Exception {
        // Get the machine
        restMachineMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMachine() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        int databaseSizeBeforeUpdate = machineRepository.findAll().size();

        // Update the machine
        Machine updatedMachine = machineRepository.findById(machine.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMachine are not directly saved in db
        em.detach(updatedMachine);
        updatedMachine
            .reference(UPDATED_REFERENCE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .firmwareVersion(UPDATED_FIRMWARE_VERSION)
            .hardwareVersion(UPDATED_HARDWARE_VERSION)
            .softwareVersion(UPDATED_SOFTWARE_VERSION)
            .serialNumber(UPDATED_SERIAL_NUMBER)
            .supportedProtocol(UPDATED_SUPPORTED_PROTOCOL)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        MachineDTO machineDTO = machineMapper.toDto(updatedMachine);

        restMachineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, machineDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(machineDTO))
            )
            .andExpect(status().isOk());

        // Validate the Machine in the database
        List<Machine> machineList = machineRepository.findAll();
        assertThat(machineList).hasSize(databaseSizeBeforeUpdate);
        Machine testMachine = machineList.get(machineList.size() - 1);
        assertThat(testMachine.getReference()).isEqualTo(UPDATED_REFERENCE);
        assertThat(testMachine.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMachine.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMachine.getFirmwareVersion()).isEqualTo(UPDATED_FIRMWARE_VERSION);
        assertThat(testMachine.getHardwareVersion()).isEqualTo(UPDATED_HARDWARE_VERSION);
        assertThat(testMachine.getSoftwareVersion()).isEqualTo(UPDATED_SOFTWARE_VERSION);
        assertThat(testMachine.getSerialNumber()).isEqualTo(UPDATED_SERIAL_NUMBER);
        assertThat(testMachine.getSupportedProtocol()).isEqualTo(UPDATED_SUPPORTED_PROTOCOL);
        assertThat(testMachine.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testMachine.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingMachine() throws Exception {
        int databaseSizeBeforeUpdate = machineRepository.findAll().size();
        machine.setId(longCount.incrementAndGet());

        // Create the Machine
        MachineDTO machineDTO = machineMapper.toDto(machine);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMachineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, machineDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(machineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Machine in the database
        List<Machine> machineList = machineRepository.findAll();
        assertThat(machineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMachine() throws Exception {
        int databaseSizeBeforeUpdate = machineRepository.findAll().size();
        machine.setId(longCount.incrementAndGet());

        // Create the Machine
        MachineDTO machineDTO = machineMapper.toDto(machine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMachineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(machineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Machine in the database
        List<Machine> machineList = machineRepository.findAll();
        assertThat(machineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMachine() throws Exception {
        int databaseSizeBeforeUpdate = machineRepository.findAll().size();
        machine.setId(longCount.incrementAndGet());

        // Create the Machine
        MachineDTO machineDTO = machineMapper.toDto(machine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMachineMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(machineDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Machine in the database
        List<Machine> machineList = machineRepository.findAll();
        assertThat(machineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMachineWithPatch() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        int databaseSizeBeforeUpdate = machineRepository.findAll().size();

        // Update the machine using partial update
        Machine partialUpdatedMachine = new Machine();
        partialUpdatedMachine.setId(machine.getId());

        partialUpdatedMachine
            .reference(UPDATED_REFERENCE)
            .description(UPDATED_DESCRIPTION)
            .firmwareVersion(UPDATED_FIRMWARE_VERSION)
            .hardwareVersion(UPDATED_HARDWARE_VERSION)
            .serialNumber(UPDATED_SERIAL_NUMBER);

        restMachineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMachine.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMachine))
            )
            .andExpect(status().isOk());

        // Validate the Machine in the database
        List<Machine> machineList = machineRepository.findAll();
        assertThat(machineList).hasSize(databaseSizeBeforeUpdate);
        Machine testMachine = machineList.get(machineList.size() - 1);
        assertThat(testMachine.getReference()).isEqualTo(UPDATED_REFERENCE);
        assertThat(testMachine.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMachine.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMachine.getFirmwareVersion()).isEqualTo(UPDATED_FIRMWARE_VERSION);
        assertThat(testMachine.getHardwareVersion()).isEqualTo(UPDATED_HARDWARE_VERSION);
        assertThat(testMachine.getSoftwareVersion()).isEqualTo(DEFAULT_SOFTWARE_VERSION);
        assertThat(testMachine.getSerialNumber()).isEqualTo(UPDATED_SERIAL_NUMBER);
        assertThat(testMachine.getSupportedProtocol()).isEqualTo(DEFAULT_SUPPORTED_PROTOCOL);
        assertThat(testMachine.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testMachine.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateMachineWithPatch() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        int databaseSizeBeforeUpdate = machineRepository.findAll().size();

        // Update the machine using partial update
        Machine partialUpdatedMachine = new Machine();
        partialUpdatedMachine.setId(machine.getId());

        partialUpdatedMachine
            .reference(UPDATED_REFERENCE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .firmwareVersion(UPDATED_FIRMWARE_VERSION)
            .hardwareVersion(UPDATED_HARDWARE_VERSION)
            .softwareVersion(UPDATED_SOFTWARE_VERSION)
            .serialNumber(UPDATED_SERIAL_NUMBER)
            .supportedProtocol(UPDATED_SUPPORTED_PROTOCOL)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restMachineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMachine.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMachine))
            )
            .andExpect(status().isOk());

        // Validate the Machine in the database
        List<Machine> machineList = machineRepository.findAll();
        assertThat(machineList).hasSize(databaseSizeBeforeUpdate);
        Machine testMachine = machineList.get(machineList.size() - 1);
        assertThat(testMachine.getReference()).isEqualTo(UPDATED_REFERENCE);
        assertThat(testMachine.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMachine.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMachine.getFirmwareVersion()).isEqualTo(UPDATED_FIRMWARE_VERSION);
        assertThat(testMachine.getHardwareVersion()).isEqualTo(UPDATED_HARDWARE_VERSION);
        assertThat(testMachine.getSoftwareVersion()).isEqualTo(UPDATED_SOFTWARE_VERSION);
        assertThat(testMachine.getSerialNumber()).isEqualTo(UPDATED_SERIAL_NUMBER);
        assertThat(testMachine.getSupportedProtocol()).isEqualTo(UPDATED_SUPPORTED_PROTOCOL);
        assertThat(testMachine.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testMachine.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingMachine() throws Exception {
        int databaseSizeBeforeUpdate = machineRepository.findAll().size();
        machine.setId(longCount.incrementAndGet());

        // Create the Machine
        MachineDTO machineDTO = machineMapper.toDto(machine);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMachineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, machineDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(machineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Machine in the database
        List<Machine> machineList = machineRepository.findAll();
        assertThat(machineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMachine() throws Exception {
        int databaseSizeBeforeUpdate = machineRepository.findAll().size();
        machine.setId(longCount.incrementAndGet());

        // Create the Machine
        MachineDTO machineDTO = machineMapper.toDto(machine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMachineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(machineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Machine in the database
        List<Machine> machineList = machineRepository.findAll();
        assertThat(machineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMachine() throws Exception {
        int databaseSizeBeforeUpdate = machineRepository.findAll().size();
        machine.setId(longCount.incrementAndGet());

        // Create the Machine
        MachineDTO machineDTO = machineMapper.toDto(machine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMachineMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(machineDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Machine in the database
        List<Machine> machineList = machineRepository.findAll();
        assertThat(machineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMachine() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        int databaseSizeBeforeDelete = machineRepository.findAll().size();

        // Delete the machine
        restMachineMockMvc
            .perform(delete(ENTITY_API_URL_ID, machine.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Machine> machineList = machineRepository.findAll();
        assertThat(machineList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
