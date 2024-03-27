package com.sothis.tech.web.rest;

import com.sothis.tech.repository.MachineModelRepository;
import com.sothis.tech.service.MachineModelQueryService;
import com.sothis.tech.service.MachineModelService;
import com.sothis.tech.service.criteria.MachineModelCriteria;
import com.sothis.tech.service.dto.MachineModelDTO;
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
 * REST controller for managing {@link com.sothis.tech.domain.MachineModel}.
 */
@RestController
@RequestMapping("/api/machine-models")
public class MachineModelResource {

    private final Logger log = LoggerFactory.getLogger(MachineModelResource.class);

    private static final String ENTITY_NAME = "machineModel";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MachineModelService machineModelService;

    private final MachineModelRepository machineModelRepository;

    private final MachineModelQueryService machineModelQueryService;

    public MachineModelResource(
        MachineModelService machineModelService,
        MachineModelRepository machineModelRepository,
        MachineModelQueryService machineModelQueryService
    ) {
        this.machineModelService = machineModelService;
        this.machineModelRepository = machineModelRepository;
        this.machineModelQueryService = machineModelQueryService;
    }

    /**
     * {@code POST  /machine-models} : Create a new machineModel.
     *
     * @param machineModelDTO the machineModelDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new machineModelDTO, or with status {@code 400 (Bad Request)} if the machineModel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MachineModelDTO> createMachineModel(@Valid @RequestBody MachineModelDTO machineModelDTO)
        throws URISyntaxException {
        log.debug("REST request to save MachineModel : {}", machineModelDTO);
        if (machineModelDTO.getId() != null) {
            throw new BadRequestAlertException("A new machineModel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MachineModelDTO result = machineModelService.save(machineModelDTO);
        return ResponseEntity
            .created(new URI("/api/machine-models/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /machine-models/:id} : Updates an existing machineModel.
     *
     * @param id the id of the machineModelDTO to save.
     * @param machineModelDTO the machineModelDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated machineModelDTO,
     * or with status {@code 400 (Bad Request)} if the machineModelDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the machineModelDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MachineModelDTO> updateMachineModel(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MachineModelDTO machineModelDTO
    ) throws URISyntaxException {
        log.debug("REST request to update MachineModel : {}, {}", id, machineModelDTO);
        if (machineModelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, machineModelDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!machineModelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MachineModelDTO result = machineModelService.update(machineModelDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, machineModelDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /machine-models/:id} : Partial updates given fields of an existing machineModel, field will ignore if it is null
     *
     * @param id the id of the machineModelDTO to save.
     * @param machineModelDTO the machineModelDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated machineModelDTO,
     * or with status {@code 400 (Bad Request)} if the machineModelDTO is not valid,
     * or with status {@code 404 (Not Found)} if the machineModelDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the machineModelDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MachineModelDTO> partialUpdateMachineModel(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MachineModelDTO machineModelDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update MachineModel partially : {}, {}", id, machineModelDTO);
        if (machineModelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, machineModelDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!machineModelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MachineModelDTO> result = machineModelService.partialUpdate(machineModelDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, machineModelDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /machine-models} : get all the machineModels.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of machineModels in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MachineModelDTO>> getAllMachineModels(
        MachineModelCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get MachineModels by criteria: {}", criteria);

        Page<MachineModelDTO> page = machineModelQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /machine-models/count} : count all the machineModels.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countMachineModels(MachineModelCriteria criteria) {
        log.debug("REST request to count MachineModels by criteria: {}", criteria);
        return ResponseEntity.ok().body(machineModelQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /machine-models/:id} : get the "id" machineModel.
     *
     * @param id the id of the machineModelDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the machineModelDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MachineModelDTO> getMachineModel(@PathVariable("id") Long id) {
        log.debug("REST request to get MachineModel : {}", id);
        Optional<MachineModelDTO> machineModelDTO = machineModelService.findOne(id);
        return ResponseUtil.wrapOrNotFound(machineModelDTO);
    }

    /**
     * {@code DELETE  /machine-models/:id} : delete the "id" machineModel.
     *
     * @param id the id of the machineModelDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMachineModel(@PathVariable("id") Long id) {
        log.debug("REST request to delete MachineModel : {}", id);
        machineModelService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
