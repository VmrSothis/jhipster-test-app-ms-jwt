package com.sothis.tech.service.mapper;

import com.sothis.tech.domain.Organization;
import com.sothis.tech.domain.Site;
import com.sothis.tech.service.dto.OrganizationDTO;
import com.sothis.tech.service.dto.SiteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Site} and its DTO {@link SiteDTO}.
 */
@Mapper(componentModel = "spring")
public interface SiteMapper extends EntityMapper<SiteDTO, Site> {
    @Mapping(target = "organization", source = "organization", qualifiedByName = "organizationName")
    SiteDTO toDto(Site s);

    @Named("organizationName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    OrganizationDTO toDtoOrganizationName(Organization organization);
}
