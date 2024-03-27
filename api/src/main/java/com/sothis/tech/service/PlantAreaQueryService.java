package com.sothis.tech.service;

import com.sothis.tech.domain.*; // for static metamodels
import com.sothis.tech.domain.PlantArea;
import com.sothis.tech.repository.PlantAreaRepository;
import com.sothis.tech.service.criteria.PlantAreaCriteria;
import com.sothis.tech.service.dto.PlantAreaDTO;
import com.sothis.tech.service.mapper.PlantAreaMapper;
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
 * Service for executing complex queries for {@link PlantArea} entities in the database.
 * The main input is a {@link PlantAreaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PlantAreaDTO} or a {@link Page} of {@link PlantAreaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PlantAreaQueryService extends QueryService<PlantArea> {

    private final Logger log = LoggerFactory.getLogger(PlantAreaQueryService.class);

    private final PlantAreaRepository plantAreaRepository;

    private final PlantAreaMapper plantAreaMapper;

    public PlantAreaQueryService(PlantAreaRepository plantAreaRepository, PlantAreaMapper plantAreaMapper) {
        this.plantAreaRepository = plantAreaRepository;
        this.plantAreaMapper = plantAreaMapper;
    }

    /**
     * Return a {@link List} of {@link PlantAreaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PlantAreaDTO> findByCriteria(PlantAreaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PlantArea> specification = createSpecification(criteria);
        return plantAreaMapper.toDto(plantAreaRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PlantAreaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PlantAreaDTO> findByCriteria(PlantAreaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PlantArea> specification = createSpecification(criteria);
        return plantAreaRepository.findAll(specification, page).map(plantAreaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PlantAreaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PlantArea> specification = createSpecification(criteria);
        return plantAreaRepository.count(specification);
    }

    /**
     * Function to convert {@link PlantAreaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PlantArea> createSpecification(PlantAreaCriteria criteria) {
        Specification<PlantArea> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PlantArea_.id));
            }
            if (criteria.getReference() != null) {
                specification = specification.and(buildStringSpecification(criteria.getReference(), PlantArea_.reference));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), PlantArea_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), PlantArea_.description));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), PlantArea_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), PlantArea_.updatedAt));
            }
            if (criteria.getPlantId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getPlantId(), root -> root.join(PlantArea_.plant, JoinType.LEFT).get(Plant_.id))
                    );
            }
        }
        return specification;
    }
}
