package com.sothis.tech.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.sothis.tech.domain.Machine} entity. This class is used
 * in {@link com.sothis.tech.web.rest.MachineResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /machines?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MachineCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter reference;

    private StringFilter name;

    private StringFilter description;

    private StringFilter firmwareVersion;

    private StringFilter hardwareVersion;

    private StringFilter softwareVersion;

    private StringFilter serialNumber;

    private StringFilter supportedProtocol;

    private ZonedDateTimeFilter createdAt;

    private ZonedDateTimeFilter updatedAt;

    private LongFilter plantAreaId;

    private LongFilter machineModelId;

    private LongFilter organizationId;

    private Boolean distinct;

    public MachineCriteria() {}

    public MachineCriteria(MachineCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.reference = other.reference == null ? null : other.reference.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.firmwareVersion = other.firmwareVersion == null ? null : other.firmwareVersion.copy();
        this.hardwareVersion = other.hardwareVersion == null ? null : other.hardwareVersion.copy();
        this.softwareVersion = other.softwareVersion == null ? null : other.softwareVersion.copy();
        this.serialNumber = other.serialNumber == null ? null : other.serialNumber.copy();
        this.supportedProtocol = other.supportedProtocol == null ? null : other.supportedProtocol.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.plantAreaId = other.plantAreaId == null ? null : other.plantAreaId.copy();
        this.machineModelId = other.machineModelId == null ? null : other.machineModelId.copy();
        this.organizationId = other.organizationId == null ? null : other.organizationId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public MachineCriteria copy() {
        return new MachineCriteria(this);
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

    public StringFilter getFirmwareVersion() {
        return firmwareVersion;
    }

    public StringFilter firmwareVersion() {
        if (firmwareVersion == null) {
            firmwareVersion = new StringFilter();
        }
        return firmwareVersion;
    }

    public void setFirmwareVersion(StringFilter firmwareVersion) {
        this.firmwareVersion = firmwareVersion;
    }

    public StringFilter getHardwareVersion() {
        return hardwareVersion;
    }

    public StringFilter hardwareVersion() {
        if (hardwareVersion == null) {
            hardwareVersion = new StringFilter();
        }
        return hardwareVersion;
    }

    public void setHardwareVersion(StringFilter hardwareVersion) {
        this.hardwareVersion = hardwareVersion;
    }

    public StringFilter getSoftwareVersion() {
        return softwareVersion;
    }

    public StringFilter softwareVersion() {
        if (softwareVersion == null) {
            softwareVersion = new StringFilter();
        }
        return softwareVersion;
    }

    public void setSoftwareVersion(StringFilter softwareVersion) {
        this.softwareVersion = softwareVersion;
    }

    public StringFilter getSerialNumber() {
        return serialNumber;
    }

    public StringFilter serialNumber() {
        if (serialNumber == null) {
            serialNumber = new StringFilter();
        }
        return serialNumber;
    }

    public void setSerialNumber(StringFilter serialNumber) {
        this.serialNumber = serialNumber;
    }

    public StringFilter getSupportedProtocol() {
        return supportedProtocol;
    }

    public StringFilter supportedProtocol() {
        if (supportedProtocol == null) {
            supportedProtocol = new StringFilter();
        }
        return supportedProtocol;
    }

    public void setSupportedProtocol(StringFilter supportedProtocol) {
        this.supportedProtocol = supportedProtocol;
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

    public LongFilter getPlantAreaId() {
        return plantAreaId;
    }

    public LongFilter plantAreaId() {
        if (plantAreaId == null) {
            plantAreaId = new LongFilter();
        }
        return plantAreaId;
    }

    public void setPlantAreaId(LongFilter plantAreaId) {
        this.plantAreaId = plantAreaId;
    }

    public LongFilter getMachineModelId() {
        return machineModelId;
    }

    public LongFilter machineModelId() {
        if (machineModelId == null) {
            machineModelId = new LongFilter();
        }
        return machineModelId;
    }

    public void setMachineModelId(LongFilter machineModelId) {
        this.machineModelId = machineModelId;
    }

    public LongFilter getOrganizationId() {
        return organizationId;
    }

    public LongFilter organizationId() {
        if (organizationId == null) {
            organizationId = new LongFilter();
        }
        return organizationId;
    }

    public void setOrganizationId(LongFilter organizationId) {
        this.organizationId = organizationId;
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
        final MachineCriteria that = (MachineCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(reference, that.reference) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(firmwareVersion, that.firmwareVersion) &&
            Objects.equals(hardwareVersion, that.hardwareVersion) &&
            Objects.equals(softwareVersion, that.softwareVersion) &&
            Objects.equals(serialNumber, that.serialNumber) &&
            Objects.equals(supportedProtocol, that.supportedProtocol) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(plantAreaId, that.plantAreaId) &&
            Objects.equals(machineModelId, that.machineModelId) &&
            Objects.equals(organizationId, that.organizationId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            reference,
            name,
            description,
            firmwareVersion,
            hardwareVersion,
            softwareVersion,
            serialNumber,
            supportedProtocol,
            createdAt,
            updatedAt,
            plantAreaId,
            machineModelId,
            organizationId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MachineCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (reference != null ? "reference=" + reference + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (firmwareVersion != null ? "firmwareVersion=" + firmwareVersion + ", " : "") +
            (hardwareVersion != null ? "hardwareVersion=" + hardwareVersion + ", " : "") +
            (softwareVersion != null ? "softwareVersion=" + softwareVersion + ", " : "") +
            (serialNumber != null ? "serialNumber=" + serialNumber + ", " : "") +
            (supportedProtocol != null ? "supportedProtocol=" + supportedProtocol + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (updatedAt != null ? "updatedAt=" + updatedAt + ", " : "") +
            (plantAreaId != null ? "plantAreaId=" + plantAreaId + ", " : "") +
            (machineModelId != null ? "machineModelId=" + machineModelId + ", " : "") +
            (organizationId != null ? "organizationId=" + organizationId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
