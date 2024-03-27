package com.sothis.tech.service;

import com.sothis.tech.service.dto.PlantDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.sothis.tech.domain.Plant}.
 */
public interface PlantService {
    /**
     * Save a plant.
     *
     * @param plantDTO the entity to save.
     * @return the persisted entity.
     */
    PlantDTO save(PlantDTO plantDTO);

    /**
     * Updates a plant.
     *
     * @param plantDTO the entity to update.
     * @return the persisted entity.
     */
    PlantDTO update(PlantDTO plantDTO);

    /**
     * Partially updates a plant.
     *
     * @param plantDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PlantDTO> partialUpdate(PlantDTO plantDTO);

    /**
     * Get all the plants.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PlantDTO> findAll(Pageable pageable);

    /**
     * Get all the plants with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PlantDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" plant.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PlantDTO> findOne(Long id);

    /**
     * Delete the "id" plant.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
