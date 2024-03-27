package com.sothis.tech.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * A Machine.
 */
@Entity
@Table(name = "machine")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Machine implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "reference", nullable = false)
    private String reference;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "firmware_version")
    private String firmwareVersion;

    @Column(name = "hardware_version")
    private String hardwareVersion;

    @Column(name = "software_version")
    private String softwareVersion;

    @Column(name = "serial_number")
    private String serialNumber;

    @Column(name = "supported_protocol")
    private String supportedProtocol;

    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "plant" }, allowSetters = true)
    private PlantArea plantArea;

    @ManyToOne(optional = false)
    @NotNull
    private MachineModel machineModel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "machines" }, allowSetters = true)
    private Organization organization;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Machine id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReference() {
        return this.reference;
    }

    public Machine reference(String reference) {
        this.setReference(reference);
        return this;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getName() {
        return this.name;
    }

    public Machine name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Machine description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFirmwareVersion() {
        return this.firmwareVersion;
    }

    public Machine firmwareVersion(String firmwareVersion) {
        this.setFirmwareVersion(firmwareVersion);
        return this;
    }

    public void setFirmwareVersion(String firmwareVersion) {
        this.firmwareVersion = firmwareVersion;
    }

    public String getHardwareVersion() {
        return this.hardwareVersion;
    }

    public Machine hardwareVersion(String hardwareVersion) {
        this.setHardwareVersion(hardwareVersion);
        return this;
    }

    public void setHardwareVersion(String hardwareVersion) {
        this.hardwareVersion = hardwareVersion;
    }

    public String getSoftwareVersion() {
        return this.softwareVersion;
    }

    public Machine softwareVersion(String softwareVersion) {
        this.setSoftwareVersion(softwareVersion);
        return this;
    }

    public void setSoftwareVersion(String softwareVersion) {
        this.softwareVersion = softwareVersion;
    }

    public String getSerialNumber() {
        return this.serialNumber;
    }

    public Machine serialNumber(String serialNumber) {
        this.setSerialNumber(serialNumber);
        return this;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getSupportedProtocol() {
        return this.supportedProtocol;
    }

    public Machine supportedProtocol(String supportedProtocol) {
        this.setSupportedProtocol(supportedProtocol);
        return this;
    }

    public void setSupportedProtocol(String supportedProtocol) {
        this.supportedProtocol = supportedProtocol;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public Machine createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public Machine updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public PlantArea getPlantArea() {
        return this.plantArea;
    }

    public void setPlantArea(PlantArea plantArea) {
        this.plantArea = plantArea;
    }

    public Machine plantArea(PlantArea plantArea) {
        this.setPlantArea(plantArea);
        return this;
    }

    public MachineModel getMachineModel() {
        return this.machineModel;
    }

    public void setMachineModel(MachineModel machineModel) {
        this.machineModel = machineModel;
    }

    public Machine machineModel(MachineModel machineModel) {
        this.setMachineModel(machineModel);
        return this;
    }

    public Organization getOrganization() {
        return this.organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public Machine organization(Organization organization) {
        this.setOrganization(organization);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Machine)) {
            return false;
        }
        return getId() != null && getId().equals(((Machine) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Machine{" +
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
            "}";
    }
}
