package com.sothis.tech.service.dto;

import com.sothis.tech.domain.enumeration.AttachedType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.sothis.tech.domain.MachineDocumentation} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MachineDocumentationDTO implements Serializable {

    private Long id;

    @NotNull
    private String reference;

    @NotNull
    private String name;

    private AttachedType type;

    private String description;

    private String url;

    private MachineDTO machine;

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

    public AttachedType getType() {
        return type;
    }

    public void setType(AttachedType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public MachineDTO getMachine() {
        return machine;
    }

    public void setMachine(MachineDTO machine) {
        this.machine = machine;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MachineDocumentationDTO)) {
            return false;
        }

        MachineDocumentationDTO machineDocumentationDTO = (MachineDocumentationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, machineDocumentationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MachineDocumentationDTO{" +
            "id=" + getId() +
            ", reference='" + getReference() + "'" +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            ", description='" + getDescription() + "'" +
            ", url='" + getUrl() + "'" +
            ", machine=" + getMachine() +
            "}";
    }
}
