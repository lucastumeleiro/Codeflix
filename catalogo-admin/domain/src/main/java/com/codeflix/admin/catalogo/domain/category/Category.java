package com.codeflix.admin.catalogo.domain.category;

import com.codeflix.admin.catalogo.domain.AggregateRoot;
import com.codeflix.admin.catalogo.domain.Utils.InstantUtils;
import com.codeflix.admin.catalogo.domain.validation.ValidationHandler;

import java.time.Instant;
import java.util.Objects;

public class Category extends AggregateRoot<CategoryID> implements Cloneable {
    private String name;
    private String description;
    private Boolean active;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    private Category(
            final CategoryID id,
            final String name,
            final String description,
            final Boolean active,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt
    ) {
        super(id);
        this.name = name;
        this.description = description;
        this.active = active;
        this.createdAt = Objects.requireNonNull(createdAt, "'createdAt' should not be null");
        this.updatedAt = Objects.requireNonNull(updatedAt, "'updatedAt' should not be null");
        this.deletedAt = deletedAt;
    }

    public static Category newCategory(final String name, final String description, final Boolean isActive) {
        final var id = CategoryID.unique();
        final var now = InstantUtils.now();
        final var deletedAt = isActive ? null : InstantUtils.now();

        return new Category(id, name, description, isActive, now, now, deletedAt);
    }

    public static Category with(
            final CategoryID id,
            final String name,
            final String description,
            final boolean active,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt
    ) {
        return new Category(
                id,
                name,
                description,
                active,
                createdAt,
                updatedAt,
                deletedAt
        );
    }

    public static Category clone(final Category category) {
        return category.clone();
    }

    public static Category with(final Category category) {
        return with(
                category.getId(),
                category.name,
                category.description,
                category.isActive(),
                category.createdAt,
                category.updatedAt,
                category.deletedAt
        );
    }

    @Override
    public void validate(final ValidationHandler handler) {
        new CategoryValidator(this, handler).validate();
    }

    public Category activate() {
        this.deletedAt = null;
        this.active = true;
        this.updatedAt = InstantUtils.now();

        return this;
    }

    public Category deactivate() {
        if (getDeletedAt() == null) {
            this.deletedAt = InstantUtils.now();
        }

        this.active = false;
        this.updatedAt = InstantUtils.now();

        return this;
    }

    public Category update(final String name, final String description, final Boolean isActive) {
        if (isActive) {
            activate();
        } else {
            deactivate();
        }
        this.name = name;
        this.description = description;
        this.updatedAt = InstantUtils.now();

        return this;
    }

    public CategoryID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Boolean isActive() {
        return active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    @Override
    public Category clone() {
        try {
            return (Category) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
