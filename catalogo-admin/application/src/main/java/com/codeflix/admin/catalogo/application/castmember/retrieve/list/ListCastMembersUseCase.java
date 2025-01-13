package com.codeflix.admin.catalogo.application.castmember.retrieve.list;

import com.codeflix.admin.catalogo.application.UseCase;
import com.codeflix.admin.catalogo.domain.pagination.Pagination;
import com.codeflix.admin.catalogo.domain.pagination.SearchQuery;

public sealed abstract class ListCastMembersUseCase
        extends UseCase<SearchQuery, Pagination<CastMemberListOutput>>
        permits DefaultListCastMembersUseCase {
}
