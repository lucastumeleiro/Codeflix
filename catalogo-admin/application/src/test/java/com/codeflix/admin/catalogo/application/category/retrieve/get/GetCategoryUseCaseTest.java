package com.codeflix.admin.catalogo.application.category.retrieve.get;

import com.codeflix.admin.catalogo.domain.category.Category;
import com.codeflix.admin.catalogo.domain.category.CategoryGateway;
import com.codeflix.admin.catalogo.domain.category.CategoryID;
import com.codeflix.admin.catalogo.domain.exceptions.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class GetCategoryUseCaseTest {

    @InjectMocks
    private DefaultGetCategoryUseCase useCase;
    @Mock
    private CategoryGateway gateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(gateway);
    }

    @Test
    public void givenValidId_whenCallsGetCategory_shouldReturnCategory() {
        final var category = Category.newCategory("Filmes", "A categoria mais assistida", true);
        final var expectedId = category.getId();


        Mockito.when(gateway.findById(Mockito.eq(expectedId)))
                .thenReturn(Optional.of(category.clone()));

        final var actualCategory = useCase.execute(expectedId.getValue());

        Assertions.assertEquals(CategoryOutput.from(category), actualCategory);
    }

    @Test
    public void givenInvalidId_whenCallsGetCategory_shouldReturnNotFound() {
        final var expectedId = CategoryID.from("123");

        final var expectedErrorMessage = "Category with ID 123 was not found";

        Mockito.when(gateway.findById(Mockito.eq(expectedId)))
                .thenReturn(Optional.empty());

        final var actualException = Assertions.assertThrows(
                NotFoundException.class,
                () -> useCase.execute(expectedId.getValue())
        );

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenValidId_whenGatewayThrowsException_shouldReturnException() {
        final var expectedId = CategoryID.from("123");

        final var expectedErrorMessage = "Gateway error";

        Mockito.when(gateway.findById(Mockito.eq(expectedId)))
                .thenThrow(new IllegalStateException(expectedErrorMessage));

        final var actualException = Assertions.assertThrows(
                IllegalStateException.class,
                () -> useCase.execute(expectedId.getValue())
        );

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }
}
