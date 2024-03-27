package com.sothis.tech.service.impl;

import com.sothis.tech.domain.Plant;
import com.sothis.tech.repository.PlantRepository;
import com.sothis.tech.service.PlantService;
import com.sothis.tech.service.dto.PlantDTO;
import com.sothis.tech.service.mapper.PlantMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.sothis.tech.domain.Plant}.
 */
@Service
@Transactional
public class PlantServiceImpl implements PlantService {

    private final Logger log = LoggerFactory.getLogger(PlantServiceImpl.class);

    private final PlantRepository plantRepository;

    private final PlantMapper plantMapper;

    public PlantServiceImpl(PlantRepository plantRepository, PlantMapper plantMapper) {
        this.plantRepository = plantRepository;
        this.plantMapper = plantMapper;
    }

    @Override
    public PlantDTO save(PlantDTO plantDTO) {
        log.debug("Request to save Plant : {}", plantDTO);
        Plant plant = plantMapper.toEntity(plantDTO);
        plant = plantRepository.save(plant);
        return plantMapper.toDto(plant);
    }

    @Override
    public PlantDTO update(PlantDTO plantDTO) {
        log.debug("Request to update Plant : {}", plantDTO);
        Plant plant = plantMapper.toEntity(plantDTO);
        plant = plantRepository.save(plant);
        return plantMapper.toDto(plant);
    }

    @Override
    public Optional<PlantDTO> partialUpdate(PlantDTO plantDTO) {
        log.debug("Request to partially update Plant : {}", plantDTO);

        return plantRepository
            .findById(plantDTO.getId())
            .map(existingPlant -> {
                plantMapper.partialUpdate(existingPlant, plantDTO);

                return existingPlant;
            })
            .map(plantRepository::save)
            .map(plantMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PlantDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Plants");
        return plantRepository.findAll(pageable).map(plantMapper::toDto);
    }

    public Page<PlantDTO> findAllWithEagerRelationships(Pageable pageable) {
        return plantRepository.findAllWithEagerRelationships(pageable).map(plantMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PlantDTO> findOne(Long id) {
        log.debug("Request to get Plant : {}", id);
        return plantRepository.findOneWithEagerRelationships(id).map(plantMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Plant : {}", id);
        plantRepository.deleteById(id);
    }
}
