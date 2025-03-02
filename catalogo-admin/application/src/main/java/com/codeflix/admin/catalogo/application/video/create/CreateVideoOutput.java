package com.codeflix.admin.catalogo.application.video.create;

import com.codeflix.admin.catalogo.domain.video.Video;

public record CreateVideoOutput(String id) {

    public static CreateVideoOutput from(final Video video) {
        return new CreateVideoOutput(video.getId().getValue());
    }
}
