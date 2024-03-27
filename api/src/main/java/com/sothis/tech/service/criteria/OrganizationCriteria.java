package com.sothis.tech.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.sothis.tech.domain.Organization} entity. This class is used
 * in {@link com.sothis.tech.web.rest.OrganizationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /organizations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrganizationCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter reference;

    private StringFilter name;

    private StringFilter legalName;

    private StringFilter description;

    private StringFilter taxId;

    private StringFilter email;

    private StringFilter telephone;

    private StringFilter url;

    private StringFilter address;

    private StringFilter postalCode;

    private StringFilter region;

    private StringFilter locality;

    private StringFilter country;

    private StringFilter location;

    private ZonedDateTimeFilter createdAt;

    private ZonedDateTimeFilter updatedAt;

    private LongFilter machineId;

    private Boolean distinct;

    public OrganizationCriteria() {}

    public OrganizationCriteria(OrganizationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.reference = other.reference == null ? null : other.reference.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.legalName = other.legalName == null ? null : other.legalName.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.taxId = other.taxId == null ? null : other.taxId.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.telephone = other.telephone == null ? null : other.telephone.copy();
        this.url = other.url == null ? null : other.url.copy();
        this.address = other.address == null ? null : other.address.copy();
        this.postalCode = other.postalCode == null ? null : other.postalCode.copy();
        this.region = other.region == null ? null : other.region.copy();
        this.locality = other.locality == null ? null : other.locality.copy();
        this.country = other.country == null ? null : other.country.copy();
        this.location = other.location == null ? null : other.location.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.machineId = other.machineId == null ? null : other.machineId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public OrganizationCriteria copy() {
        return new OrganizationCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getReference() {
        return reference;
    }

    public StringFilter reference() {
        if (reference == null) {
            reference = new StringFilter();
        }
        return reference;
    }

    public void setReference(StringFilter reference) {
        this.reference = reference;
    }

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getLegalName() {
        return legalName;
    }

    public StringFilter legalName() {
        if (legalName == null) {
            legalName = new StringFilter();
        }
        return legalName;
    }

    public void setLegalName(StringFilter legalName) {
        this.legalName = legalName;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getTaxId() {
        return taxId;
    }

    public StringFilter taxId() {
        if (taxId == null) {
            taxId = new StringFilter();
        }
        return taxId;
    }

    public void setTaxId(StringFilter taxId) {
        this.taxId = taxId;
    }

    public StringFilter getEmail() {
        return email;
    }

    public StringFilter email() {
        if (email == null) {
            email = new StringFilter();
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getTelephone() {
        return telephone;
    }

    public StringFilter telephone() {
        if (telephone == null) {
            telephone = new StringFilter();
        }
        return telephone;
    }

    public void setTelephone(StringFilter telephone) {
        this.telephone = telephone;
    }

    public StringFilter getUrl() {
        return url;
    }

    public StringFilter url() {
        if (url == null) {
            url = new StringFilter();
        }
        return url;
    }

    public void setUrl(StringFilter url) {
        this.url = url;
    }

    public StringFilter getAddress() {
        return address;
    }

    public StringFilter address() {
        if (address == null) {
            address = new StringFilter();
        }
        return address;
    }

    public void setAddress(StringFilter address) {
        this.address = address;
    }

    public StringFilter getPostalCode() {
        return postalCode;
    }

    public StringFilter postalCode() {
        if (postalCode == null) {
            postalCode = new StringFilter();
        }
        return postalCode;
    }

    public void setPostalCode(StringFilter postalCode) {
        this.postalCode = postalCode;
    }

    public StringFilter getRegion() {
        return region;
    }

    public StringFilter region() {
        if (region == null) {
            region = new StringFilter();
        }
        return region;
    }

    public void setRegion(StringFilter region) {
        this.region = region;
    }

    public StringFilter getLocality() {
        return locality;
    }

    public StringFilter locality() {
        if (locality == null) {
            locality = new StringFilter();
        }
        return locality;
    }

    public void setLocality(StringFilter locality) {
        this.locality = locality;
    }

    public StringFilter getCountry() {
        return country;
    }

    public StringFilter country() {
        if (country == null) {
            country = new StringFilter();
        }
        return country;
    }

    public void setCountry(StringFilter country) {
        this.country = country;
    }

    public StringFilter getLocation() {
        return location;
    }

    public StringFilter location() {
        if (location == null) {
            location = new StringFilter();
        }
        return location;
    }

    public void setLocation(StringFilter location) {
        this.location = location;
    }

    public ZonedDateTimeFilter getCreatedAt() {
        return createdAt;
    }

    public ZonedDateTimeFilter createdAt() {
        if (createdAt == null) {
            createdAt = new ZonedDateTimeFilter();
        }
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTimeFilter createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTimeFilter getUpdatedAt() {
        return updatedAt;
    }

    public ZonedDateTimeFilter updatedAt() {
        if (updatedAt == null) {
            updatedAt = new ZonedDateTimeFilter();
        }
        return updatedAt;
    }

    public void setUpdatedAt(ZonedDateTimeFilter updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LongFilter getMachineId() {
        return machineId;
    }

    public LongFilter machineId() {
        if (machineId == null) {
            machineId = new LongFilter();
        }
        return machineId;
    }

    public void setMachineId(LongFilter machineId) {
        this.machineId = machineId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final OrganizationCriteria that = (OrganizationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(reference, that.reference) &&
            Objects.equals(name, that.name) &&
            Objects.equals(legalName, that.legalName) &&
            Objects.equals(description, that.description) &&
            Objects.equals(taxId, that.taxId) &&
            Objects.equals(email, that.email) &&
            Objects.equals(telephone, that.telephone) &&
            Objects.equals(url, that.url) &&
            Objects.equals(address, that.address) &&
            Objects.equals(postalCode, that.postalCode) &&
            Objects.equals(region, that.region) &&
            Objects.equals(locality, that.locality) &&
            Objects.equals(country, that.country) &&
            Objects.equals(location, that.location) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(machineId, that.machineId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            reference,
            name,
            legalName,
            description,
            taxId,
            email,
            telephone,
            url,
            address,
            postalCode,
            region,
            locality,
            country,
            location,
            createdAt,
            updatedAt,
            machineId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrganizationCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (reference != null ? "reference=" + reference + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (legalName != null ? "legalName=" + legalName + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (taxId != null ? "taxId=" + taxId + ", " : "") +
            (email != null ? "email=" + email + ", " : "") +
            (telephone != null ? "telephone=" + telephone + ", " : "") +
            (url != null ? "url=" + url + ", " : "") +
            (address != null ? "address=" + address + ", " : "") +
            (postalCode != null ? "postalCode=" + postalCode + ", " : "") +
            (region != null ? "region=" + region + ", " : "") +
            (locality != null ? "locality=" + locality + ", " : "") +
            (country != null ? "country=" + country + ", " : "") +
            (location != null ? "location=" + location + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (updatedAt != null ? "updatedAt=" + updatedAt + ", " : "") +
            (machineId != null ? "machineId=" + machineId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
