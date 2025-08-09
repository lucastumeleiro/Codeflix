package com.codeflix.admin.catalogo.application.video.media.get;

public record GetMediaCommand(
        String videoId,
        String mediaType
) {

    public static GetMediaCommand with(final String id, final String type) {
        return new GetMediaCommand(id, type);
    }
}
