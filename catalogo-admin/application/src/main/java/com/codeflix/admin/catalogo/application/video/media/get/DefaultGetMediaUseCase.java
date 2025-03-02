package com.codeflix.admin.catalogo.application.video.media.get;

import com.codeflix.admin.catalogo.domain.exceptions.NotFoundException;
import com.codeflix.admin.catalogo.domain.video.MediaResourceGateway;
import com.codeflix.admin.catalogo.domain.video.VideoID;
import com.codeflix.admin.catalogo.domain.video.VideoMediaType;
import com.codeflix.admin.catalogo.domain.validation.Error;

import java.util.Objects;

public class DefaultGetMediaUseCase extends GetMediaUseCase {

    private final MediaResourceGateway mediaResourceGateway;

    public DefaultGetMediaUseCase(final MediaResourceGateway mediaResourceGateway) {
        this.mediaResourceGateway = Objects.requireNonNull(mediaResourceGateway);
    }

    @Override
    public MediaOutput execute(final GetMediaCommand command) {
        final var id = VideoID.from(command.videoId());
        final var type = VideoMediaType.of(command.mediaType())
                .orElseThrow(() -> typeNotFound(command.mediaType()));

        final var aResource =
                this.mediaResourceGateway.getResource(id, type)
                        .orElseThrow(() -> notFound(command.videoId(), command.mediaType()));

        return MediaOutput.with(aResource);
    }

    private NotFoundException notFound(final String id, final String type) {
        return NotFoundException.with(new Error("Resource %s not found for video %s".formatted(type, id)));
    }

    private NotFoundException typeNotFound(final String type) {
        return NotFoundException.with(new Error("Media type %s doesn't exists".formatted(type)));
    }
}
