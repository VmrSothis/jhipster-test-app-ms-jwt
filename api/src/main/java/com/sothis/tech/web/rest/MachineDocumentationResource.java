package com.sothis.tech.web.rest;

import com.sothis.tech.repository.MachineDocumentationRepository;
import com.sothis.tech.service.MachineDocumentationQueryService;
import com.sothis.tech.service.MachineDocumentationService;
import com.sothis.tech.service.criteria.MachineDocumentationCriteria;
import com.sothis.tech.service.dto.MachineDocumentationDTO;
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
 * REST controller for managing {@link com.sothis.tech.domain.MachineDocumentation}.
 */
@RestController
@RequestMapping("/api/machine-documentations")
public class MachineDocumentationResource {

    private final Logger log = LoggerFactory.getLogger(MachineDocumentationResource.class);

    private static final String ENTITY_NAME = "machineDocumentation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MachineDocumentationService machineDocumentationService;

    private final MachineDocumentationRepository machineDocumentationRepository;

    private final MachineDocumentationQueryService machineDocumentationQueryService;

    public MachineDocumentationResource(
        MachineDocumentationService machineDocumentationService,
        MachineDocumentationRepository machineDocumentationRepository,
        MachineDocumentationQueryService machineDocumentationQueryService
    ) {
        this.machineDocumentationService = machineDocumentationService;
        this.machineDocumentationRepository = machineDocumentationRepository;
        this.machineDocumentationQueryService = machineDocumentationQueryService;
    }

    /**
     * {@code POST  /machine-documentations} : Create a new machineDocumentation.
     *
     * @param machineDocumentationDTO the machineDocumentationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new machineDocumentationDTO, or with status {@code 400 (Bad Request)} if the machineDocumentation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MachineDocumentationDTO> createMachineDocumentation(
        @Valid @RequestBody MachineDocumentationDTO machineDocumentationDTO
    ) throws URISyntaxException {
        log.debug("REST request to save MachineDocumentation : {}", machineDocumentationDTO);
        if (machineDocumentationDTO.getId() != null) {
            throw new BadRequestAlertException("A new machineDocumentation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MachineDocumentationDTO result = machineDocumentationService.save(machineDocumentationDTO);
        return ResponseEntity
            .created(new URI("/api/machine-documentations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /machine-documentations/:id} : Updates an existing machineDocumentation.
     *
     * @param id the id of the machineDocumentationDTO to save.
     * @param machineDocumentationDTO the machineDocumentationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated machineDocumentationDTO,
     * or with status {@code 400 (Bad Request)} if the machineDocumentationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the machineDocumentationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MachineDocumentationDTO> updateMachineDocumentation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MachineDocumentationDTO machineDocumentationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update MachineDocumentation : {}, {}", id, machineDocumentationDTO);
        if (machineDocumentationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, machineDocumentationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!machineDocumentationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MachineDocumentationDTO result = machineDocumentationService.update(machineDocumentationDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, machineDocumentationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /machine-documentations/:id} : Partial updates given fields of an existing machineDocumentation, field will ignore if it is null
     *
     * @param id the id of the machineDocumentationDTO to save.
     * @param machineDocumentationDTO the machineDocumentationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated machineDocumentationDTO,
     * or with status {@code 400 (Bad Request)} if the machineDocumentationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the machineDocumentationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the machineDocumentationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MachineDocumentationDTO> partialUpdateMachineDocumentation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MachineDocumentationDTO machineDocumentationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update MachineDocumentation partially : {}, {}", id, machineDocumentationDTO);
        if (machineDocumentationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, machineDocumentationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!machineDocumentationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MachineDocumentationDTO> result = machineDocumentationService.partialUpdate(machineDocumentationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, machineDocumentationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /machine-documentations} : get all the machineDocumentations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of machineDocumentations in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MachineDocumentationDTO>> getAllMachineDocumentations(
        MachineDocumentationCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get MachineDocumentations by criteria: {}", criteria);

        Page<MachineDocumentationDTO> page = machineDocumentationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /machine-documentations/count} : count all the machineDocumentations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countMachineDocumentations(MachineDocumentationCriteria criteria) {
        log.debug("REST request to count MachineDocumentations by criteria: {}", criteria);
        return ResponseEntity.ok().body(machineDocumentationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /machine-documentations/:id} : get the "id" machineDocumentation.
     *
     * @param id the id of the machineDocumentationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the machineDocumentationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MachineDocumentationDTO> getMachineDocumentation(@PathVariable("id") Long id) {
        log.debug("REST request to get MachineDocumentation : {}", id);
        Optional<MachineDocumentationDTO> machineDocumentationDTO = machineDocumentationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(machineDocumentationDTO);
    }

    /**
     * {@code DELETE  /machine-documentations/:id} : delete the "id" machineDocumentation.
     *
     * @param id the id of the machineDocumentationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMachineDocumentation(@PathVariable("id") Long id) {
        log.debug("REST request to delete MachineDocumentation : {}", id);
        machineDocumentationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
