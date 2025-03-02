package com.codeflix.admin.catalogo.domain.video;

import com.codeflix.admin.catalogo.domain.Utils.InstantUtils;
import com.codeflix.admin.catalogo.domain.events.DomainEvent;

import java.time.Instant;

public record VideoMediaCreated(
        String resourceId,
        String filePath,
        Instant occurredOn
) implements DomainEvent {

    public VideoMediaCreated(final String resourceId, final String filePath) {
        this(resourceId, filePath, InstantUtils.now());
    }
}
