package com.codeflix.admin.catalogo.infrastructure.api;

import com.codeflix.admin.catalogo.ControllerTest;
import com.codeflix.admin.catalogo.application.genre.create.CreateGenreOutput;
import com.codeflix.admin.catalogo.application.genre.create.CreateGenreUseCase;
import com.codeflix.admin.catalogo.application.genre.delete.DeleteGenreUseCase;
import com.codeflix.admin.catalogo.application.genre.retrieve.get.GenreOutput;
import com.codeflix.admin.catalogo.application.genre.retrieve.get.GetGenreUseCase;
import com.codeflix.admin.catalogo.application.genre.retrieve.list.GenreListOutput;
import com.codeflix.admin.catalogo.application.genre.retrieve.list.ListGenreUseCase;
import com.codeflix.admin.catalogo.application.genre.update.UpdateGenreOutput;
import com.codeflix.admin.catalogo.application.genre.update.UpdateGenreUseCase;
import com.codeflix.admin.catalogo.domain.category.CategoryID;
import com.codeflix.admin.catalogo.domain.exceptions.NotFoundException;
import com.codeflix.admin.catalogo.domain.exceptions.NotificationException;
import com.codeflix.admin.catalogo.domain.genre.Genre;
import com.codeflix.admin.catalogo.domain.genre.GenreID;
import com.codeflix.admin.catalogo.domain.pagination.Pagination;
import com.codeflix.admin.catalogo.domain.validation.handler.Notification;
import com.codeflix.admin.catalogo.infrastructure.genre.models.CreateGenreRequest;
import com.codeflix.admin.catalogo.infrastructure.genre.models.UpdateGenreRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Objects;

@ControllerTest(controllers = GenreAPI.class)
public class GenreAPITest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;

    @MockitoBean
    private CreateGenreUseCase createGenreUseCase;
    @MockitoBean
    private GetGenreUseCase getGenreUseCase;
    @MockitoBean
    private UpdateGenreUseCase updateGenreUseCase;
    @MockitoBean
    private DeleteGenreUseCase deleteGenreUseCase;
    @MockitoBean
    private ListGenreUseCase listGenreUseCase;

    @Test
    public void givenValidCommand_whenCallsCreateGenre_shouldReturnGenreId() throws Exception {
        final var expectedName = "Ação";
        final var expectedCategories = List.of("123", "456");
        final var expectedIsActive = true;
        final var expectedId = "123";

        final var command =
                new CreateGenreRequest(expectedName, expectedCategories, expectedIsActive);

        Mockito.when(createGenreUseCase.execute(Mockito.any()))
                .thenReturn(CreateGenreOutput.from(expectedId));

        final var request = MockMvcRequestBuilders.post("/genres")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(command));

        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", "/genres/" + expectedId))
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(expectedId)));

        Mockito.verify(createGenreUseCase).execute(Mockito.argThat(genreCommand ->
                Objects.equals(expectedName, genreCommand.name())
                        && Objects.equals(expectedCategories, genreCommand.categories())
                        && Objects.equals(expectedIsActive, genreCommand.isActive())
        ));
    }

    @Test
    public void givenInvalidName_whenCallsCreateGenre_shouldReturnNotification() throws Exception {
        final String expectedName = null;
        final var expectedCategories = List.of("123", "456");
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be null";

        final var command =
                new CreateGenreRequest(expectedName, expectedCategories, expectedIsActive);

        Mockito.when(createGenreUseCase.execute(Mockito.any()))
                .thenThrow(new NotificationException("Error", Notification.create(new Error(expectedErrorMessage))));

        final var request = MockMvcRequestBuilders.post("/genres")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(command));

        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        response.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Location", Matchers.nullValue()))
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", Matchers.equalTo(expectedErrorMessage)));

        Mockito.verify(createGenreUseCase).execute(Mockito.argThat(genreCommand ->
                Objects.equals(expectedName, genreCommand.name())
                        && Objects.equals(expectedCategories, genreCommand.categories())
                        && Objects.equals(expectedIsActive, genreCommand.isActive())
        ));
    }

    @Test
    public void givenValidId_whenCallsGetGenreById_shouldReturnGenre() throws Exception {
        final var expectedName = "Ação";
        final var expectedCategories = List.of("123", "456");
        final var expectedIsActive = false;

        final var genre = Genre.newGenre(expectedName, expectedIsActive)
                .addCategories(
                        expectedCategories.stream()
                                .map(CategoryID::from)
                                .toList()
                );

        final var expectedId = genre.getId().getValue();

        Mockito.when(getGenreUseCase.execute(Mockito.any()))
                .thenReturn(GenreOutput.from(genre));

        final var request = MockMvcRequestBuilders.get("/genres/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request);

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(expectedId)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.equalTo(expectedName)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.categories_id", Matchers.equalTo(expectedCategories)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.is_active", Matchers.equalTo(expectedIsActive)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.created_at", Matchers.equalTo(genre.getCreatedAt().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.updated_at", Matchers.equalTo(genre.getUpdatedAt().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.deleted_at", Matchers.equalTo(genre.getDeletedAt().toString())));

        Mockito.verify(getGenreUseCase).execute(Mockito.eq(expectedId));
    }

    @Test
    public void givenInvalidId_whenCallsGetGenreById_shouldReturnNotFound() throws Exception {
        final var expectedErrorMessage = "Genre with ID 123 was not found";
        final var expectedId = GenreID.from("123");

        Mockito.when(getGenreUseCase.execute(Mockito.any()))
                .thenThrow(NotFoundException.with(Genre.class, expectedId));

        final var request = MockMvcRequestBuilders.get("/genres/{id}", expectedId.getValue())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request);

        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo(expectedErrorMessage)));

        Mockito.verify(getGenreUseCase).execute(Mockito.eq(expectedId.getValue()));
    }

    @Test
    public void givenValidCommand_whenCallsUpdateGenre_shouldReturnGenreId() throws Exception {
        final var expectedName = "Ação";
        final var expectedCategories = List.of("123", "456");
        final var expectedIsActive = true;

        final var genre = Genre.newGenre(expectedName, expectedIsActive);
        final var expectedId = genre.getId().getValue();

        final var command =
                new UpdateGenreRequest(expectedName, expectedCategories, expectedIsActive);

        Mockito.when(updateGenreUseCase.execute(Mockito.any()))
                .thenReturn(UpdateGenreOutput.from(genre));

        final var request = MockMvcRequestBuilders.put("/genres/{id}", expectedId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(command));

        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(expectedId)));

        Mockito.verify(updateGenreUseCase).execute(Mockito.argThat(genreCommand ->
                Objects.equals(expectedName, genreCommand.name())
                        && Objects.equals(expectedCategories, genreCommand.categories())
                        && Objects.equals(expectedIsActive, genreCommand.isActive())
        ));
    }

    @Test
    public void givenInvalidName_whenCallsUpdateGenre_shouldReturnNotification() throws Exception {
        final String expectedName = null;
        final var expectedCategories = List.of("123", "456");
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be null";

        final var genre = Genre.newGenre("Ação", expectedIsActive);
        final var expectedId = genre.getId().getValue();

        final var command =
                new UpdateGenreRequest(expectedName, expectedCategories, expectedIsActive);

        Mockito.when(updateGenreUseCase.execute(Mockito.any()))
                .thenThrow(new NotificationException("Error", Notification.create(new Error(expectedErrorMessage))));

        final var request = MockMvcRequestBuilders.put("/genres/{id}", expectedId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(command));

        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        response.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", Matchers.equalTo(expectedErrorMessage)));

        Mockito.verify(updateGenreUseCase).execute(Mockito.argThat(genreCommand ->
                Objects.equals(expectedName, genreCommand.name())
                        && Objects.equals(expectedCategories, genreCommand.categories())
                        && Objects.equals(expectedIsActive, genreCommand.isActive())
        ));
    }

    @Test
    public void givenValidId_whenCallsDeleteGenre_shouldBeOK() throws Exception {
        final var expectedId = "123";

        Mockito.doNothing()
                .when(deleteGenreUseCase).execute(Mockito.any());

        final var request = MockMvcRequestBuilders.delete("/genres/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON);

        final var result = this.mvc.perform(request);

        result.andExpect(MockMvcResultMatchers.status().isNoContent());

        Mockito.verify(deleteGenreUseCase).execute(Mockito.eq(expectedId));
    }

    @Test
    public void givenValidParams_whenCallsListGenres_shouldReturnGenres() throws Exception {
        final var genre = Genre.newGenre("Ação", false);

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "ac";
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var expectedItemsCount = 1;
        final var expectedTotal = 1;

        final var expectedItems = List.of(GenreListOutput.from(genre));

        Mockito.when(listGenreUseCase.execute(Mockito.any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems));

        final var request = MockMvcRequestBuilders.get("/genres")
                .queryParam("page", String.valueOf(expectedPage))
                .queryParam("perPage", String.valueOf(expectedPerPage))
                .queryParam("sort", expectedSort)
                .queryParam("dir", expectedDirection)
                .queryParam("search", expectedTerms)
                .accept(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request);

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(expectedPage)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(expectedPerPage)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(expectedTotal)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(expectedItemsCount)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].id", Matchers.equalTo(genre.getId().getValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo(genre.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].is_active", Matchers.equalTo(genre.isActive())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].created_at", Matchers.equalTo(genre.getCreatedAt().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].deleted_at", Matchers.equalTo(genre.getDeletedAt().toString())));

        Mockito.verify(listGenreUseCase).execute(Mockito.argThat(query ->
                Objects.equals(expectedPage, query.page())
                        && Objects.equals(expectedPerPage, query.perPage())
                        && Objects.equals(expectedDirection, query.direction())
                        && Objects.equals(expectedSort, query.sort())
                        && Objects.equals(expectedTerms, query.terms())
        ));
    }
}

