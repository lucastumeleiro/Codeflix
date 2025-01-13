package com.codeflix.admin.catalogo.application.castmember.retrieve.get;

import com.codeflix.admin.catalogo.domain.castmember.CastMember;
import com.codeflix.admin.catalogo.domain.castmember.CastMemberType;

import java.time.Instant;

public record CastMemberOutput(
        String id,
        String name,
        CastMemberType type,
        Instant createdAt,
        Instant updatedAt
) {

    public static CastMemberOutput from(final CastMember member) {
        return new CastMemberOutput(
                member.getId().getValue(),
                member.getName(),
                member.getType(),
                member.getCreatedAt(),
                member.getUpdatedAt()
        );
    }
}