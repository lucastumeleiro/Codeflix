package com.codeflix.admin.catalogo.infrastructure.configuration.useCases;

import com.codeflix.admin.catalogo.application.castmember.create.CreateCastMemberUseCase;
import com.codeflix.admin.catalogo.application.castmember.create.DefaultCreateCastMemberUseCase;
import com.codeflix.admin.catalogo.application.castmember.delete.DefaultDeleteCastMemberUseCase;
import com.codeflix.admin.catalogo.application.castmember.delete.DeleteCastMemberUseCase;
import com.codeflix.admin.catalogo.application.castmember.retrieve.get.DefaultGetCastMemberUseCase;
import com.codeflix.admin.catalogo.application.castmember.retrieve.get.GetCastMemberUseCase;
import com.codeflix.admin.catalogo.application.castmember.retrieve.list.DefaultListCastMembersUseCase;
import com.codeflix.admin.catalogo.application.castmember.retrieve.list.ListCastMembersUseCase;
import com.codeflix.admin.catalogo.application.castmember.update.DefaultUpdateCastMemberUseCase;
import com.codeflix.admin.catalogo.application.castmember.update.UpdateCastMemberUseCase;
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
import com.codeflix.admin.catalogo.domain.castmember.CastMemberGateway;
import com.codeflix.admin.catalogo.domain.category.CategoryGateway;
import com.codeflix.admin.catalogo.domain.genre.GenreGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class CastMemberUseCaseConfig {

    private final CastMemberGateway castMemberGateway;

    public CastMemberUseCaseConfig(final CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Bean
    public CreateCastMemberUseCase createCastMemberUseCase() {
        return new DefaultCreateCastMemberUseCase(castMemberGateway);
    }

    @Bean
    public DeleteCastMemberUseCase deleteCastMemberUseCase() {
        return new DefaultDeleteCastMemberUseCase(castMemberGateway);
    }

    @Bean
    public GetCastMemberUseCase getCastMemberByIdUseCase() {
        return new DefaultGetCastMemberUseCase(castMemberGateway);
    }

    @Bean
    public ListCastMembersUseCase listCastMembersUseCase() {
        return new DefaultListCastMembersUseCase(castMemberGateway);
    }

    @Bean
    public UpdateCastMemberUseCase updateCastMemberUseCase() {
        return new DefaultUpdateCastMemberUseCase(castMemberGateway);
    }
}
