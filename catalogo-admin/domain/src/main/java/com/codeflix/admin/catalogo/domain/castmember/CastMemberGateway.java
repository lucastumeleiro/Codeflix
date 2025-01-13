package com.codeflix.admin.catalogo.domain.castmember;

import com.codeflix.admin.catalogo.domain.pagination.Pagination;
import com.codeflix.admin.catalogo.domain.pagination.SearchQuery;

import java.util.List;
import java.util.Optional;

public interface CastMemberGateway {

    CastMember create(CastMember castMember);

    void deleteById(CastMemberID id);

    Optional<CastMember> findById(CastMemberID id);

    CastMember update(CastMember castMember);

    Pagination<CastMember> findAll(SearchQuery query);

    List<CastMemberID> existsByIds(Iterable<CastMemberID> ids);
}
