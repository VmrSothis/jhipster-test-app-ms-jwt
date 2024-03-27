package com.sothis.tech.service.mapper;

import com.sothis.tech.domain.Plant;
import com.sothis.tech.domain.PlantArea;
import com.sothis.tech.service.dto.PlantAreaDTO;
import com.sothis.tech.service.dto.PlantDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PlantArea} and its DTO {@link PlantAreaDTO}.
 */
@Mapper(componentModel = "spring")
public interface PlantAreaMapper extends EntityMapper<PlantAreaDTO, PlantArea> {
    @Mapping(target = "plant", source = "plant", qualifiedByName = "plantName")
    PlantAreaDTO toDto(PlantArea s);

    @Named("plantName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    PlantDTO toDtoPlantName(Plant plant);
}
