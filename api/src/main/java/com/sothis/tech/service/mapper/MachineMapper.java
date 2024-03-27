package com.sothis.tech.service.mapper;

import com.sothis.tech.domain.Machine;
import com.sothis.tech.domain.MachineModel;
import com.sothis.tech.domain.Organization;
import com.sothis.tech.domain.PlantArea;
import com.sothis.tech.service.dto.MachineDTO;
import com.sothis.tech.service.dto.MachineModelDTO;
import com.sothis.tech.service.dto.OrganizationDTO;
import com.sothis.tech.service.dto.PlantAreaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Machine} and its DTO {@link MachineDTO}.
 */
@Mapper(componentModel = "spring")
public interface MachineMapper extends EntityMapper<MachineDTO, Machine> {
    @Mapping(target = "plantArea", source = "plantArea", qualifiedByName = "plantAreaName")
    @Mapping(target = "machineModel", source = "machineModel", qualifiedByName = "machineModelName")
    @Mapping(target = "organization", source = "organization", qualifiedByName = "organizationId")
    MachineDTO toDto(Machine s);

    @Named("plantAreaName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    PlantAreaDTO toDtoPlantAreaName(PlantArea plantArea);

    @Named("machineModelName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    MachineModelDTO toDtoMachineModelName(MachineModel machineModel);

    @Named("organizationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OrganizationDTO toDtoOrganizationId(Organization organization);
}
