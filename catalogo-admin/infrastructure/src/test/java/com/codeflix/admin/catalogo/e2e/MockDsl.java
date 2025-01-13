package com.codeflix.admin.catalogo.e2e;

import com.codeflix.admin.catalogo.domain.Identifier;
import com.codeflix.admin.catalogo.domain.category.CategoryID;
import com.codeflix.admin.catalogo.domain.genre.GenreID;
import com.codeflix.admin.catalogo.infrastructure.category.models.CategoryResponse;
import com.codeflix.admin.catalogo.infrastructure.category.models.CreateCategoryRequest;
import com.codeflix.admin.catalogo.infrastructure.category.models.UpdateCategoryRequest;
import com.codeflix.admin.catalogo.infrastructure.configuration.json.Json;
import com.codeflix.admin.catalogo.infrastructure.genre.models.CreateGenreRequest;
import com.codeflix.admin.catalogo.infrastructure.genre.models.GenreResponse;
import com.codeflix.admin.catalogo.infrastructure.genre.models.UpdateGenreRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public interface MockDsl {

    MockMvc mvc();

    /**
     * Category
     */
    default ResultActions deleteCategory(final CategoryID id) throws Exception {
        return this.delete("/categories/", id);
    }

    default CategoryID givenCategory(final String aName, final String aDescription, final boolean isActive) throws Exception {
        final var aRequestBody = new CreateCategoryRequest(aName, aDescription, isActive);
        final var actualId = this.given("/categories", aRequestBody);
        return CategoryID.from(actualId);
    }

    default ResultActions listCategories(final int page, final int perPage) throws Exception {
        return listCategories(page, perPage, "", "", "");
    }

    default ResultActions listCategories(final int page, final int perPage, final String search) throws Exception {
        return listCategories(page, perPage, search, "", "");
    }

    default ResultActions listCategories(final int page, final int perPage, final String search, final String sort, final String direction) throws Exception {
        return this.list("/categories", page, perPage, search, sort, direction);
    }

    default CategoryResponse retrieveCategory(final CategoryID id) throws Exception {
        return this.retrieve("/categories/", id, CategoryResponse.class);
    }

    default ResultActions updateCategory(final CategoryID id, final UpdateCategoryRequest aRequest) throws Exception {
        return this.update("/categories/", id, aRequest);
    }

    /**
     * Genre
     */
    default ResultActions deleteGenre(final GenreID id) throws Exception {
        return this.delete("/genres/", id);
    }

    default GenreID givenGenre(final String aName, final boolean isActive, final List<CategoryID> categories) throws Exception {
        final var aRequestBody = new CreateGenreRequest(aName, mapTo(categories, CategoryID::getValue), isActive);
        final var actualId = this.given("/genres", aRequestBody);
        return GenreID.from(actualId);
    }

    default ResultActions listGenres(final int page, final int perPage) throws Exception {
        return listGenres(page, perPage, "", "", "");
    }

    default ResultActions listGenres(final int page, final int perPage, final String search) throws Exception {
        return listGenres(page, perPage, search, "", "");
    }

    default ResultActions listGenres(final int page, final int perPage, final String search, final String sort, final String direction) throws Exception {
        return this.list("/genres", page, perPage, search, sort, direction);
    }

    default GenreResponse retrieveGenre(final GenreID id) throws Exception {
        return this.retrieve("/genres/", id, GenreResponse.class);
    }

    default ResultActions updateGenre(final GenreID id, final UpdateGenreRequest aRequest) throws Exception {
        return this.update("/genres/", id, aRequest);
    }

    default <A, D> List<D> mapTo(final List<A> actual, final Function<A, D> mapper) {
        return actual.stream()
                .map(mapper)
                .toList();
    }

    private String given(final String url, final Object body) throws Exception {
        final var aRequest = MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(body));

        final var actualId = Objects.requireNonNull(this.mvc().perform(aRequest)
                        .andExpect(MockMvcResultMatchers.status().isCreated())
                        .andReturn()
                        .getResponse().getHeader("Location"))
                .replace("%s/".formatted(url), "");

        return actualId;
    }

    private ResultActions givenResult(final String url, final Object body) throws Exception {
        final var aRequest = MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(body));

        return this.mvc().perform(aRequest);
    }

    private ResultActions list(final String url, final int page, final int perPage, final String search, final String sort, final String direction) throws Exception {
        final var aRequest = MockMvcRequestBuilders.get(url)
                .queryParam("page", String.valueOf(page))
                .queryParam("perPage", String.valueOf(perPage))
                .queryParam("search", search)
                .queryParam("sort", sort)
                .queryParam("dir", direction)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        return this.mvc().perform(aRequest);
    }

    private <T> T retrieve(final String url, final Identifier id, final Class<T> clazz) throws Exception {
        final var aRequest = MockMvcRequestBuilders.get(url + id.getValue())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var json = this.mvc().perform(aRequest)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse().getContentAsString();

        return Json.readValue(json, clazz);
    }

    private ResultActions retrieveResult(final String url, final Identifier id) throws Exception {
        final var aRequest = MockMvcRequestBuilders.get(url + id.getValue())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        return this.mvc().perform(aRequest);
    }

    private ResultActions delete(final String url, final Identifier id) throws Exception {
        final var aRequest = MockMvcRequestBuilders.delete(url + id.getValue())
                .contentType(MediaType.APPLICATION_JSON);

        return this.mvc().perform(aRequest);
    }

    private ResultActions update(final String url, final Identifier id, final Object aRequestBody) throws Exception {
        final var aRequest = MockMvcRequestBuilders.put(url + id.getValue())
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(aRequestBody));

        return this.mvc().perform(aRequest);
    }
}

