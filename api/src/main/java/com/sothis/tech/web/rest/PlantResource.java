package com.sothis.tech.web.rest;

import com.sothis.tech.repository.PlantRepository;
import com.sothis.tech.service.PlantQueryService;
import com.sothis.tech.service.PlantService;
import com.sothis.tech.service.criteria.PlantCriteria;
import com.sothis.tech.service.dto.PlantDTO;
import com.sothis.tech.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.sothis.tech.domain.Plant}.
 */
@RestController
@RequestMapping("/api/plants")
public class PlantResource {

    private final Logger log = LoggerFactory.getLogger(PlantResource.class);

    private static final String ENTITY_NAME = "plant";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PlantService plantService;

    private final PlantRepository plantRepository;

    private final PlantQueryService plantQueryService;

    public PlantResource(PlantService plantService, PlantRepository plantRepository, PlantQueryService plantQueryService) {
        this.plantService = plantService;
        this.plantRepository = plantRepository;
        this.plantQueryService = plantQueryService;
    }

    /**
     * {@code POST  /plants} : Create a new plant.
     *
     * @param plantDTO the plantDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new plantDTO, or with status {@code 400 (Bad Request)} if the plant has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PlantDTO> createPlant(@Valid @RequestBody PlantDTO plantDTO) throws URISyntaxException {
        log.debug("REST request to save Plant : {}", plantDTO);
        if (plantDTO.getId() != null) {
            throw new BadRequestAlertException("A new plant cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PlantDTO result = plantService.save(plantDTO);
        return ResponseEntity
            .created(new URI("/api/plants/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /plants/:id} : Updates an existing plant.
     *
     * @param id the id of the plantDTO to save.
     * @param plantDTO the plantDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated plantDTO,
     * or with status {@code 400 (Bad Request)} if the plantDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the plantDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PlantDTO> updatePlant(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PlantDTO plantDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Plant : {}, {}", id, plantDTO);
        if (plantDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, plantDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!plantRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PlantDTO result = plantService.update(plantDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, plantDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /plants/:id} : Partial updates given fields of an existing plant, field will ignore if it is null
     *
     * @param id the id of the plantDTO to save.
     * @param plantDTO the plantDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated plantDTO,
     * or with status {@code 400 (Bad Request)} if the plantDTO is not valid,
     * or with status {@code 404 (Not Found)} if the plantDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the plantDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PlantDTO> partialUpdatePlant(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PlantDTO plantDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Plant partially : {}, {}", id, plantDTO);
        if (plantDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, plantDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!plantRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PlantDTO> result = plantService.partialUpdate(plantDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, plantDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /plants} : get all the plants.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of plants in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PlantDTO>> getAllPlants(
        PlantCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Plants by criteria: {}", criteria);

        Page<PlantDTO> page = plantQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /plants/count} : count all the plants.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPlants(PlantCriteria criteria) {
        log.debug("REST request to count Plants by criteria: {}", criteria);
        return ResponseEntity.ok().body(plantQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /plants/:id} : get the "id" plant.
     *
     * @param id the id of the plantDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the plantDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PlantDTO> getPlant(@PathVariable("id") Long id) {
        log.debug("REST request to get Plant : {}", id);
        Optional<PlantDTO> plantDTO = plantService.findOne(id);
        return ResponseUtil.wrapOrNotFound(plantDTO);
    }

    /**
     * {@code DELETE  /plants/:id} : delete the "id" plant.
     *
     * @param id the id of the plantDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlant(@PathVariable("id") Long id) {
        log.debug("REST request to delete Plant : {}", id);
        plantService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
