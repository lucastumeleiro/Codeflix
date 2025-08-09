package com.codeflix.admin.catalogo.application.video.media.upload;

import com.codeflix.admin.catalogo.domain.video.Video;
import com.codeflix.admin.catalogo.domain.video.VideoMediaType;

public record UploadMediaOutput(
        String videoId,
        VideoMediaType mediaType
) {

    public static UploadMediaOutput with(final Video video, final VideoMediaType type) {
        return new UploadMediaOutput(video.getId().getValue(), type);
    }
}
