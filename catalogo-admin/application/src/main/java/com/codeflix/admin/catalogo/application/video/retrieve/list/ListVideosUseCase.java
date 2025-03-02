package com.codeflix.admin.catalogo.application.video.retrieve.list;

import com.codeflix.admin.catalogo.application.UseCase;
import com.codeflix.admin.catalogo.domain.pagination.Pagination;
import com.codeflix.admin.catalogo.domain.video.VideoSearchQuery;

public abstract class ListVideosUseCase
        extends UseCase<VideoSearchQuery, Pagination<VideoListOutput>> {
}
