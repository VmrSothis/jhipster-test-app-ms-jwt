package com.sothis.tech.service;

import com.sothis.tech.domain.*; // for static metamodels
import com.sothis.tech.domain.Machine;
import com.sothis.tech.repository.MachineRepository;
import com.sothis.tech.service.criteria.MachineCriteria;
import com.sothis.tech.service.dto.MachineDTO;
import com.sothis.tech.service.mapper.MachineMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Machine} entities in the database.
 * The main input is a {@link MachineCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MachineDTO} or a {@link Page} of {@link MachineDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MachineQueryService extends QueryService<Machine> {

    private final Logger log = LoggerFactory.getLogger(MachineQueryService.class);

    private final MachineRepository machineRepository;

    private final MachineMapper machineMapper;

    public MachineQueryService(MachineRepository machineRepository, MachineMapper machineMapper) {
        this.machineRepository = machineRepository;
        this.machineMapper = machineMapper;
    }

    /**
     * Return a {@link List} of {@link MachineDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MachineDTO> findByCriteria(MachineCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Machine> specification = createSpecification(criteria);
        return machineMapper.toDto(machineRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MachineDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MachineDTO> findByCriteria(MachineCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Machine> specification = createSpecification(criteria);
        return machineRepository.findAll(specification, page).map(machineMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MachineCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Machine> specification = createSpecification(criteria);
        return machineRepository.count(specification);
    }

    /**
     * Function to convert {@link MachineCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Machine> createSpecification(MachineCriteria criteria) {
        Specification<Machine> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Machine_.id));
            }
            if (criteria.getReference() != null) {
                specification = specification.and(buildStringSpecification(criteria.getReference(), Machine_.reference));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Machine_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Machine_.description));
            }
            if (criteria.getFirmwareVersion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirmwareVersion(), Machine_.firmwareVersion));
            }
            if (criteria.getHardwareVersion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getHardwareVersion(), Machine_.hardwareVersion));
            }
            if (criteria.getSoftwareVersion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSoftwareVersion(), Machine_.softwareVersion));
            }
            if (criteria.getSerialNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSerialNumber(), Machine_.serialNumber));
            }
            if (criteria.getSupportedProtocol() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSupportedProtocol(), Machine_.supportedProtocol));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), Machine_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), Machine_.updatedAt));
            }
            if (criteria.getPlantAreaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlantAreaId(),
                            root -> root.join(Machine_.plantArea, JoinType.LEFT).get(PlantArea_.id)
                        )
                    );
            }
            if (criteria.getMachineModelId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getMachineModelId(),
                            root -> root.join(Machine_.machineModel, JoinType.LEFT).get(MachineModel_.id)
                        )
                    );
            }
            if (criteria.getOrganizationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getOrganizationId(),
                            root -> root.join(Machine_.organization, JoinType.LEFT).get(Organization_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
