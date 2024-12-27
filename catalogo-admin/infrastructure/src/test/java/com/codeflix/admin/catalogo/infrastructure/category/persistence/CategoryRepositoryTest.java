package com.codeflix.admin.catalogo.infrastructure.category.persistence;

import com.codeflix.admin.catalogo.domain.category.Category;
import com.codeflix.admin.catalogo.infrastructure.MySqlGatewayTest;
import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

@MySqlGatewayTest
public class CategoryRepositoryTest {
    @Autowired
    private CategoryRepository repository;

    @Test
    public void givenInvalidNullName_whenCallsSave_shouldReturnError() {
        final var expectedPropertyName = "name";
        final var expectedMessage = "not-null property references a null or transient value: com.codeflix.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity.name";
        final var category = Category.newCategory("Filmes", "A categoria mais assistida", true);
        final var entity = CategoryJpaEntity.from(category);
        entity.setName(null);

        final var actualException = Assertions.assertThrows(DataIntegrityViolationException.class, () -> repository.save(entity));

        final var actualCause = Assertions.assertInstanceOf(PropertyValueException.class, actualException.getCause());

        Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
        Assertions.assertEquals(expectedMessage, actualCause.getMessage());
    }

    @Test
    public void givenInvalidNullCreatedAt_whenCallsSave_shouldReturnError() {
        final var expectedPropertyCreatedAt = "createdAt";
        final var expectedMessage = "not-null property references a null or transient value: com.codeflix.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity.createdAt";
        final var category = Category.newCategory("Filmes", "A categoria mais assistida", true);
        final var entity = CategoryJpaEntity.from(category);
        entity.setCreatedAt(null);

        final var actualException = Assertions.assertThrows(DataIntegrityViolationException.class, () -> repository.save(entity));

        final var actualCause = Assertions.assertInstanceOf(PropertyValueException.class, actualException.getCause());

        Assertions.assertEquals(expectedPropertyCreatedAt, actualCause.getPropertyName());
        Assertions.assertEquals(expectedMessage, actualCause.getMessage());
    }

    @Test
    public void givenInvalidNullUpdatedAt_whenCallsSave_shouldReturnError() {
        final var expectedPropertyUpdatedAt = "updatedAt";
        final var expectedMessage = "not-null property references a null or transient value: com.codeflix.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity.updatedAt";
        final var category = Category.newCategory("Filmes", "A categoria mais assistida", true);
        final var entity = CategoryJpaEntity.from(category);
        entity.setUpdatedAt(null);

        final var actualException = Assertions.assertThrows(DataIntegrityViolationException.class, () -> repository.save(entity));

        final var actualCause = Assertions.assertInstanceOf(PropertyValueException.class, actualException.getCause());

        Assertions.assertEquals(expectedPropertyUpdatedAt, actualCause.getPropertyName());
        Assertions.assertEquals(expectedMessage, actualCause.getMessage());
    }
}
