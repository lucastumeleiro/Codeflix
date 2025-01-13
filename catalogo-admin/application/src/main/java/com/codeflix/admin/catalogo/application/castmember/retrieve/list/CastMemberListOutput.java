package com.codeflix.admin.catalogo.application.castmember.retrieve.list;

import com.codeflix.admin.catalogo.domain.castmember.CastMember;
import com.codeflix.admin.catalogo.domain.castmember.CastMemberType;

import java.time.Instant;

public record CastMemberListOutput(
        String id,
        String name,
        CastMemberType type,
        Instant createdAt
) {

    public static CastMemberListOutput from(final CastMember member) {
        return new CastMemberListOutput(
                member.getId().getValue(),
                member.getName(),
                member.getType(),
                member.getCreatedAt()
        );
    }
}
