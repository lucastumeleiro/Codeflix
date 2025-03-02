package com.codeflix.admin.catalogo.application.video.update;

import com.codeflix.admin.catalogo.domain.Identifier;
import com.codeflix.admin.catalogo.domain.castmember.CastMemberGateway;
import com.codeflix.admin.catalogo.domain.castmember.CastMemberID;
import com.codeflix.admin.catalogo.domain.category.CategoryGateway;
import com.codeflix.admin.catalogo.domain.category.CategoryID;
import com.codeflix.admin.catalogo.domain.exceptions.DomainException;
import com.codeflix.admin.catalogo.domain.exceptions.InternalErrorException;
import com.codeflix.admin.catalogo.domain.exceptions.NotFoundException;
import com.codeflix.admin.catalogo.domain.exceptions.NotificationException;
import com.codeflix.admin.catalogo.domain.genre.GenreGateway;
import com.codeflix.admin.catalogo.domain.genre.GenreID;
import com.codeflix.admin.catalogo.domain.validation.Error;
import com.codeflix.admin.catalogo.domain.validation.ValidationHandler;
import com.codeflix.admin.catalogo.domain.validation.handler.Notification;
import com.codeflix.admin.catalogo.domain.video.*;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class DefaultUpdateVideoUseCase extends UpdateVideoUseCase {

    private final VideoGateway videoGateway;
    private final CategoryGateway categoryGateway;
    private final CastMemberGateway castMemberGateway;
    private final GenreGateway genreGateway;
    private final MediaResourceGateway mediaResourceGateway;

    public DefaultUpdateVideoUseCase(
            final VideoGateway videoGateway,
            final CategoryGateway categoryGateway,
            final CastMemberGateway castMemberGateway,
            final GenreGateway genreGateway,
            final MediaResourceGateway mediaResourceGateway
    ) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
        this.genreGateway = Objects.requireNonNull(genreGateway);
        this.mediaResourceGateway = Objects.requireNonNull(mediaResourceGateway);
    }

    @Override
    public UpdateVideoOutput execute(final UpdateVideoCommand aCommand) {
        final var id = VideoID.from(aCommand.id());
        final var rating = Rating.of(aCommand.rating()).orElse(null);
        final var launchYear = aCommand.launchedAt() != null ? Year.of(aCommand.launchedAt()) : null;
        final var categories = toIdentifier(aCommand.categories(), CategoryID::from);
        final var genres = toIdentifier(aCommand.genres(), GenreID::from);
        final var members = toIdentifier(aCommand.members(), CastMemberID::from);

        final var video = this.videoGateway.findById(id)
                .orElseThrow(notFoundException(id));

        final var notification = Notification.create();
        notification.append(validateCategories(categories));
        notification.append(validateGenres(genres));
        notification.append(validateMembers(members));

        video.update(
                aCommand.title(),
                aCommand.description(),
                launchYear,
                aCommand.duration(),
                aCommand.opened(),
                aCommand.published(),
                rating,
                categories,
                genres,
                members
        );

        video.validate(notification);

        if (notification.hasError()) {
            throw new NotificationException("Could not update Aggregate Video", notification);
        }

        return UpdateVideoOutput.from(update(aCommand, video));
    }

    private Video update(final UpdateVideoCommand aCommand, final Video video) {
        final var id = video.getId();

        try {
            final var videoMedia = aCommand.getVideo()
                    .map(it -> this.mediaResourceGateway.storeAudioVideo(id, VideoResource.with(VideoMediaType.VIDEO, it)))
                    .orElse(null);

            final var aTrailerMedia = aCommand.getTrailer()
                    .map(it -> this.mediaResourceGateway.storeAudioVideo(id, VideoResource.with(VideoMediaType.TRAILER, it)))
                    .orElse(null);

            final var aBannerMedia = aCommand.getBanner()
                    .map(it -> this.mediaResourceGateway.storeImage(id, VideoResource.with(VideoMediaType.BANNER, it)))
                    .orElse(null);

            final var aThumbnailMedia = aCommand.getThumbnail()
                    .map(it -> this.mediaResourceGateway.storeImage(id, VideoResource.with(VideoMediaType.THUMBNAIL, it)))
                    .orElse(null);

            final var aThumbHalfMedia = aCommand.getThumbnailHalf()
                    .map(it -> this.mediaResourceGateway.storeImage(id, VideoResource.with(VideoMediaType.THUMBNAIL_HALF, it)))
                    .orElse(null);

            return this.videoGateway.update(
                    video
                            .updateVideoMedia(videoMedia)
                            .updateTrailerMedia(aTrailerMedia)
                            .updateBannerMedia(aBannerMedia)
                            .updateThumbnailMedia(aThumbnailMedia)
                            .updateThumbnailHalfMedia(aThumbHalfMedia)
            );
        } catch (final Throwable throwable) {
            throw InternalErrorException.with(
                    "An error on create video was observed [videoId:%s]".formatted(id.getValue()),
                    throwable
            );
        }
    }

    private Supplier<DomainException> notFoundException(final VideoID id) {
        return () -> NotFoundException.with(Video.class, id);
    }

    private ValidationHandler validateCategories(final Set<CategoryID> ids) {
        return validateAggregate("categories", ids, categoryGateway::existsByIds);
    }

    private ValidationHandler validateGenres(final Set<GenreID> ids) {
        return validateAggregate("genres", ids, genreGateway::existsByIds);
    }

    private ValidationHandler validateMembers(final Set<CastMemberID> ids) {
        return validateAggregate("cast members", ids, castMemberGateway::existsByIds);
    }

    private <T extends Identifier> ValidationHandler validateAggregate(
            final String aggregate,
            final Set<T> ids,
            final Function<Iterable<T>, List<T>> existsByIds
    ) {
        final var notification = Notification.create();
        if (ids == null || ids.isEmpty()) {
            return notification;
        }

        final var retrievedIds = existsByIds.apply(ids);

        if (ids.size() != retrievedIds.size()) {
            final var missingIds = new ArrayList<>(ids);
            missingIds.removeAll(retrievedIds);

            final var missingIdsMessage = missingIds.stream()
                    .map(Identifier::getValue)
                    .collect(Collectors.joining(", "));

            notification.append(new Error("Some %s could not be found: %s".formatted(aggregate, missingIdsMessage)));
        }

        return notification;
    }

    private <T> Set<T> toIdentifier(final Set<String> ids, final Function<String, T> mapper) {
        return ids.stream()
                .map(mapper)
                .collect(Collectors.toSet());
    }
}
