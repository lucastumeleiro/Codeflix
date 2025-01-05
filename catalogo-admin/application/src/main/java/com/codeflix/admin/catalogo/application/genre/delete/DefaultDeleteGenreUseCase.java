package com.codeflix.admin.catalogo.application.genre.delete;

import com.codeflix.admin.catalogo.domain.genre.GenreGateway;
import com.codeflix.admin.catalogo.domain.genre.GenreID;

import java.util.Objects;

public class DefaultDeleteGenreUseCase extends DeleteGenreUseCase {

    private final GenreGateway gateway;

    public DefaultDeleteGenreUseCase(final GenreGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Override
    public void execute(final String anIn) {
        this.gateway.deleteById(GenreID.from(anIn));
    }
}
