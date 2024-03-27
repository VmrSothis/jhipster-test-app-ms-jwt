package com.sothis.tech.service;

import com.sothis.tech.domain.*; // for static metamodels
import com.sothis.tech.domain.MachineModel;
import com.sothis.tech.repository.MachineModelRepository;
import com.sothis.tech.service.criteria.MachineModelCriteria;
import com.sothis.tech.service.dto.MachineModelDTO;
import com.sothis.tech.service.mapper.MachineModelMapper;
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
 * Service for executing complex queries for {@link MachineModel} entities in the database.
 * The main input is a {@link MachineModelCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MachineModelDTO} or a {@link Page} of {@link MachineModelDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MachineModelQueryService extends QueryService<MachineModel> {

    private final Logger log = LoggerFactory.getLogger(MachineModelQueryService.class);

    private final MachineModelRepository machineModelRepository;

    private final MachineModelMapper machineModelMapper;

    public MachineModelQueryService(MachineModelRepository machineModelRepository, MachineModelMapper machineModelMapper) {
        this.machineModelRepository = machineModelRepository;
        this.machineModelMapper = machineModelMapper;
    }

    /**
     * Return a {@link List} of {@link MachineModelDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MachineModelDTO> findByCriteria(MachineModelCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<MachineModel> specification = createSpecification(criteria);
        return machineModelMapper.toDto(machineModelRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MachineModelDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MachineModelDTO> findByCriteria(MachineModelCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MachineModel> specification = createSpecification(criteria);
        return machineModelRepository.findAll(specification, page).map(machineModelMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MachineModelCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<MachineModel> specification = createSpecification(criteria);
        return machineModelRepository.count(specification);
    }

    /**
     * Function to convert {@link MachineModelCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MachineModel> createSpecification(MachineModelCriteria criteria) {
        Specification<MachineModel> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), MachineModel_.id));
            }
            if (criteria.getReference() != null) {
                specification = specification.and(buildStringSpecification(criteria.getReference(), MachineModel_.reference));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), MachineModel_.name));
            }
            if (criteria.getBrandName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBrandName(), MachineModel_.brandName));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), MachineModel_.description));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), MachineModel_.type));
            }
            if (criteria.getManufacurerName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getManufacurerName(), MachineModel_.manufacurerName));
            }
            if (criteria.getVersion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVersion(), MachineModel_.version));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), MachineModel_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), MachineModel_.updatedAt));
            }
        }
        return specification;
    }
}
