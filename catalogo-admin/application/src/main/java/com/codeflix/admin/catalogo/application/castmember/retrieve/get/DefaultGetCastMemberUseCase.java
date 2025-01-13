package com.codeflix.admin.catalogo.application.castmember.retrieve.get;

import com.codeflix.admin.catalogo.domain.castmember.CastMember;
import com.codeflix.admin.catalogo.domain.castmember.CastMemberGateway;
import com.codeflix.admin.catalogo.domain.castmember.CastMemberID;
import com.codeflix.admin.catalogo.domain.exceptions.NotFoundException;

import java.util.Objects;

public non-sealed class DefaultGetCastMemberUseCase extends GetCastMemberUseCase {

    private final CastMemberGateway castMemberGateway;

    public DefaultGetCastMemberUseCase(final CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Override
    public CastMemberOutput execute(final String in) {
        final var memberId = CastMemberID.from(in);
        return this.castMemberGateway.findById(memberId)
                .map(CastMemberOutput::from)
                .orElseThrow(() -> NotFoundException.with(CastMember.class, memberId));
    }
}
