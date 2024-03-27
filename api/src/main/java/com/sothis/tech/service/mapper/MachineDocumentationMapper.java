package com.sothis.tech.service.mapper;

import com.sothis.tech.domain.Machine;
import com.sothis.tech.domain.MachineDocumentation;
import com.sothis.tech.service.dto.MachineDTO;
import com.sothis.tech.service.dto.MachineDocumentationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MachineDocumentation} and its DTO {@link MachineDocumentationDTO}.
 */
@Mapper(componentModel = "spring")
public interface MachineDocumentationMapper extends EntityMapper<MachineDocumentationDTO, MachineDocumentation> {
    @Mapping(target = "machine", source = "machine", qualifiedByName = "machineName")
    MachineDocumentationDTO toDto(MachineDocumentation s);

    @Named("machineName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    MachineDTO toDtoMachineName(Machine machine);
}
