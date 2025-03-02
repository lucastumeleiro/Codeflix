package com.codeflix.admin.catalogo.application.video.delete;

import com.codeflix.admin.catalogo.domain.video.MediaResourceGateway;
import com.codeflix.admin.catalogo.domain.video.VideoGateway;
import com.codeflix.admin.catalogo.domain.video.VideoID;

import java.util.Objects;

public class DefaultDeleteVideoUseCase extends DeleteVideoUseCase {

    private final VideoGateway videoGateway;
    private final MediaResourceGateway mediaResourceGateway;
    public DefaultDeleteVideoUseCase(
            final VideoGateway videoGateway,
            final MediaResourceGateway mediaResourceGateway
    ) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
        this.mediaResourceGateway = Objects.requireNonNull(mediaResourceGateway);
    }

    @Override
    public void execute(final String in) {
        final var videoId = VideoID.from(in);
        this.videoGateway.deleteById(videoId);
        this.mediaResourceGateway.clearResources(videoId);
    }
}
