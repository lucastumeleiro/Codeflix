package com.codeflix.admin.catalogo.application.video.retrieve.list;

import com.codeflix.admin.catalogo.domain.pagination.Pagination;
import com.codeflix.admin.catalogo.domain.video.VideoGateway;
import com.codeflix.admin.catalogo.domain.video.VideoSearchQuery;

import java.util.Objects;

public class DefaultListVideosUseCase extends ListVideosUseCase {

    private final VideoGateway videoGateway;

    public DefaultListVideosUseCase(final VideoGateway videoGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Override
    public Pagination<VideoListOutput> execute(final VideoSearchQuery query) {
        return this.videoGateway.findAll(query)
                .map(VideoListOutput::from);
    }
}
