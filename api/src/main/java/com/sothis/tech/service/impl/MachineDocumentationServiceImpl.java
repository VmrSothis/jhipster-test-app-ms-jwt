package com.sothis.tech.service.impl;

import com.sothis.tech.domain.MachineDocumentation;
import com.sothis.tech.repository.MachineDocumentationRepository;
import com.sothis.tech.service.MachineDocumentationService;
import com.sothis.tech.service.dto.MachineDocumentationDTO;
import com.sothis.tech.service.mapper.MachineDocumentationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.sothis.tech.domain.MachineDocumentation}.
 */
@Service
@Transactional
public class MachineDocumentationServiceImpl implements MachineDocumentationService {

    private final Logger log = LoggerFactory.getLogger(MachineDocumentationServiceImpl.class);

    private final MachineDocumentationRepository machineDocumentationRepository;

    private final MachineDocumentationMapper machineDocumentationMapper;

    public MachineDocumentationServiceImpl(
        MachineDocumentationRepository machineDocumentationRepository,
        MachineDocumentationMapper machineDocumentationMapper
    ) {
        this.machineDocumentationRepository = machineDocumentationRepository;
        this.machineDocumentationMapper = machineDocumentationMapper;
    }

    @Override
    public MachineDocumentationDTO save(MachineDocumentationDTO machineDocumentationDTO) {
        log.debug("Request to save MachineDocumentation : {}", machineDocumentationDTO);
        MachineDocumentation machineDocumentation = machineDocumentationMapper.toEntity(machineDocumentationDTO);
        machineDocumentation = machineDocumentationRepository.save(machineDocumentation);
        return machineDocumentationMapper.toDto(machineDocumentation);
    }

    @Override
    public MachineDocumentationDTO update(MachineDocumentationDTO machineDocumentationDTO) {
        log.debug("Request to update MachineDocumentation : {}", machineDocumentationDTO);
        MachineDocumentation machineDocumentation = machineDocumentationMapper.toEntity(machineDocumentationDTO);
        machineDocumentation = machineDocumentationRepository.save(machineDocumentation);
        return machineDocumentationMapper.toDto(machineDocumentation);
    }

    @Override
    public Optional<MachineDocumentationDTO> partialUpdate(MachineDocumentationDTO machineDocumentationDTO) {
        log.debug("Request to partially update MachineDocumentation : {}", machineDocumentationDTO);

        return machineDocumentationRepository
            .findById(machineDocumentationDTO.getId())
            .map(existingMachineDocumentation -> {
                machineDocumentationMapper.partialUpdate(existingMachineDocumentation, machineDocumentationDTO);

                return existingMachineDocumentation;
            })
            .map(machineDocumentationRepository::save)
            .map(machineDocumentationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MachineDocumentationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MachineDocumentations");
        return machineDocumentationRepository.findAll(pageable).map(machineDocumentationMapper::toDto);
    }

    public Page<MachineDocumentationDTO> findAllWithEagerRelationships(Pageable pageable) {
        return machineDocumentationRepository.findAllWithEagerRelationships(pageable).map(machineDocumentationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MachineDocumentationDTO> findOne(Long id) {
        log.debug("Request to get MachineDocumentation : {}", id);
        return machineDocumentationRepository.findOneWithEagerRelationships(id).map(machineDocumentationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete MachineDocumentation : {}", id);
        machineDocumentationRepository.deleteById(id);
    }
}
