package com.codeflix.admin.catalogo.domain.video;

import com.codeflix.admin.catalogo.domain.pagination.Pagination;

import java.util.Optional;

public interface VideoGateway {

    Video create(Video video);

    void deleteById(VideoID id);

    Optional<Video> findById(VideoID id);

    Video update(Video video);

    Pagination<VideoPreview> findAll(VideoSearchQuery query);

}
