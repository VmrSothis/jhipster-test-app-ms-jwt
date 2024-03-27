package com.sothis.tech.service.criteria;

import com.sothis.tech.domain.enumeration.AttachedType;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.sothis.tech.domain.MachineDocumentation} entity. This class is used
 * in {@link com.sothis.tech.web.rest.MachineDocumentationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /machine-documentations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MachineDocumentationCriteria implements Serializable, Criteria {

    /**
     * Class for filtering AttachedType
     */
    public static class AttachedTypeFilter extends Filter<AttachedType> {

        public AttachedTypeFilter() {}

        public AttachedTypeFilter(AttachedTypeFilter filter) {
            super(filter);
        }

        @Override
        public AttachedTypeFilter copy() {
            return new AttachedTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter reference;

    private StringFilter name;

    private AttachedTypeFilter type;

    private StringFilter description;

    private StringFilter url;

    private LongFilter machineId;

    private Boolean distinct;

    public MachineDocumentationCriteria() {}

    public MachineDocumentationCriteria(MachineDocumentationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.reference = other.reference == null ? null : other.reference.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.url = other.url == null ? null : other.url.copy();
        this.machineId = other.machineId == null ? null : other.machineId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public MachineDocumentationCriteria copy() {
        return new MachineDocumentationCriteria(this);
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

    public AttachedTypeFilter getType() {
        return type;
    }

    public AttachedTypeFilter type() {
        if (type == null) {
            type = new AttachedTypeFilter();
        }
        return type;
    }

    public void setType(AttachedTypeFilter type) {
        this.type = type;
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
        final MachineDocumentationCriteria that = (MachineDocumentationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(reference, that.reference) &&
            Objects.equals(name, that.name) &&
            Objects.equals(type, that.type) &&
            Objects.equals(description, that.description) &&
            Objects.equals(url, that.url) &&
            Objects.equals(machineId, that.machineId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, reference, name, type, description, url, machineId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MachineDocumentationCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (reference != null ? "reference=" + reference + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (type != null ? "type=" + type + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (url != null ? "url=" + url + ", " : "") +
            (machineId != null ? "machineId=" + machineId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
