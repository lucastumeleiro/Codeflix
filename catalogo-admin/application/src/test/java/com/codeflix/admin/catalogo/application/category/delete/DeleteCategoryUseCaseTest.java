package com.codeflix.admin.catalogo.application.category.delete;

import com.codeflix.admin.catalogo.application.UseCaseTest;
import com.codeflix.admin.catalogo.domain.category.Category;
import com.codeflix.admin.catalogo.domain.category.CategoryGateway;
import com.codeflix.admin.catalogo.domain.category.CategoryID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

public class DeleteCategoryUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultDeleteCategoryUseCase useCase;
    @Mock
    private CategoryGateway gateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(gateway);
    }

    @Test
    public void givenValidId_whenCallsDeleteCategory_shouldBeSuccess() {
        final var category = Category.newCategory("Filmes", "A categoria mais assistida", true);
        final var expectedId = category.getId();

        Mockito.doNothing()
                .when(gateway).deleteById(Mockito.eq(expectedId));

        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        Mockito.verify(gateway, Mockito.times(1)).deleteById(Mockito.eq(expectedId));
    }

    @Test
    public void givenInvalidId_whenCallsDeleteCategory_shouldBeNothing() {
        final var expectedId = CategoryID.from("123");

        Mockito.doNothing().when(gateway).deleteById(Mockito.eq(expectedId));

        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        Mockito.verify(gateway, Mockito.times(1)).deleteById(Mockito.eq(expectedId));
    }

    @Test
    public void givenValidId_whenGatewayThrowsException_shouldReturnException() {
        final var category = Category.newCategory("Filmes", "A categoria mais assistida", true);
        final var expectedId = category.getId();

        Mockito.doThrow(new IllegalStateException("Gateway error"))
                .when(gateway).deleteById(Mockito.eq(expectedId));

        Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));

        Mockito.verify(gateway, Mockito.times(1))
                .deleteById(Mockito.eq(expectedId));
    }
}
