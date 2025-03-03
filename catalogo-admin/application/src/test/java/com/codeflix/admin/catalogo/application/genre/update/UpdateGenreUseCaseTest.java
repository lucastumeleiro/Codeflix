package com.codeflix.admin.catalogo.application.genre.update;

import com.codeflix.admin.catalogo.application.UseCaseTest;
import com.codeflix.admin.catalogo.domain.category.CategoryGateway;
import com.codeflix.admin.catalogo.domain.category.CategoryID;
import com.codeflix.admin.catalogo.domain.exceptions.NotificationException;
import com.codeflix.admin.catalogo.domain.genre.Genre;
import com.codeflix.admin.catalogo.domain.genre.GenreGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class UpdateGenreUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultUpdateGenreUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @Mock
    private GenreGateway genreGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(categoryGateway, genreGateway);
    }

    @Test
    public void givenValidCommand_whenCallsUpdateGenre_shouldReturnGenreId() throws Exception {
        final var genre = Genre.newGenre("acao", true);

        final var expectedId = genre.getId();
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var command = UpdateGenreCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedIsActive,
                asString(expectedCategories)
        );

        Mockito.when(genreGateway.findById(Mockito.any()))
                .thenReturn(Optional.of(Genre.with(genre)));

        Mockito.when(genreGateway.update(Mockito.any()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());
        final var actualOutput = useCase.execute(command);

        Thread.sleep(10);

        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedId.getValue(), actualOutput.id());

        Mockito.verify(genreGateway, Mockito.times(1)).findById(Mockito.eq(expectedId));

        Mockito.verify(genreGateway, Mockito.times(1)).update(Mockito.argThat(updatedGenre ->
                Objects.equals(expectedId, updatedGenre.getId())
                        && Objects.equals(expectedName, updatedGenre.getName())
                        && Objects.equals(expectedIsActive, updatedGenre.isActive())
                        && Objects.equals(expectedCategories, updatedGenre.getCategories())
                        && Objects.equals(genre.getCreatedAt(), updatedGenre.getCreatedAt())
                        && genre.getUpdatedAt().isBefore(updatedGenre.getUpdatedAt())
                        && Objects.isNull(updatedGenre.getDeletedAt())
        ));
    }

    @Test
    public void givenValidCommandWithCategories_whenCallsUpdateGenre_shouldReturnGenreId() throws Exception {
        final var genre = Genre.newGenre("acao", true);

        final var expectedId = genre.getId();
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(
                CategoryID.from("123"),
                CategoryID.from("456")
        );

        final var command = UpdateGenreCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedIsActive,
                asString(expectedCategories)
        );

        Mockito.when(genreGateway.findById(Mockito.any()))
                .thenReturn(Optional.of(Genre.with(genre)));

        Mockito.when(categoryGateway.existsByIds(Mockito.any()))
                .thenReturn(expectedCategories);

        Mockito.when(genreGateway.update(Mockito.any()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());
        final var actualOutput = useCase.execute(command);

        Thread.sleep(10);

        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedId.getValue(), actualOutput.id());

        Mockito.verify(genreGateway, Mockito.times(1)).findById(Mockito.eq(expectedId));

        Mockito.verify(categoryGateway, Mockito.times(1)).existsByIds(Mockito.eq(expectedCategories));

        Mockito.verify(genreGateway, Mockito.times(1)).update(Mockito.argThat(updatedGenre ->
                Objects.equals(expectedId, updatedGenre.getId())
                        && Objects.equals(expectedName, updatedGenre.getName())
                        && Objects.equals(expectedIsActive, updatedGenre.isActive())
                        && Objects.equals(expectedCategories, updatedGenre.getCategories())
                        && Objects.equals(genre.getCreatedAt(), updatedGenre.getCreatedAt())
                        && genre.getUpdatedAt().isBefore(updatedGenre.getUpdatedAt())
                        && Objects.isNull(updatedGenre.getDeletedAt())
        ));
    }

    @Test
    public void givenValidCommandWithInactiveGenre_whenCallsUpdateGenre_shouldReturnGenreId() throws Exception {
        final var genre = Genre.newGenre("acao", true);

        final var expectedId = genre.getId();
        final var expectedName = "Ação";
        final var expectedIsActive = false;
        final var expectedCategories = List.<CategoryID>of();

        final var command = UpdateGenreCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedIsActive,
                asString(expectedCategories)
        );

        Mockito.when(genreGateway.findById(Mockito.any()))
                .thenReturn(Optional.of(Genre.with(genre)));

        Mockito.when(genreGateway.update(Mockito.any()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());

        Thread.sleep(10);

        Assertions.assertTrue(genre.isActive());
        Assertions.assertNull(genre.getDeletedAt());

        final var actualOutput = useCase.execute(command);

        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedId.getValue(), actualOutput.id());

        Mockito.verify(genreGateway, Mockito.times(1)).findById(Mockito.eq(expectedId));

        Mockito.verify(genreGateway, Mockito.times(1)).update(Mockito.argThat(updatedGenre ->
                Objects.equals(expectedId, updatedGenre.getId())
                        && Objects.equals(expectedName, updatedGenre.getName())
                        && Objects.equals(expectedIsActive, updatedGenre.isActive())
                        && Objects.equals(expectedCategories, updatedGenre.getCategories())
                        && Objects.equals(genre.getCreatedAt(), updatedGenre.getCreatedAt())
                        && genre.getUpdatedAt().isBefore(updatedGenre.getUpdatedAt())
                        && Objects.nonNull(updatedGenre.getDeletedAt())
        ));
    }

    @Test
    public void givenInvalidName_whenCallsUpdateGenre_shouldReturnNotificationException() {
        final var genre = Genre.newGenre("acao", true);

        final var expectedId = genre.getId();
        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var command = UpdateGenreCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedIsActive,
                asString(expectedCategories)
        );

        Mockito.when(genreGateway.findById(Mockito.any()))
                .thenReturn(Optional.of(Genre.with(genre)));

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> useCase.execute(command));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        Mockito.verify(genreGateway, Mockito.times(1)).findById(Mockito.eq(expectedId));

        Mockito.verify(categoryGateway, Mockito.times(0)).existsByIds(Mockito.any());

        Mockito.verify(genreGateway, Mockito.times(0)).update(Mockito.any());
    }

    @Test
    public void givenInvalidName_whenCallsUpdateGenreAndSomeCategoriesDoesNotExists_shouldReturnNotificationException() {
        final var filmes = CategoryID.from("123");
        final var series = CategoryID.from("456");
        final var documentarios = CategoryID.from("789");

        final var genre = Genre.newGenre("acao", true);

        final var expectedId = genre.getId();
        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedCategories = List.of(filmes, series, documentarios);

        final var expectedErrorCount = 2;
        final var expectedErrorMessageOne = "Some categories could not be found: 456, 789";
        final var expectedErrorMessageTwo = "'name' should not be null";

        final var command = UpdateGenreCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedIsActive,
                asString(expectedCategories)
        );

        Mockito.when(genreGateway.findById(Mockito.any()))
                .thenReturn(Optional.of(Genre.with(genre)));

        Mockito.when(categoryGateway.existsByIds(Mockito.any()))
                .thenReturn(List.of(filmes));

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> useCase.execute(command));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessageOne, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorMessageTwo, actualException.getErrors().get(1).message());

        Mockito.verify(genreGateway, Mockito.times(1)).findById(Mockito.eq(expectedId));

        Mockito.verify(categoryGateway, Mockito.times(1)).existsByIds(Mockito.eq(expectedCategories));

        Mockito.verify(genreGateway, Mockito.times(0)).update(Mockito.any());
    }
}