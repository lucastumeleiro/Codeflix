package com.codeflix.admin.catalogo.application.castmember.update;

import com.codeflix.admin.catalogo.domain.castmember.CastMemberType;

public record UpdateCastMemberCommand(
        String id,
        String name,
        CastMemberType type
) {

    public static UpdateCastMemberCommand with(
            final String id,
            final String name,
            final CastMemberType type
    ) {
        return new UpdateCastMemberCommand(id, name, type);
    }
}