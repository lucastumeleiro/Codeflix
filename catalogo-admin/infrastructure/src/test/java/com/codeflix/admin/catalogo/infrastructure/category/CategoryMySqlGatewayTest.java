package com.codeflix.admin.catalogo.infrastructure.category;

import com.codeflix.admin.catalogo.domain.category.Category;
import com.codeflix.admin.catalogo.infrastructure.MySqlGatewayTest;
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
}
