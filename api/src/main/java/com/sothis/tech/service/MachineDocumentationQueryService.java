package com.sothis.tech.service;

import com.sothis.tech.domain.*; // for static metamodels
import com.sothis.tech.domain.MachineDocumentation;
import com.sothis.tech.repository.MachineDocumentationRepository;
import com.sothis.tech.service.criteria.MachineDocumentationCriteria;
import com.sothis.tech.service.dto.MachineDocumentationDTO;
import com.sothis.tech.service.mapper.MachineDocumentationMapper;
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
 * Service for executing complex queries for {@link MachineDocumentation} entities in the database.
 * The main input is a {@link MachineDocumentationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MachineDocumentationDTO} or a {@link Page} of {@link MachineDocumentationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MachineDocumentationQueryService extends QueryService<MachineDocumentation> {

    private final Logger log = LoggerFactory.getLogger(MachineDocumentationQueryService.class);

    private final MachineDocumentationRepository machineDocumentationRepository;

    private final MachineDocumentationMapper machineDocumentationMapper;

    public MachineDocumentationQueryService(
        MachineDocumentationRepository machineDocumentationRepository,
        MachineDocumentationMapper machineDocumentationMapper
    ) {
        this.machineDocumentationRepository = machineDocumentationRepository;
        this.machineDocumentationMapper = machineDocumentationMapper;
    }

    /**
     * Return a {@link List} of {@link MachineDocumentationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MachineDocumentationDTO> findByCriteria(MachineDocumentationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<MachineDocumentation> specification = createSpecification(criteria);
        return machineDocumentationMapper.toDto(machineDocumentationRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MachineDocumentationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MachineDocumentationDTO> findByCriteria(MachineDocumentationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MachineDocumentation> specification = createSpecification(criteria);
        return machineDocumentationRepository.findAll(specification, page).map(machineDocumentationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MachineDocumentationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<MachineDocumentation> specification = createSpecification(criteria);
        return machineDocumentationRepository.count(specification);
    }

    /**
     * Function to convert {@link MachineDocumentationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MachineDocumentation> createSpecification(MachineDocumentationCriteria criteria) {
        Specification<MachineDocumentation> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), MachineDocumentation_.id));
            }
            if (criteria.getReference() != null) {
                specification = specification.and(buildStringSpecification(criteria.getReference(), MachineDocumentation_.reference));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), MachineDocumentation_.name));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildSpecification(criteria.getType(), MachineDocumentation_.type));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), MachineDocumentation_.description));
            }
            if (criteria.getUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUrl(), MachineDocumentation_.url));
            }
            if (criteria.getMachineId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getMachineId(),
                            root -> root.join(MachineDocumentation_.machine, JoinType.LEFT).get(Machine_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
