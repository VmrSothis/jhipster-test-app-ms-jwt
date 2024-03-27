package com.sothis.tech.service;

import com.sothis.tech.service.dto.SiteDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.sothis.tech.domain.Site}.
 */
public interface SiteService {
    /**
     * Save a site.
     *
     * @param siteDTO the entity to save.
     * @return the persisted entity.
     */
    SiteDTO save(SiteDTO siteDTO);

    /**
     * Updates a site.
     *
     * @param siteDTO the entity to update.
     * @return the persisted entity.
     */
    SiteDTO update(SiteDTO siteDTO);

    /**
     * Partially updates a site.
     *
     * @param siteDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SiteDTO> partialUpdate(SiteDTO siteDTO);

    /**
     * Get all the sites.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SiteDTO> findAll(Pageable pageable);

    /**
     * Get all the sites with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SiteDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" site.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SiteDTO> findOne(Long id);

    /**
     * Delete the "id" site.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
