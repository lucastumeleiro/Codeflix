package com.codeflix.admin.catalogo.infrastructure.category;

import com.codeflix.admin.catalogo.domain.category.Category;
import com.codeflix.admin.catalogo.domain.category.CategoryID;
import com.codeflix.admin.catalogo.infrastructure.MySqlGatewayTest;
import com.codeflix.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import com.codeflix.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@MySqlGatewayTest
public class CategoryMySqlGatewayTest {

    @Autowired
    private CategoryMySqlGateway gateway;
    @Autowired
    private CategoryRepository repository;

    @Test
    public void givenValidCategory_whenCallsCreate_shouldReturnNewCategory() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var category = Category.newCategory(expectedName, expectedDescription, expectedIsActive);
        Assertions.assertEquals(0, repository.count());

        final var actualCategory = gateway.create(category);
        Assertions.assertEquals(1, repository.count());
        Assertions.assertEquals(category.getId(), actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertEquals(category.getCreatedAt(), actualCategory.getCreatedAt());
        Assertions.assertEquals(category.getUpdatedAt(), actualCategory.getUpdatedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());

        final var actualEntity = repository.findById(category.getId().getValue()).get();
        Assertions.assertEquals(category.getId().getValue(), actualEntity.getId());
        Assertions.assertEquals(expectedName, actualEntity.getName());
        Assertions.assertEquals(expectedDescription, actualEntity.getDescription());
        Assertions.assertEquals(expectedIsActive, actualEntity.isActive());
        Assertions.assertEquals(category.getCreatedAt(), actualEntity.getCreatedAt());
        Assertions.assertEquals(category.getUpdatedAt(), actualEntity.getUpdatedAt());
        Assertions.assertNull(actualEntity.getDeletedAt());
    }

    @Test
    public void givenValidCategory_whenCallsUpdate_shouldReturnNewCategory() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var category = Category.newCategory("Film", null, expectedIsActive);
        Assertions.assertEquals(0, repository.count());

        repository.saveAndFlush(CategoryJpaEntity.from(category));
        Assertions.assertEquals(1, repository.count());

        final var updatedCategory = category.clone().update(expectedName, expectedDescription, expectedIsActive);
        final var actualCategory = gateway.update(updatedCategory);
        Assertions.assertEquals(1, repository.count());

        Assertions.assertEquals(category.getId(), actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertEquals(category.getCreatedAt(), actualCategory.getCreatedAt());
        Assertions.assertTrue(category.getUpdatedAt().isBefore(actualCategory.getUpdatedAt()));
        Assertions.assertNull(actualCategory.getDeletedAt());

        final var actualEntity = repository.findById(category.getId().getValue()).get();
        Assertions.assertEquals(category.getId().getValue(), actualEntity.getId());
        Assertions.assertEquals(expectedName, actualEntity.getName());
        Assertions.assertEquals(expectedDescription, actualEntity.getDescription());
        Assertions.assertEquals(expectedIsActive, actualEntity.isActive());
        Assertions.assertEquals(category.getCreatedAt(), actualEntity.getCreatedAt());
        Assertions.assertTrue(category.getUpdatedAt().isBefore(actualCategory.getUpdatedAt()));
        Assertions.assertNull(actualEntity.getDeletedAt());
    }

    @Test
    public void givenPrePersistedCategoryAndValidCategoryId_whenTryToDeletedIt_shouldDeleteCategory() {
        final var category = Category.newCategory("Filmes", null, true);
        Assertions.assertEquals(0, repository.count());

        repository.saveAndFlush(CategoryJpaEntity.from(category));
        Assertions.assertEquals(1, repository.count());

        gateway.deleteById(category.getId());
        Assertions.assertEquals(0, repository.count());
    }

    @Test
    public void givenInvalidCategoryId_whenTryToDeletedIt_shouldNothing() {
        Assertions.assertEquals(0, repository.count());

        gateway.deleteById(CategoryID.from("Invalid"));

        Assertions.assertEquals(0, repository.count());
    }

    @Test
    public void givenPrePersistedCategoryAndValidCategoryId_whenCallsFindById_shouldReturnCategory() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var category = Category.newCategory(expectedName, expectedDescription, expectedIsActive);
        Assertions.assertEquals(0, repository.count());

        repository.saveAndFlush(CategoryJpaEntity.from(category));
        Assertions.assertEquals(1, repository.count());


        final var actualCategory = gateway.findById(category.getId()).get();
        Assertions.assertEquals(1, repository.count());

        Assertions.assertEquals(category.getId(), actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertEquals(category.getCreatedAt(), actualCategory.getCreatedAt());
        Assertions.assertEquals(category.getUpdatedAt(), actualCategory.getUpdatedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenValidCategoryIdButNotStored_whenCallsFindById_shouldReturnEmpty() {
        Assertions.assertEquals(0, repository.count());

        final var actualCategory = gateway.findById(CategoryID.from("Empty"));

        Assertions.assertTrue(actualCategory.isEmpty());
    }

}
