package com.codeflix.admin.catalogo.application.video.retrieve.get;

import com.codeflix.admin.catalogo.domain.exceptions.NotFoundException;
import com.codeflix.admin.catalogo.domain.video.Video;
import com.codeflix.admin.catalogo.domain.video.VideoGateway;
import com.codeflix.admin.catalogo.domain.video.VideoID;

import java.util.Objects;

public class DefaultGetVideoUseCase extends GetVideoUseCase {

    private final VideoGateway videoGateway;

    public DefaultGetVideoUseCase(final VideoGateway videoGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Override
    public VideoOutput execute(final String in) {
        final var videoId = VideoID.from(in);
        return this.videoGateway.findById(videoId)
                .map(VideoOutput::from)
                .orElseThrow(() -> NotFoundException.with(Video.class, videoId));
    }
}
