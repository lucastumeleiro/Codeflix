package com.codeflix.admin.catalogo.application.genre.retrieve.list;

import com.codeflix.admin.catalogo.domain.genre.GenreGateway;
import com.codeflix.admin.catalogo.domain.pagination.Pagination;
import com.codeflix.admin.catalogo.domain.pagination.SearchQuery;

import java.util.Objects;

public class DefaultListGenreUseCase extends ListGenreUseCase {

    private final GenreGateway gateway;

    public DefaultListGenreUseCase(final GenreGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Override
    public Pagination<GenreListOutput> execute(final SearchQuery query) {
        return this.gateway.findAll(query)
                .map(GenreListOutput::from);
    }
}
