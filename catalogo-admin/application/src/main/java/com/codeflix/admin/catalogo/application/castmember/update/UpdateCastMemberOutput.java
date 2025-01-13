package com.codeflix.admin.catalogo.application.castmember.update;

import com.codeflix.admin.catalogo.domain.castmember.CastMember;
import com.codeflix.admin.catalogo.domain.castmember.CastMemberID;

public record UpdateCastMemberOutput(String id) {

    public static UpdateCastMemberOutput from(final CastMemberID id) {
        return new UpdateCastMemberOutput(id.getValue());
    }

    public static UpdateCastMemberOutput from(final CastMember member) {
        return from(member.getId());
    }
}
