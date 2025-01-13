package com.codeflix.admin.catalogo.application.castmember.update;

import com.codeflix.admin.catalogo.domain.Identifier;
import com.codeflix.admin.catalogo.domain.castmember.CastMember;
import com.codeflix.admin.catalogo.domain.castmember.CastMemberGateway;
import com.codeflix.admin.catalogo.domain.castmember.CastMemberID;
import com.codeflix.admin.catalogo.domain.exceptions.NotFoundException;
import com.codeflix.admin.catalogo.domain.exceptions.NotificationException;
import com.codeflix.admin.catalogo.domain.validation.handler.Notification;

import java.util.Objects;
import java.util.function.Supplier;

public non-sealed class DefaultUpdateCastMemberUseCase extends UpdateCastMemberUseCase {

    private final CastMemberGateway castMemberGateway;

    public DefaultUpdateCastMemberUseCase(final CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Override
    public UpdateCastMemberOutput execute(final UpdateCastMemberCommand command) {
        final var id = CastMemberID.from(command.id());
        final var name = command.name();
        final var type = command.type();

        final var member = this.castMemberGateway.findById(id)
                .orElseThrow(notFound(id));

        final var notification = Notification.create();
        notification.validate(() -> member.update(name, type));

        if (notification.hasError()) {
            notify(id, notification);
        }

        return UpdateCastMemberOutput.from(this.castMemberGateway.update(member));
    }

    private void notify(final Identifier id, final Notification notification) {
        throw new NotificationException("Could not update Aggregate CastMember %s".formatted(id.getValue()), notification);
    }

    private Supplier<NotFoundException> notFound(final CastMemberID id) {
        return () -> NotFoundException.with(CastMember.class, id);
    }
}

