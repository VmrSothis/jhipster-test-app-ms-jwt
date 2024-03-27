package com.sothis.tech.service.mapper;

import com.sothis.tech.domain.Plant;
import com.sothis.tech.domain.Site;
import com.sothis.tech.service.dto.PlantDTO;
import com.sothis.tech.service.dto.SiteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Plant} and its DTO {@link PlantDTO}.
 */
@Mapper(componentModel = "spring")
public interface PlantMapper extends EntityMapper<PlantDTO, Plant> {
    @Mapping(target = "site", source = "site", qualifiedByName = "siteName")
    PlantDTO toDto(Plant s);

    @Named("siteName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    SiteDTO toDtoSiteName(Site site);
}
