package com.codeflix.admin.catalogo.application.genre.retrieve.list;

import com.codeflix.admin.catalogo.IntegrationTest;
import com.codeflix.admin.catalogo.domain.genre.Genre;
import com.codeflix.admin.catalogo.domain.genre.GenreGateway;
import com.codeflix.admin.catalogo.domain.pagination.SearchQuery;
import com.codeflix.admin.catalogo.infrastructure.genre.persistence.GenreJpaEntity;
import com.codeflix.admin.catalogo.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@IntegrationTest
public class ListGenreUseCaseIT{

    @Autowired
    private ListGenreUseCase useCase;
    @Autowired
    private GenreGateway genreGateway;
    @Autowired
    private GenreRepository genreRepository;

    @Test
    public void givenValidQuery_whenCallsListGenre_shouldReturnGenres() {
        final var genres = List.of(
                Genre.newGenre("Ação", true),
                Genre.newGenre("Aventura", true)
        );

        genreRepository.saveAllAndFlush(
                genres.stream()
                        .map(GenreJpaEntity::from)
                        .toList()
        );

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "A";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 2;

        final var expectedItems = genres.stream()
                .map(GenreListOutput::from)
                .toList();

        final var query =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualOutput = useCase.execute(query);

        Assertions.assertEquals(expectedPage, actualOutput.currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.total());
        Assertions.assertTrue(
                expectedItems.size() == actualOutput.items().size()
                        && expectedItems.containsAll(actualOutput.items())
        );
    }

    @Test
    public void givenValidQuery_whenCallsListGenreAndResultIsEmpty_shouldReturnGenres() {
        final var genres = List.<Genre>of();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "A";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;

        final var expectedItems = List.<GenreListOutput>of();

        final var query =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualOutput = useCase.execute(query);

        Assertions.assertEquals(expectedPage, actualOutput.currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.total());
        Assertions.assertEquals(expectedItems, actualOutput.items());
    }
}
