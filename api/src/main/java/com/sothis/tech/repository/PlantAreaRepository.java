package com.sothis.tech.repository;

import com.sothis.tech.domain.PlantArea;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PlantArea entity.
 */
@Repository
public interface PlantAreaRepository extends JpaRepository<PlantArea, Long>, JpaSpecificationExecutor<PlantArea> {
    default Optional<PlantArea> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<PlantArea> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<PlantArea> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select plantArea from PlantArea plantArea left join fetch plantArea.plant",
        countQuery = "select count(plantArea) from PlantArea plantArea"
    )
    Page<PlantArea> findAllWithToOneRelationships(Pageable pageable);

    @Query("select plantArea from PlantArea plantArea left join fetch plantArea.plant")
    List<PlantArea> findAllWithToOneRelationships();

    @Query("select plantArea from PlantArea plantArea left join fetch plantArea.plant where plantArea.id =:id")
    Optional<PlantArea> findOneWithToOneRelationships(@Param("id") Long id);
}
