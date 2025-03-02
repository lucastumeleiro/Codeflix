package com.codeflix.admin.catalogo.application.video.update;

import com.codeflix.admin.catalogo.domain.video.Video;

public record UpdateVideoOutput(String id) {

    public static UpdateVideoOutput from(final Video video) {
        return new UpdateVideoOutput(video.getId().getValue());
    }
}
