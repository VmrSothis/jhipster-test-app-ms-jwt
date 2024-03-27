package com.sothis.tech.service;

import com.sothis.tech.service.dto.MachineDocumentationDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.sothis.tech.domain.MachineDocumentation}.
 */
public interface MachineDocumentationService {
    /**
     * Save a machineDocumentation.
     *
     * @param machineDocumentationDTO the entity to save.
     * @return the persisted entity.
     */
    MachineDocumentationDTO save(MachineDocumentationDTO machineDocumentationDTO);

    /**
     * Updates a machineDocumentation.
     *
     * @param machineDocumentationDTO the entity to update.
     * @return the persisted entity.
     */
    MachineDocumentationDTO update(MachineDocumentationDTO machineDocumentationDTO);

    /**
     * Partially updates a machineDocumentation.
     *
     * @param machineDocumentationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MachineDocumentationDTO> partialUpdate(MachineDocumentationDTO machineDocumentationDTO);

    /**
     * Get all the machineDocumentations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MachineDocumentationDTO> findAll(Pageable pageable);

    /**
     * Get all the machineDocumentations with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MachineDocumentationDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" machineDocumentation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MachineDocumentationDTO> findOne(Long id);

    /**
     * Delete the "id" machineDocumentation.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
