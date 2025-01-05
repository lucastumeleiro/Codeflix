package com.codeflix.admin.catalogo.application.genre.retrieve.get;

import com.codeflix.admin.catalogo.domain.exceptions.NotFoundException;
import com.codeflix.admin.catalogo.domain.genre.Genre;
import com.codeflix.admin.catalogo.domain.genre.GenreGateway;
import com.codeflix.admin.catalogo.domain.genre.GenreID;

import java.util.Objects;

public class DefaultGetGenreUseCase extends GetGenreUseCase{

    private final GenreGateway gateway;

    public DefaultGetGenreUseCase(final GenreGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Override
    public GenreOutput execute(final String in) {
        final var genreId = GenreID.from(in);
        return this.gateway.findById(genreId)
                .map(GenreOutput::from)
                .orElseThrow(() -> NotFoundException.with(Genre.class, genreId));
    }
}