package com.sothis.tech.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.sothis.tech.domain.Machine} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MachineDTO implements Serializable {

    private Long id;

    @NotNull
    private String reference;

    @NotNull
    private String name;

    private String description;

    private String firmwareVersion;

    private String hardwareVersion;

    private String softwareVersion;

    private String serialNumber;

    private String supportedProtocol;

    private ZonedDateTime createdAt;

    private ZonedDateTime updatedAt;

    private PlantAreaDTO plantArea;

    private MachineModelDTO machineModel;

    private OrganizationDTO organization;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    public void setFirmwareVersion(String firmwareVersion) {
        this.firmwareVersion = firmwareVersion;
    }

    public String getHardwareVersion() {
        return hardwareVersion;
    }

    public void setHardwareVersion(String hardwareVersion) {
        this.hardwareVersion = hardwareVersion;
    }

    public String getSoftwareVersion() {
        return softwareVersion;
    }

    public void setSoftwareVersion(String softwareVersion) {
        this.softwareVersion = softwareVersion;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getSupportedProtocol() {
        return supportedProtocol;
    }

    public void setSupportedProtocol(String supportedProtocol) {
        this.supportedProtocol = supportedProtocol;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public PlantAreaDTO getPlantArea() {
        return plantArea;
    }

    public void setPlantArea(PlantAreaDTO plantArea) {
        this.plantArea = plantArea;
    }

    public MachineModelDTO getMachineModel() {
        return machineModel;
    }

    public void setMachineModel(MachineModelDTO machineModel) {
        this.machineModel = machineModel;
    }

    public OrganizationDTO getOrganization() {
        return organization;
    }

    public void setOrganization(OrganizationDTO organization) {
        this.organization = organization;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MachineDTO)) {
            return false;
        }

        MachineDTO machineDTO = (MachineDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, machineDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MachineDTO{" +
            "id=" + getId() +
            ", reference='" + getReference() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", firmwareVersion='" + getFirmwareVersion() + "'" +
            ", hardwareVersion='" + getHardwareVersion() + "'" +
            ", softwareVersion='" + getSoftwareVersion() + "'" +
            ", serialNumber='" + getSerialNumber() + "'" +
            ", supportedProtocol='" + getSupportedProtocol() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", plantArea=" + getPlantArea() +
            ", machineModel=" + getMachineModel() +
            ", organization=" + getOrganization() +
            "}";
    }
}
