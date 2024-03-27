package com.sothis.tech.service.mapper;

import com.sothis.tech.domain.MachineModel;
import com.sothis.tech.service.dto.MachineModelDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MachineModel} and its DTO {@link MachineModelDTO}.
 */
@Mapper(componentModel = "spring")
public interface MachineModelMapper extends EntityMapper<MachineModelDTO, MachineModel> {}
