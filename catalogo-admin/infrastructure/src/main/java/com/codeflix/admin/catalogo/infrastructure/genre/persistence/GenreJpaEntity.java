package com.codeflix.admin.catalogo.infrastructure.genre.persistence;

import com.codeflix.admin.catalogo.domain.category.CategoryID;
import com.codeflix.admin.catalogo.domain.genre.Genre;
import com.codeflix.admin.catalogo.domain.genre.GenreID;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "Genre")
@Table(name = "genres")
public class GenreJpaEntity {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "active", nullable = false)
    private boolean active;

    @OneToMany(mappedBy = "genre", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<GenreCategoryJpaEntity> categories;

    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant updatedAt;

    @Column(name = "deleted_at", columnDefinition = "DATETIME(6)")
    private Instant deletedAt;

    public GenreJpaEntity() {
    }

    private GenreJpaEntity(
            final String id,
            final String name,
            final boolean isActive,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt
    ) {
        this.id = id;
        this.name = name;
        this.active = isActive;
        this.categories = new HashSet<>();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public static GenreJpaEntity from(final Genre genre) {
        final var anEntity = new GenreJpaEntity(
                genre.getId().getValue(),
                genre.getName(),
                genre.isActive(),
                genre.getCreatedAt(),
                genre.getUpdatedAt(),
                genre.getDeletedAt()
        );

        genre.getCategories()
                .forEach(anEntity::addCategory);

        return anEntity;
    }

    public Genre toAggregate() {
        return Genre.with(
                GenreID.from(getId()),
                getName(),
                isActive(),
                getCategoryIDs(),
                getCreatedAt(),
                getUpdatedAt(),
                getDeletedAt()
        );
    }

    private void addCategory(final CategoryID id) {
        this.categories.add(GenreCategoryJpaEntity.from(this, id));
    }

    private void removeCategory(final CategoryID id) {
        this.categories.remove(GenreCategoryJpaEntity.from(this, id));
    }

    public String getId() {
        return id;
    }

    public GenreJpaEntity setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public GenreJpaEntity setName(String name) {
        this.name = name;
        return this;
    }

    public boolean isActive() {
        return active;
    }

    public GenreJpaEntity setActive(boolean active) {
        this.active = active;
        return this;
    }

    public List<CategoryID> getCategoryIDs() {
        return getCategories().stream()
                .map(it -> CategoryID.from(it.getId().getCategoryId()))
                .toList();
    }

    public Set<GenreCategoryJpaEntity> getCategories() {
        return categories;
    }

    public GenreJpaEntity setCategories(Set<GenreCategoryJpaEntity> categories) {
        this.categories = categories;
        return this;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public GenreJpaEntity setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public GenreJpaEntity setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public GenreJpaEntity setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }
}
