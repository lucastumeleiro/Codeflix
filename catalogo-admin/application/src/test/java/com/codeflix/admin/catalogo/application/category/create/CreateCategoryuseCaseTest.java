package com.codeflix.admin.catalogo.application.category.create;

import com.codeflix.admin.catalogo.domain.category.CategoryGateway;
import com.codeflix.admin.catalogo.domain.exceptions.DomainException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;

import static org.mockito.AdditionalAnswers.returnsFirstArg;

@ExtendWith(MockitoExtension.class)
public class CreateCategoryuseCaseTest {

    @InjectMocks
    private DefaultCreateCategoryUseCase useCase;
    @Mock
    private CategoryGateway gateway;


    @Test
    public void givenValidCommand_whenCallsCreateCategory_thenShouldReturnCategoryId() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var command = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        Mockito.when(gateway.create(Mockito.any()))
                .thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(command);

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Mockito.verify(gateway, Mockito.times(1))
                .create(Mockito.argThat(category ->
                        Objects.equals(expectedName, category.getName())
                                && Objects.equals(expectedDescription, category.getDescription())
                                && Objects.equals(expectedIsActive, category.isActive())
                                && Objects.nonNull(category.getId())
                                && Objects.nonNull(category.getCreatedAt())
                                && Objects.nonNull(category.getUpdatedAt())
                                && Objects.isNull(category.getDeletedAt())
                ));
    }

    @Test
    public void givenValidCommandWithInactiveCategory_whenCallsCreateCategory_thenShouldReturnInactiveCategoryId() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;

        final var command = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        Mockito.when(gateway.create(Mockito.any()))
                .thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(command);

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Mockito.verify(gateway, Mockito.times(1))
                .create(Mockito.argThat(category ->
                        Objects.equals(expectedName, category.getName())
                                && Objects.equals(expectedDescription, category.getDescription())
                                && Objects.equals(expectedIsActive, category.isActive())
                                && Objects.nonNull(category.getId())
                                && Objects.nonNull(category.getCreatedAt())
                                && Objects.nonNull(category.getUpdatedAt())
                                && Objects.nonNull(category.getDeletedAt())
                ));
    }

    @Test
    public void givenInvalidName_whenCallsCreateCategory_thenShouldReturnDomainException() {
        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be null";

        final var command = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        final var actualException = Assertions.assertThrows(DomainException.class, () -> useCase.execute(command));

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        Mockito.verify(gateway, Mockito.times(0)).create(Mockito.any());
    }

    @Test
    public void givenValidCommand_whenGatewayThrowsRandomException_thenShouldReturnException() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "Gateway error";

        final var command = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        Mockito.when(gateway.create(Mockito.any()))
                .thenThrow(new IllegalStateException(expectedErrorMessage));

        final var actualException = Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(command));

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        Mockito.verify(gateway, Mockito.times(1))
                .create(Mockito.argThat(category ->
                        Objects.equals(expectedName, category.getName())
                                && Objects.equals(expectedDescription, category.getDescription())
                                && Objects.equals(expectedIsActive, category.isActive())
                                && Objects.nonNull(category.getId())
                                && Objects.nonNull(category.getCreatedAt())
                                && Objects.nonNull(category.getUpdatedAt())
                                && Objects.isNull(category.getDeletedAt())
                ));
    }

}
