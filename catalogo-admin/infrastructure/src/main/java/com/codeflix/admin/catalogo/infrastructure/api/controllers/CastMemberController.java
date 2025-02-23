package com.codeflix.admin.catalogo.infrastructure.api.controllers;

import com.codeflix.admin.catalogo.application.castmember.create.CreateCastMemberCommand;
import com.codeflix.admin.catalogo.application.castmember.create.CreateCastMemberUseCase;
import com.codeflix.admin.catalogo.application.castmember.delete.DeleteCastMemberUseCase;
import com.codeflix.admin.catalogo.application.castmember.retrieve.get.GetCastMemberUseCase;
import com.codeflix.admin.catalogo.application.castmember.retrieve.list.ListCastMembersUseCase;
import com.codeflix.admin.catalogo.application.castmember.update.UpdateCastMemberCommand;
import com.codeflix.admin.catalogo.application.castmember.update.UpdateCastMemberUseCase;
import com.codeflix.admin.catalogo.domain.pagination.Pagination;
import com.codeflix.admin.catalogo.domain.pagination.SearchQuery;
import com.codeflix.admin.catalogo.infrastructure.api.CastMemberAPI;
import com.codeflix.admin.catalogo.infrastructure.castmember.models.CastMemberListResponse;
import com.codeflix.admin.catalogo.infrastructure.castmember.models.CastMemberResponse;
import com.codeflix.admin.catalogo.infrastructure.castmember.models.CreateCastMemberRequest;
import com.codeflix.admin.catalogo.infrastructure.castmember.models.UpdateCastMemberRequest;
import com.codeflix.admin.catalogo.infrastructure.castmember.presenter.CastMemberPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;

@RestController
public class CastMemberController implements CastMemberAPI {

    private final CreateCastMemberUseCase createCastMemberUseCase;
    private final GetCastMemberUseCase getCastMemberUseCase;
    private final UpdateCastMemberUseCase updateCastMemberUseCase;
    private final DeleteCastMemberUseCase deleteCastMemberUseCase;
    private final ListCastMembersUseCase listCastMembersUseCase;

    public CastMemberController(
            final CreateCastMemberUseCase createCastMemberUseCase,
            final GetCastMemberUseCase getCastMemberUseCase,
            final UpdateCastMemberUseCase updateCastMemberUseCase,
            final DeleteCastMemberUseCase deleteCastMemberUseCase,
            final ListCastMembersUseCase listCastMembersUseCase
    ) {
        this.createCastMemberUseCase = Objects.requireNonNull(createCastMemberUseCase);
        this.getCastMemberUseCase = Objects.requireNonNull(getCastMemberUseCase);
        this.updateCastMemberUseCase = Objects.requireNonNull(updateCastMemberUseCase);
        this.deleteCastMemberUseCase = Objects.requireNonNull(deleteCastMemberUseCase);
        this.listCastMembersUseCase = Objects.requireNonNull(listCastMembersUseCase);
    }

    @Override
    public ResponseEntity<?> create(final CreateCastMemberRequest input) {
        final var command =
                CreateCastMemberCommand.with(input.name(), input.type());

        final var output = this.createCastMemberUseCase.execute(command);

        return ResponseEntity.created(URI.create("/cast_members/" + output.id())).body(output);
    }

    @Override
    public Pagination<CastMemberListResponse> list(
            final String search,
            final int page,
            final int perPage,
            final String sort,
            final String direction
    ) {
        return this.listCastMembersUseCase.execute(new SearchQuery(page, perPage, search, sort, direction))
                .map(CastMemberPresenter::present);
    }

    @Override
    public CastMemberResponse getById(final String id) {
        return CastMemberPresenter.present(this.getCastMemberUseCase.execute(id));
    }

    @Override
    public ResponseEntity<?> updateById(final String id, final UpdateCastMemberRequest body) {
        final var command =
                UpdateCastMemberCommand.with(id, body.name(), body.type());

        final var output = this.updateCastMemberUseCase.execute(command);

        return ResponseEntity.ok(output);
    }

    @Override
    public void deleteById(final String id) {
        this.deleteCastMemberUseCase.execute(id);
    }
}
