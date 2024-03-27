package com.sothis.tech.service.impl;

import com.sothis.tech.domain.PlantArea;
import com.sothis.tech.repository.PlantAreaRepository;
import com.sothis.tech.service.PlantAreaService;
import com.sothis.tech.service.dto.PlantAreaDTO;
import com.sothis.tech.service.mapper.PlantAreaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.sothis.tech.domain.PlantArea}.
 */
@Service
@Transactional
public class PlantAreaServiceImpl implements PlantAreaService {

    private final Logger log = LoggerFactory.getLogger(PlantAreaServiceImpl.class);

    private final PlantAreaRepository plantAreaRepository;

    private final PlantAreaMapper plantAreaMapper;

    public PlantAreaServiceImpl(PlantAreaRepository plantAreaRepository, PlantAreaMapper plantAreaMapper) {
        this.plantAreaRepository = plantAreaRepository;
        this.plantAreaMapper = plantAreaMapper;
    }

    @Override
    public PlantAreaDTO save(PlantAreaDTO plantAreaDTO) {
        log.debug("Request to save PlantArea : {}", plantAreaDTO);
        PlantArea plantArea = plantAreaMapper.toEntity(plantAreaDTO);
        plantArea = plantAreaRepository.save(plantArea);
        return plantAreaMapper.toDto(plantArea);
    }

    @Override
    public PlantAreaDTO update(PlantAreaDTO plantAreaDTO) {
        log.debug("Request to update PlantArea : {}", plantAreaDTO);
        PlantArea plantArea = plantAreaMapper.toEntity(plantAreaDTO);
        plantArea = plantAreaRepository.save(plantArea);
        return plantAreaMapper.toDto(plantArea);
    }

    @Override
    public Optional<PlantAreaDTO> partialUpdate(PlantAreaDTO plantAreaDTO) {
        log.debug("Request to partially update PlantArea : {}", plantAreaDTO);

        return plantAreaRepository
            .findById(plantAreaDTO.getId())
            .map(existingPlantArea -> {
                plantAreaMapper.partialUpdate(existingPlantArea, plantAreaDTO);

                return existingPlantArea;
            })
            .map(plantAreaRepository::save)
            .map(plantAreaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PlantAreaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PlantAreas");
        return plantAreaRepository.findAll(pageable).map(plantAreaMapper::toDto);
    }

    public Page<PlantAreaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return plantAreaRepository.findAllWithEagerRelationships(pageable).map(plantAreaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PlantAreaDTO> findOne(Long id) {
        log.debug("Request to get PlantArea : {}", id);
        return plantAreaRepository.findOneWithEagerRelationships(id).map(plantAreaMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PlantArea : {}", id);
        plantAreaRepository.deleteById(id);
    }
}
