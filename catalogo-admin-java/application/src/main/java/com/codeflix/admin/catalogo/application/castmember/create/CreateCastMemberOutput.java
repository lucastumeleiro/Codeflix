package com.codeflix.admin.catalogo.application.castmember.create;

import com.codeflix.admin.catalogo.domain.castmember.CastMember;
import com.codeflix.admin.catalogo.domain.castmember.CastMemberID;

public record CreateCastMemberOutput(
        String id
) {

    public static CreateCastMemberOutput from(final CastMemberID id) {
        return new CreateCastMemberOutput(id.getValue());
    }

    public static CreateCastMemberOutput from(final CastMember member) {
        return from(member.getId());
    }
}
