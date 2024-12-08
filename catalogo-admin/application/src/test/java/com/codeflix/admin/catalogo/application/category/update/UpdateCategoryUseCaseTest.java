package com.codeflix.admin.catalogo.application.category.update;

import com.codeflix.admin.catalogo.domain.category.Category;
import com.codeflix.admin.catalogo.domain.category.CategoryGateway;
import com.codeflix.admin.catalogo.domain.category.CategoryID;
import com.codeflix.admin.catalogo.domain.exceptions.DomainException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.argThat;

@ExtendWith(MockitoExtension.class)
public class UpdateCategoryUseCaseTest {

    @InjectMocks
    private DefaultUpdateCategoryUseCase useCase;
    @Mock
    private CategoryGateway gateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(gateway);
    }

    @Test
    public void givenValidCommand_whenCallsUpdateCategory_thenShouldReturnCategoryId() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var category = Category.newCategory("Film", null, true);
        final var expectedId = category.getId();

        final var command = UpdateCategoryCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        Mockito.when(gateway.findById(Mockito.eq(expectedId)))
                .thenReturn(Optional.of(Category.clone(category)));

        Mockito.when(gateway.update(Mockito.any()))
                .thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(command).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Mockito.verify(gateway, Mockito.times(1)).findById(expectedId);
        Mockito.verify(gateway, Mockito.times(1)).update(argThat(
                updatedCategory ->
                        Objects.equals(expectedName, updatedCategory.getName())
                                && Objects.equals(expectedDescription, updatedCategory.getDescription())
                                && Objects.equals(expectedIsActive, updatedCategory.isActive())
                                && Objects.equals(expectedId, updatedCategory.getId())
                                && Objects.equals(category.getCreatedAt(), updatedCategory.getCreatedAt())
                                && category.getUpdatedAt().isBefore(updatedCategory.getUpdatedAt())
                                && Objects.isNull(updatedCategory.getDeletedAt())
        ));
    }

    @Test
    public void givenValidCommandWithInactiveCategory_whenCallsUpdateCategory_thenShouldReturnInactiveCategoryId() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;

        final var category = Category.newCategory("Film", null, true);
        final var expectedId = category.getId();

        final var command = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);

        Mockito.when(gateway.findById(Mockito.eq(expectedId)))
                .thenReturn(Optional.of(Category.clone(category)));

        Mockito.when(gateway.update(Mockito.any()))
                .thenAnswer(returnsFirstArg());

        Assertions.assertTrue(category.isActive());
        Assertions.assertNull(category.getDeletedAt());

        final var actualOutput = useCase.execute(command).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Mockito.verify(gateway, Mockito.times(1)).findById(Mockito.eq(expectedId));

        Mockito.verify(gateway, Mockito.times(1))
                .update(Mockito.argThat(updatedCategory ->
                        Objects.equals(expectedName, updatedCategory.getName())
                                && Objects.equals(expectedId, updatedCategory.getId())
                                && Objects.equals(expectedDescription, updatedCategory.getDescription())
                                && Objects.equals(expectedIsActive, updatedCategory.isActive())
                                && Objects.equals(category.getCreatedAt(), updatedCategory.getCreatedAt())
                                && category.getUpdatedAt().isBefore(updatedCategory.getUpdatedAt())
                                && Objects.nonNull(updatedCategory.getDeletedAt())
                ));
    }

    @Test
    public void givenInvalidName_whenCallsUpdateCategory_thenShouldReturnDomainException() {
        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var category = Category.newCategory("Film", null, true);
        final var expectedId = category.getId();

        final var command = UpdateCategoryCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        Mockito.when(gateway.findById(Mockito.eq(expectedId)))
                .thenReturn(Optional.of(Category.clone(category)));

        final var notification = useCase.execute(command).getLeft();

        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());

        Mockito.verify(gateway, Mockito.times(0)).update(Mockito.any());
    }

    @Test
    public void givenValidCommand_whenGatewayThrowsRandomException_thenShouldReturnException() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "Gateway error";
        final var expectedErrorCount = 1;

        final var category = Category.newCategory("Film", null, true);
        final var expectedId = category.getId();

        final var command = UpdateCategoryCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        Mockito.when(gateway.findById(Mockito.eq(expectedId)))
                .thenReturn(Optional.of(Category.clone(category)));

        Mockito.when(gateway.update(Mockito.any()))
                .thenThrow(new IllegalStateException(expectedErrorMessage));

        final var notification = useCase.execute(command).getLeft();

        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());

        Mockito.verify(gateway, Mockito.times(1))
                .update(Mockito.argThat(updatedCategory ->
                        Objects.equals(expectedName, updatedCategory.getName())
                                && Objects.equals(expectedId, updatedCategory.getId())
                                && Objects.equals(expectedDescription, updatedCategory.getDescription())
                                && Objects.equals(expectedIsActive, updatedCategory.isActive())
                                && Objects.equals(category.getCreatedAt(), updatedCategory.getCreatedAt())
                                && category.getUpdatedAt().isBefore(updatedCategory.getUpdatedAt())
                                && Objects.isNull(updatedCategory.getDeletedAt())
                ));
    }

    @Test
    public void givenCommandWithInvalidID_whenCallsUpdateCategory_shouldReturnNotFoundException() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;
        final var expectedErrorMessage = "Category with ID 123 was not found";

        final var expectedId = "123";

        final var aCommand = UpdateCategoryCommand.with(
                expectedId,
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        Mockito.when(gateway.findById(Mockito.eq(CategoryID.from(expectedId))))
                .thenReturn(Optional.empty());

        final var actualException = Assertions.assertThrows(DomainException.class, () -> useCase.execute(aCommand));

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        Mockito.verify(gateway, Mockito.times(1)).findById(Mockito.eq(CategoryID.from(expectedId)));

        Mockito.verify(gateway, Mockito.times(0)).update(Mockito.any());
    }
}
