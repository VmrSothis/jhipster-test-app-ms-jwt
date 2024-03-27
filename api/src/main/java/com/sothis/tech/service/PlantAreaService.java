package com.sothis.tech.service;

import com.sothis.tech.service.dto.PlantAreaDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.sothis.tech.domain.PlantArea}.
 */
public interface PlantAreaService {
    /**
     * Save a plantArea.
     *
     * @param plantAreaDTO the entity to save.
     * @return the persisted entity.
     */
    PlantAreaDTO save(PlantAreaDTO plantAreaDTO);

    /**
     * Updates a plantArea.
     *
     * @param plantAreaDTO the entity to update.
     * @return the persisted entity.
     */
    PlantAreaDTO update(PlantAreaDTO plantAreaDTO);

    /**
     * Partially updates a plantArea.
     *
     * @param plantAreaDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PlantAreaDTO> partialUpdate(PlantAreaDTO plantAreaDTO);

    /**
     * Get all the plantAreas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PlantAreaDTO> findAll(Pageable pageable);

    /**
     * Get all the plantAreas with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PlantAreaDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" plantArea.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PlantAreaDTO> findOne(Long id);

    /**
     * Delete the "id" plantArea.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
