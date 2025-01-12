package com.codeflix.admin.catalogo.infrastructure.configuration.useCases;

import com.codeflix.admin.catalogo.application.genre.create.CreateGenreUseCase;
import com.codeflix.admin.catalogo.application.genre.create.DefaultCreateGenreUseCase;
import com.codeflix.admin.catalogo.application.genre.delete.DefaultDeleteGenreUseCase;
import com.codeflix.admin.catalogo.application.genre.delete.DeleteGenreUseCase;
import com.codeflix.admin.catalogo.application.genre.retrieve.get.DefaultGetGenreUseCase;
import com.codeflix.admin.catalogo.application.genre.retrieve.get.GetGenreUseCase;
import com.codeflix.admin.catalogo.application.genre.retrieve.list.DefaultListGenreUseCase;
import com.codeflix.admin.catalogo.application.genre.retrieve.list.ListGenreUseCase;
import com.codeflix.admin.catalogo.application.genre.update.DefaultUpdateGenreUseCase;
import com.codeflix.admin.catalogo.application.genre.update.UpdateGenreUseCase;
import com.codeflix.admin.catalogo.domain.category.CategoryGateway;
import com.codeflix.admin.catalogo.domain.genre.GenreGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class GenreUseCaseConfig {

    private final CategoryGateway categoryGateway;
    private final GenreGateway genreGateway;

    public GenreUseCaseConfig(
            final CategoryGateway categoryGateway,
            final GenreGateway genreGateway
    ) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Bean
    public CreateGenreUseCase createGenreUseCase() {
        return new DefaultCreateGenreUseCase(categoryGateway, genreGateway);
    }

    @Bean
    public UpdateGenreUseCase updateGenreUseCase() {
        return new DefaultUpdateGenreUseCase(categoryGateway, genreGateway);
    }

    @Bean
    public GetGenreUseCase getGenreUseCase() {
        return new DefaultGetGenreUseCase(genreGateway);
    }

    @Bean
    public ListGenreUseCase listGenreUseCase() {
        return new DefaultListGenreUseCase(genreGateway);
    }

    @Bean
    public DeleteGenreUseCase deleteGenreUseCase() {
        return new DefaultDeleteGenreUseCase(genreGateway);
    }
}
