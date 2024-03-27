package com.sothis.tech.service;

import com.sothis.tech.service.dto.MachineModelDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.sothis.tech.domain.MachineModel}.
 */
public interface MachineModelService {
    /**
     * Save a machineModel.
     *
     * @param machineModelDTO the entity to save.
     * @return the persisted entity.
     */
    MachineModelDTO save(MachineModelDTO machineModelDTO);

    /**
     * Updates a machineModel.
     *
     * @param machineModelDTO the entity to update.
     * @return the persisted entity.
     */
    MachineModelDTO update(MachineModelDTO machineModelDTO);

    /**
     * Partially updates a machineModel.
     *
     * @param machineModelDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MachineModelDTO> partialUpdate(MachineModelDTO machineModelDTO);

    /**
     * Get all the machineModels.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MachineModelDTO> findAll(Pageable pageable);

    /**
     * Get the "id" machineModel.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MachineModelDTO> findOne(Long id);

    /**
     * Delete the "id" machineModel.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
