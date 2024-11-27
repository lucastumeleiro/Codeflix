package com.codeflix.admin.catalogo.domain.category;

import com.codeflix.admin.catalogo.domain.exceptions.DomainException;
import com.codeflix.admin.catalogo.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CategoryTest {

    @Test
    public void givenValidParams_whenCallNewCategory_thenInstantiateACategory() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var actualCategory =
                Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertNotNull(actualCategory);
        Assertions.assertNotNull(actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenInvalidNullName_whenCallNewCategoryAndValidate_thenShouldReciveError() {
        final String expectedName = null;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var actualCategory =
                Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var error = Assertions.assertThrows(
                DomainException.class,
                () -> actualCategory.validate(new ThrowsValidationHandler())
        );

        Assertions.assertEquals(expectedErrorMessage, error.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, error.getErrors().size());
    }

    @Test
    public void givenInvalidEmptyName_whenCallNewCategoryAndValidate_thenShouldReciveError() {
        final String expectedName = " ";
        final var expectedErrorMessage = "'name' should not be empty";
        final var expectedErrorCount = 1;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var actualCategory =
                Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var error = Assertions.assertThrows(
                DomainException.class,
                () -> actualCategory.validate(new ThrowsValidationHandler())
        );

        Assertions.assertEquals(expectedErrorMessage, error.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, error.getErrors().size());
    }

    @Test
    public void givenInvalidNameLengthLessThan3_whenCallNewCategoryAndValidate_thenShouldReciveError() {
        final String expectedName = "Fi ";
        final var expectedErrorMessage = "'name' must be between 3 and 255 characters";
        final var expectedErrorCount = 1;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var actualCategory =
                Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var error = Assertions.assertThrows(
                DomainException.class,
                () -> actualCategory.validate(new ThrowsValidationHandler())
        );

        Assertions.assertEquals(expectedErrorMessage, error.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, error.getErrors().size());
    }

    @Test
    public void givenInvalidNameLengthMoreThan3_whenCallNewCategoryAndValidate_thenShouldReciveError() {
        final String expectedName = """
                lorem ipsum dolor sit amet consectetur adipiscing elit lacus cursus montes dictum duis class est
                ullamcorper placerat senectus vivamus diam auctor magnis condimentum turpis egestas massa maecenas
                luctus in ultricies nisi taciti ridiculus facilisis litora curabitur
                """;
        final var expectedErrorMessage = "'name' must be between 3 and 255 characters";
        final var expectedErrorCount = 1;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var actualCategory =
                Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var error = Assertions.assertThrows(
                DomainException.class,
                () -> actualCategory.validate(new ThrowsValidationHandler())
        );

        Assertions.assertEquals(expectedErrorMessage, error.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, error.getErrors().size());
    }

    @Test
    public void givenValidEmptyDescription_whenCallNewCategory_thenShouldReciveError() {
        final var expectedName = "Filmes";
        final var expectedDescription = " ";
        final var expectedIsActive = true;

        final var actualCategory =
                Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertDoesNotThrow(() -> actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertNotNull(actualCategory);
        Assertions.assertNotNull(actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenValidFalseIsActive_whenCallNewCategory_thenShouldReciveError() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;

        final var actualCategory =
                Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertDoesNotThrow(() -> actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertNotNull(actualCategory);
        Assertions.assertNotNull(actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNotNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenValidActiveCategory_whenCallDeactivate_thenReturnCategoryInactivated() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;

        final var category =
                Category.newCategory(expectedName, expectedDescription, true);

        Assertions.assertDoesNotThrow(() -> category.validate(new ThrowsValidationHandler()));

        final var updatedAt = category.getUpdatedAt();
        final var createdAt = category.getCreatedAt();

        Assertions.assertTrue(category.isActive());
        Assertions.assertNull(category.getDeletedAt());

        final var actualCategory = category.deactivate();

        Assertions.assertDoesNotThrow(() -> actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(category.getId(), actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertEquals(createdAt, actualCategory.getCreatedAt());
        Assertions.assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNotNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenValidInactiveCategory_whenCallActivate_thenReturnCategoryActivated() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var category =
                Category.newCategory(expectedName, expectedDescription, false);

        Assertions.assertDoesNotThrow(() -> category.validate(new ThrowsValidationHandler()));

        final var createdAt = category.getCreatedAt();
        final var updatedAt = category.getUpdatedAt();

        Assertions.assertFalse(category.isActive());
        Assertions.assertNotNull(category.getDeletedAt());

        final var actualCategory = category.activate();

        Assertions.assertDoesNotThrow(() -> actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(category.getId(), actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertEquals(createdAt, actualCategory.getCreatedAt());
        Assertions.assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenValidCategory_whenCallUpdate_thenReturnCategoryUpdated() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var category =
                Category.newCategory("Filme", "A categoria", expectedIsActive);

        Assertions.assertDoesNotThrow(() -> category.validate(new ThrowsValidationHandler()));

        final var createdAt = category.getCreatedAt();
        final var updatedAt = category.getUpdatedAt();

        final var actualCategory = category.update(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertDoesNotThrow(() -> actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(category.getId(), actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertEquals(createdAt, actualCategory.getCreatedAt());
        Assertions.assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenValidCategory_whenCallUpdateToInactive_thenReturnCategoryUpdated() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;

        final var category =
                Category.newCategory("Filme", "A categoria", true);

        Assertions.assertDoesNotThrow(() -> category.validate(new ThrowsValidationHandler()));
        Assertions.assertTrue(category.isActive());
        Assertions.assertNull(category.getDeletedAt());

        final var createdAt = category.getCreatedAt();
        final var updatedAt = category.getUpdatedAt();

        final var actualCategory = category.update(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertDoesNotThrow(() -> actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(category.getId(), actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertFalse(category.isActive());
        Assertions.assertNotNull(category.getDeletedAt());
        Assertions.assertEquals(createdAt, actualCategory.getCreatedAt());
        Assertions.assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));
    }

    @Test
    public void givenValidCategory_whenCallUpdateWithInvalidParams_thenReturnCategoryUpdated() {
        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var category =
                Category.newCategory("Filmes", "A categoria", expectedIsActive);

        Assertions.assertDoesNotThrow(() -> category.validate(new ThrowsValidationHandler()));

        final var createdAt = category.getCreatedAt();
        final var updatedAt = category.getUpdatedAt();

        final var actualCategory = category.update(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertEquals(category.getId(), actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertTrue(category.isActive());
        Assertions.assertEquals(createdAt, actualCategory.getCreatedAt());
        Assertions.assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNull(category.getDeletedAt());
    }
}
