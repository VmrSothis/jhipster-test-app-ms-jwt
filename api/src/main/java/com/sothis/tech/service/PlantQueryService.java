package com.sothis.tech.service;

import com.sothis.tech.domain.*; // for static metamodels
import com.sothis.tech.domain.Plant;
import com.sothis.tech.repository.PlantRepository;
import com.sothis.tech.service.criteria.PlantCriteria;
import com.sothis.tech.service.dto.PlantDTO;
import com.sothis.tech.service.mapper.PlantMapper;
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
 * Service for executing complex queries for {@link Plant} entities in the database.
 * The main input is a {@link PlantCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PlantDTO} or a {@link Page} of {@link PlantDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PlantQueryService extends QueryService<Plant> {

    private final Logger log = LoggerFactory.getLogger(PlantQueryService.class);

    private final PlantRepository plantRepository;

    private final PlantMapper plantMapper;

    public PlantQueryService(PlantRepository plantRepository, PlantMapper plantMapper) {
        this.plantRepository = plantRepository;
        this.plantMapper = plantMapper;
    }

    /**
     * Return a {@link List} of {@link PlantDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PlantDTO> findByCriteria(PlantCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Plant> specification = createSpecification(criteria);
        return plantMapper.toDto(plantRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PlantDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PlantDTO> findByCriteria(PlantCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Plant> specification = createSpecification(criteria);
        return plantRepository.findAll(specification, page).map(plantMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PlantCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Plant> specification = createSpecification(criteria);
        return plantRepository.count(specification);
    }

    /**
     * Function to convert {@link PlantCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Plant> createSpecification(PlantCriteria criteria) {
        Specification<Plant> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Plant_.id));
            }
            if (criteria.getReference() != null) {
                specification = specification.and(buildStringSpecification(criteria.getReference(), Plant_.reference));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Plant_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Plant_.description));
            }
            if (criteria.getAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddress(), Plant_.address));
            }
            if (criteria.getLocation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLocation(), Plant_.location));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), Plant_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), Plant_.updatedAt));
            }
            if (criteria.getSiteId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getSiteId(), root -> root.join(Plant_.site, JoinType.LEFT).get(Site_.id))
                    );
            }
        }
        return specification;
    }
}
