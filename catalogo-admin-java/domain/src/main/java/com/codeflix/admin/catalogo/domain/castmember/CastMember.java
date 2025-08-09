package com.codeflix.admin.catalogo.domain.castmember;

import com.codeflix.admin.catalogo.domain.AggregateRoot;
import com.codeflix.admin.catalogo.domain.Utils.InstantUtils;
import com.codeflix.admin.catalogo.domain.exceptions.NotificationException;
import com.codeflix.admin.catalogo.domain.validation.ValidationHandler;
import com.codeflix.admin.catalogo.domain.validation.handler.Notification;

import java.time.Instant;

public class CastMember extends AggregateRoot<CastMemberID> {

    private String name;
    private CastMemberType type;
    private Instant createdAt;
    private Instant updatedAt;

    protected CastMember(
    final CastMemberID id,
    final String name,
    final CastMemberType type,
    final Instant creationDate,
    final Instant updateDate
    ) {
        super(id);
        this.name = name;
        this.type = type;
        this.createdAt = creationDate;
        this.updatedAt = updateDate;
        selfValidate();
    }

    public static CastMember newMember(final String name, final CastMemberType type) {
        final var id = CastMemberID.unique();
        final var now = InstantUtils.now();
        return new CastMember(id, name, type, now, now);
    }

    public static CastMember with(
    final CastMemberID id,
    final String name,
    final CastMemberType type,
    final Instant creationDate,
    final Instant updateDate
    ) {
        return new CastMember(id, name, type, creationDate, updateDate);
    }

    public static CastMember with(final CastMember member) {
        return new CastMember(
                member.id,
                member.name,
                member.type,
                member.createdAt,
                member.updatedAt
        );
    }

    public CastMember update(final String name, final CastMemberType type) {
        this.name = name;
        this.type = type;
        this.updatedAt = InstantUtils.now();
        selfValidate();
        return this;
    }

    @Override
    public void validate(final ValidationHandler aHandler) {
        new CastMemberValidator(this, aHandler).validate();
    }

    public String getName() {
        return name;
    }

    public CastMemberType getType() {
        return type;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    private void selfValidate() {
        final var notification = Notification.create();
        validate(notification);

        if (notification.hasError()) {
            throw new NotificationException("Failed to create a Aggregate CastMember", notification);
        }
    }
}
