package com.sothis.tech.repository;

import com.sothis.tech.domain.MachineModel;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MachineModel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MachineModelRepository extends JpaRepository<MachineModel, Long>, JpaSpecificationExecutor<MachineModel> {}
