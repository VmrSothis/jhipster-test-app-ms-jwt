package com.sothis.tech.repository;

import com.sothis.tech.domain.Site;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Site entity.
 */
@Repository
public interface SiteRepository extends JpaRepository<Site, Long>, JpaSpecificationExecutor<Site> {
    default Optional<Site> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Site> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Site> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(value = "select site from Site site left join fetch site.organization", countQuery = "select count(site) from Site site")
    Page<Site> findAllWithToOneRelationships(Pageable pageable);

    @Query("select site from Site site left join fetch site.organization")
    List<Site> findAllWithToOneRelationships();

    @Query("select site from Site site left join fetch site.organization where site.id =:id")
    Optional<Site> findOneWithToOneRelationships(@Param("id") Long id);
}
