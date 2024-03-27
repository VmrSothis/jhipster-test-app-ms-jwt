package com.sothis.tech.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sothis.tech.domain.enumeration.AttachedType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A MachineDocumentation.
 */
@Entity
@Table(name = "machine_documentation")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MachineDocumentation implements Serializable {

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

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private AttachedType type;

    @Column(name = "description")
    private String description;

    @Column(name = "url")
    private String url;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "plantArea", "machineModel", "organization" }, allowSetters = true)
    private Machine machine;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MachineDocumentation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReference() {
        return this.reference;
    }

    public MachineDocumentation reference(String reference) {
        this.setReference(reference);
        return this;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getName() {
        return this.name;
    }

    public MachineDocumentation name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AttachedType getType() {
        return this.type;
    }

    public MachineDocumentation type(AttachedType type) {
        this.setType(type);
        return this;
    }

    public void setType(AttachedType type) {
        this.type = type;
    }

    public String getDescription() {
        return this.description;
    }

    public MachineDocumentation description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return this.url;
    }

    public MachineDocumentation url(String url) {
        this.setUrl(url);
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Machine getMachine() {
        return this.machine;
    }

    public void setMachine(Machine machine) {
        this.machine = machine;
    }

    public MachineDocumentation machine(Machine machine) {
        this.setMachine(machine);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MachineDocumentation)) {
            return false;
        }
        return getId() != null && getId().equals(((MachineDocumentation) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MachineDocumentation{" +
            "id=" + getId() +
            ", reference='" + getReference() + "'" +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            ", description='" + getDescription() + "'" +
            ", url='" + getUrl() + "'" +
            "}";
    }
}
