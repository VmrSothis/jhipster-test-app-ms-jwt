package com.sothis.tech.repository;

import com.sothis.tech.domain.MachineDocumentation;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MachineDocumentation entity.
 */
@Repository
public interface MachineDocumentationRepository
    extends JpaRepository<MachineDocumentation, Long>, JpaSpecificationExecutor<MachineDocumentation> {
    default Optional<MachineDocumentation> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<MachineDocumentation> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<MachineDocumentation> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select machineDocumentation from MachineDocumentation machineDocumentation left join fetch machineDocumentation.machine",
        countQuery = "select count(machineDocumentation) from MachineDocumentation machineDocumentation"
    )
    Page<MachineDocumentation> findAllWithToOneRelationships(Pageable pageable);

    @Query("select machineDocumentation from MachineDocumentation machineDocumentation left join fetch machineDocumentation.machine")
    List<MachineDocumentation> findAllWithToOneRelationships();

    @Query(
        "select machineDocumentation from MachineDocumentation machineDocumentation left join fetch machineDocumentation.machine where machineDocumentation.id =:id"
    )
    Optional<MachineDocumentation> findOneWithToOneRelationships(@Param("id") Long id);
}
