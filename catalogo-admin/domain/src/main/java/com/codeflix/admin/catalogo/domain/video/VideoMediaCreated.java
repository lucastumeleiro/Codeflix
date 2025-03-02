package com.codeflix.admin.catalogo.domain.video;

import com.codeflix.admin.catalogo.domain.Utils.InstantUtils;

import java.time.Instant;

/*public record VideoMediaCreated(
        String resourceId,
        String filePath,
        Instant occurredOn
) implements DomainEvent {

    public VideoMediaCreated(final String resourceId, final String filePath) {
        this(resourceId, filePath, InstantUtils.now());
    }
}*/

public record VideoMediaCreated(
        String resourceId,
        String filePath,
        Instant occurredOn
) {

    public VideoMediaCreated(final String resourceId, final String filePath) {
        this(resourceId, filePath, InstantUtils.now());
    }
}

