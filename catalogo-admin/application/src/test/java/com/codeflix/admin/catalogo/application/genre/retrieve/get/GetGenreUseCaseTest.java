package com.codeflix.admin.catalogo.application.genre.retrieve.get;

import com.codeflix.admin.catalogo.application.UseCaseTest;
import com.codeflix.admin.catalogo.domain.category.CategoryID;
import com.codeflix.admin.catalogo.domain.exceptions.NotFoundException;
import com.codeflix.admin.catalogo.domain.genre.Genre;
import com.codeflix.admin.catalogo.domain.genre.GenreGateway;
import com.codeflix.admin.catalogo.domain.genre.GenreID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

public class GetGenreUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultGetGenreUseCase useCase;

    @Mock
    private GenreGateway genreGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(genreGateway);
    }

    @Test
    public void givenValidId_whenCallsGetGenre_shouldReturnGenre() {
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(
                CategoryID.from("123"),
                CategoryID.from("456")
        );

        final var genre = Genre.newGenre(expectedName, expectedIsActive)
                .addCategories(expectedCategories);

        final var expectedId = genre.getId();

        Mockito.when(genreGateway.findById(Mockito.any()))
                .thenReturn(Optional.of(genre));
        final var actualGenre = useCase.execute(expectedId.getValue());

        Assertions.assertEquals(expectedId.getValue(), actualGenre.id());
        Assertions.assertEquals(expectedName, actualGenre.name());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(asString(expectedCategories), actualGenre.categories());
        Assertions.assertEquals(genre.getCreatedAt(), actualGenre.createdAt());
        Assertions.assertEquals(genre.getUpdatedAt(), actualGenre.updatedAt());
        Assertions.assertEquals(genre.getDeletedAt(), actualGenre.deletedAt());

        Mockito.verify(genreGateway, Mockito.times(1)).findById(Mockito.eq(expectedId));
    }

    @Test
    public void givenValidId_whenCallsGetGenreAndDoesNotExists_shouldReturnNotFound() {
        final var expectedErrorMessage = "Genre with ID 123 was not found";

        final var expectedId = GenreID.from("123");

        Mockito.when(genreGateway.findById(Mockito.eq(expectedId)))
                .thenReturn(Optional.empty());

        final var actualException = Assertions.assertThrows(NotFoundException.class, () -> useCase.execute(expectedId.getValue()));

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }
}
