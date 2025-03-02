package com.codeflix.admin.catalogo.application.video.retrieve.get;

import com.codeflix.admin.catalogo.domain.Identifier;
import com.codeflix.admin.catalogo.domain.Utils.CollectionUtils;
import com.codeflix.admin.catalogo.domain.video.AudioVideoMedia;
import com.codeflix.admin.catalogo.domain.video.ImageMedia;
import com.codeflix.admin.catalogo.domain.video.Rating;
import com.codeflix.admin.catalogo.domain.video.Video;

import java.time.Instant;
import java.util.Set;

public record VideoOutput(
        String id,
        Instant createdAt,
        Instant updatedAt,
        String title,
        String description,
        int launchedAt,
        double duration,
        boolean opened,
        boolean published,
        Rating rating,
        Set<String> categories,
        Set<String> genres,
        Set<String> castMembers,
        ImageMedia banner,
        ImageMedia thumbnail,
        ImageMedia thumbnailHalf,
        AudioVideoMedia video,
        AudioVideoMedia trailer
) {

    public static VideoOutput from(final Video video) {
        return new VideoOutput(
                video.getId().getValue(),
                video.getCreatedAt(),
                video.getUpdatedAt(),
                video.getTitle(),
                video.getDescription(),
                video.getLaunchedAt().getValue(),
                video.getDuration(),
                video.getOpened(),
                video.getPublished(),
                video.getRating(),
                CollectionUtils.mapTo(video.getCategories(), Identifier::getValue),
                CollectionUtils.mapTo(video.getGenres(), Identifier::getValue),
                CollectionUtils.mapTo(video.getCastMembers(), Identifier::getValue),
                video.getBanner().orElse(null),
                video.getThumbnail().orElse(null),
                video.getThumbnailHalf().orElse(null),
                video.getVideo().orElse(null),
                video.getTrailer().orElse(null)
        );
    }
}
