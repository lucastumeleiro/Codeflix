package com.codeflix.admin.catalogo.domain.castmember;

import com.codeflix.admin.catalogo.domain.pagination.Pagination;
import com.codeflix.admin.catalogo.domain.pagination.SearchQuery;

import java.util.List;
import java.util.Optional;

public interface CastMemberGateway {

    CastMember create(CastMember aCastMember);

    void deleteById(CastMemberID anId);

    Optional<CastMember> findById(CastMemberID anId);

    CastMember update(CastMember aCastMember);

    Pagination<CastMember> findAll(SearchQuery aQuery);

    List<CastMemberID> existsByIds(Iterable<CastMemberID> ids);
}
