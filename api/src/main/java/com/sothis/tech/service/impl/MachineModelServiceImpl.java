package com.sothis.tech.service.impl;

import com.sothis.tech.domain.MachineModel;
import com.sothis.tech.repository.MachineModelRepository;
import com.sothis.tech.service.MachineModelService;
import com.sothis.tech.service.dto.MachineModelDTO;
import com.sothis.tech.service.mapper.MachineModelMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.sothis.tech.domain.MachineModel}.
 */
@Service
@Transactional
public class MachineModelServiceImpl implements MachineModelService {

    private final Logger log = LoggerFactory.getLogger(MachineModelServiceImpl.class);

    private final MachineModelRepository machineModelRepository;

    private final MachineModelMapper machineModelMapper;

    public MachineModelServiceImpl(MachineModelRepository machineModelRepository, MachineModelMapper machineModelMapper) {
        this.machineModelRepository = machineModelRepository;
        this.machineModelMapper = machineModelMapper;
    }

    @Override
    public MachineModelDTO save(MachineModelDTO machineModelDTO) {
        log.debug("Request to save MachineModel : {}", machineModelDTO);
        MachineModel machineModel = machineModelMapper.toEntity(machineModelDTO);
        machineModel = machineModelRepository.save(machineModel);
        return machineModelMapper.toDto(machineModel);
    }

    @Override
    public MachineModelDTO update(MachineModelDTO machineModelDTO) {
        log.debug("Request to update MachineModel : {}", machineModelDTO);
        MachineModel machineModel = machineModelMapper.toEntity(machineModelDTO);
        machineModel = machineModelRepository.save(machineModel);
        return machineModelMapper.toDto(machineModel);
    }

    @Override
    public Optional<MachineModelDTO> partialUpdate(MachineModelDTO machineModelDTO) {
        log.debug("Request to partially update MachineModel : {}", machineModelDTO);

        return machineModelRepository
            .findById(machineModelDTO.getId())
            .map(existingMachineModel -> {
                machineModelMapper.partialUpdate(existingMachineModel, machineModelDTO);

                return existingMachineModel;
            })
            .map(machineModelRepository::save)
            .map(machineModelMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MachineModelDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MachineModels");
        return machineModelRepository.findAll(pageable).map(machineModelMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MachineModelDTO> findOne(Long id) {
        log.debug("Request to get MachineModel : {}", id);
        return machineModelRepository.findById(id).map(machineModelMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete MachineModel : {}", id);
        machineModelRepository.deleteById(id);
    }
}
