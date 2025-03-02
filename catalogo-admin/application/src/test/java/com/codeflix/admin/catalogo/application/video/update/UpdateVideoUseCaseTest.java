package com.codeflix.admin.catalogo.application.video.update;

import com.codeflix.admin.catalogo.application.UseCaseTest;
import com.codeflix.admin.catalogo.domain.Fixture;
import com.codeflix.admin.catalogo.domain.castmember.CastMemberGateway;
import com.codeflix.admin.catalogo.domain.castmember.CastMemberID;
import com.codeflix.admin.catalogo.domain.category.CategoryGateway;
import com.codeflix.admin.catalogo.domain.category.CategoryID;
import com.codeflix.admin.catalogo.domain.exceptions.DomainException;
import com.codeflix.admin.catalogo.domain.exceptions.InternalErrorException;
import com.codeflix.admin.catalogo.domain.exceptions.NotificationException;
import com.codeflix.admin.catalogo.domain.genre.GenreGateway;
import com.codeflix.admin.catalogo.domain.genre.GenreID;
import com.codeflix.admin.catalogo.domain.video.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.time.Year;
import java.util.*;

public class UpdateVideoUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultUpdateVideoUseCase useCase;

    @Mock
    private VideoGateway videoGateway;

    @Mock
    private CategoryGateway categoryGateway;

    @Mock
    private GenreGateway genreGateway;

    @Mock
    private CastMemberGateway castMemberGateway;

    @Mock
    private MediaResourceGateway mediaResourceGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(videoGateway, categoryGateway, genreGateway, castMemberGateway, mediaResourceGateway);
    }

    @Test
    public void givenValidCommand_whenUpdateVideo_shouldReturnVideoId() {
        final var video = Fixture.Videos.systemDesign();

        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(Fixture.Categories.aulas().getId());
        final var expectedGenres = Set.of(Fixture.Genres.tech().getId());
        final var expectedMembers = Set.of(
                Fixture.CastMembers.lucas().getId(),
                Fixture.CastMembers.geo().getId()
        );
        final Resource expectedVideo = Fixture.Videos.resource(VideoMediaType.VIDEO);
        final Resource expectedTrailer = Fixture.Videos.resource(VideoMediaType.TRAILER);
        final Resource expectedBanner = Fixture.Videos.resource(VideoMediaType.BANNER);
        final Resource expectedThumb = Fixture.Videos.resource(VideoMediaType.THUMBNAIL);
        final Resource expectedThumbHalf = Fixture.Videos.resource(VideoMediaType.THUMBNAIL_HALF);

        final var aCommand = UpdateVideoCommand.with(
                video.getId().getValue(),
                expectedTitle,
                expectedDescription,
                expectedLaunchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf
        );

        Mockito.when(videoGateway.findById(Mockito.any()))
                .thenReturn(Optional.of(Video.with(video)));

        Mockito.when(categoryGateway.existsByIds(Mockito.any()))
                .thenReturn(new ArrayList<>(expectedCategories));

        Mockito.when(castMemberGateway.existsByIds(Mockito.any()))
                .thenReturn(new ArrayList<>(expectedMembers));

        Mockito.when(genreGateway.existsByIds(Mockito.any()))
                .thenReturn(new ArrayList<>(expectedGenres));

        mockImageMedia();
        mockAudioVideoMedia();

        Mockito.when(videoGateway.update(Mockito.any()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());

        final var actualResult = useCase.execute(aCommand);

        Assertions.assertNotNull(actualResult);
        Assertions.assertNotNull(actualResult.id());

        Mockito.verify(videoGateway).findById(Mockito.eq(video.getId()));

        Mockito.verify(videoGateway).update(Mockito.argThat(actualVideo ->
                Objects.equals(expectedTitle, actualVideo.getTitle())
                        && Objects.equals(expectedDescription, actualVideo.getDescription())
                        && Objects.equals(expectedLaunchYear, actualVideo.getLaunchedAt())
                        && Objects.equals(expectedDuration, actualVideo.getDuration())
                        && Objects.equals(expectedOpened, actualVideo.getOpened())
                        && Objects.equals(expectedPublished, actualVideo.getPublished())
                        && Objects.equals(expectedRating, actualVideo.getRating())
                        && Objects.equals(expectedCategories, actualVideo.getCategories())
                        && Objects.equals(expectedGenres, actualVideo.getGenres())
                        && Objects.equals(expectedMembers, actualVideo.getCastMembers())
                        && Objects.equals(expectedVideo.name(), actualVideo.getVideo().get().name())
                        && Objects.equals(expectedTrailer.name(), actualVideo.getTrailer().get().name())
                        && Objects.equals(expectedBanner.name(), actualVideo.getBanner().get().name())
                        && Objects.equals(expectedThumb.name(), actualVideo.getThumbnail().get().name())
                        && Objects.equals(expectedThumbHalf.name(), actualVideo.getThumbnailHalf().get().name())
                        && Objects.equals(video.getCreatedAt(), actualVideo.getCreatedAt())
                        && video.getUpdatedAt().isBefore(actualVideo.getUpdatedAt())
        ));
    }

    @Test
    public void givenValidCommandWithoutCategories_whenUpdateVideo_shouldReturnVideoId() {
        final var video = Fixture.Videos.systemDesign();

        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.of(Fixture.Genres.tech().getId());
        final var expectedMembers = Set.of(
                Fixture.CastMembers.lucas().getId(),
                Fixture.CastMembers.geo().getId()
        );
        final Resource expectedVideo = Fixture.Videos.resource(VideoMediaType.VIDEO);
        final Resource expectedTrailer = Fixture.Videos.resource(VideoMediaType.TRAILER);
        final Resource expectedBanner = Fixture.Videos.resource(VideoMediaType.BANNER);
        final Resource expectedThumb = Fixture.Videos.resource(VideoMediaType.THUMBNAIL);
        final Resource expectedThumbHalf = Fixture.Videos.resource(VideoMediaType.THUMBNAIL_HALF);

        final var aCommand = UpdateVideoCommand.with(
                video.getId().getValue(),
                expectedTitle,
                expectedDescription,
                expectedLaunchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf
        );

        Mockito.when(videoGateway.findById(Mockito.any()))
                .thenReturn(Optional.of(Video.with(video)));

        Mockito.when(castMemberGateway.existsByIds(Mockito.any()))
                .thenReturn(new ArrayList<>(expectedMembers));

        Mockito.when(genreGateway.existsByIds(Mockito.any()))
                .thenReturn(new ArrayList<>(expectedGenres));

        mockImageMedia();
        mockAudioVideoMedia();

        Mockito.when(videoGateway.update(Mockito.any()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());

        final var actualResult = useCase.execute(aCommand);

        Assertions.assertNotNull(actualResult);
        Assertions.assertNotNull(actualResult.id());

        Mockito.verify(videoGateway).findById(Mockito.eq(video.getId()));

        Mockito.verify(videoGateway).update(Mockito.argThat(actualVideo ->
                Objects.equals(expectedTitle, actualVideo.getTitle())
                        && Objects.equals(expectedDescription, actualVideo.getDescription())
                        && Objects.equals(expectedLaunchYear, actualVideo.getLaunchedAt())
                        && Objects.equals(expectedDuration, actualVideo.getDuration())
                        && Objects.equals(expectedOpened, actualVideo.getOpened())
                        && Objects.equals(expectedPublished, actualVideo.getPublished())
                        && Objects.equals(expectedRating, actualVideo.getRating())
                        && Objects.equals(expectedCategories, actualVideo.getCategories())
                        && Objects.equals(expectedGenres, actualVideo.getGenres())
                        && Objects.equals(expectedMembers, actualVideo.getCastMembers())
                        && Objects.equals(expectedVideo.name(), actualVideo.getVideo().get().name())
                        && Objects.equals(expectedTrailer.name(), actualVideo.getTrailer().get().name())
                        && Objects.equals(expectedBanner.name(), actualVideo.getBanner().get().name())
                        && Objects.equals(expectedThumb.name(), actualVideo.getThumbnail().get().name())
                        && Objects.equals(expectedThumbHalf.name(), actualVideo.getThumbnailHalf().get().name())
                        && Objects.equals(video.getCreatedAt(), actualVideo.getCreatedAt())
                        && video.getUpdatedAt().isBefore(actualVideo.getUpdatedAt())
        ));
    }

    @Test
    public void givenValidCommandWithoutGenres_whenUpdateVideo_shouldReturnVideoId() {
        final var video = Fixture.Videos.systemDesign();

        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(Fixture.Categories.aulas().getId());
        final var expectedGenres = Set.<GenreID>of();
        final var expectedMembers = Set.of(
                Fixture.CastMembers.lucas().getId(),
                Fixture.CastMembers.geo().getId()
        );
        final Resource expectedVideo = Fixture.Videos.resource(VideoMediaType.VIDEO);
        final Resource expectedTrailer = Fixture.Videos.resource(VideoMediaType.TRAILER);
        final Resource expectedBanner = Fixture.Videos.resource(VideoMediaType.BANNER);
        final Resource expectedThumb = Fixture.Videos.resource(VideoMediaType.THUMBNAIL);
        final Resource expectedThumbHalf = Fixture.Videos.resource(VideoMediaType.THUMBNAIL_HALF);

        final var aCommand = UpdateVideoCommand.with(
                video.getId().getValue(),
                expectedTitle,
                expectedDescription,
                expectedLaunchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf
        );

        Mockito.when(videoGateway.findById(Mockito.any()))
                .thenReturn(Optional.of(Video.with(video)));

        Mockito.when(categoryGateway.existsByIds(Mockito.any()))
                .thenReturn(new ArrayList<>(expectedCategories));

        Mockito.when(castMemberGateway.existsByIds(Mockito.any()))
                .thenReturn(new ArrayList<>(expectedMembers));

        mockImageMedia();
        mockAudioVideoMedia();

        Mockito.when(videoGateway.update(Mockito.any()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());

        final var actualResult = useCase.execute(aCommand);

        Assertions.assertNotNull(actualResult);
        Assertions.assertNotNull(actualResult.id());

        Mockito.verify(videoGateway).findById(Mockito.eq(video.getId()));

        Mockito.verify(videoGateway).update(Mockito.argThat(actualVideo ->
                Objects.equals(expectedTitle, actualVideo.getTitle())
                        && Objects.equals(expectedDescription, actualVideo.getDescription())
                        && Objects.equals(expectedLaunchYear, actualVideo.getLaunchedAt())
                        && Objects.equals(expectedDuration, actualVideo.getDuration())
                        && Objects.equals(expectedOpened, actualVideo.getOpened())
                        && Objects.equals(expectedPublished, actualVideo.getPublished())
                        && Objects.equals(expectedRating, actualVideo.getRating())
                        && Objects.equals(expectedCategories, actualVideo.getCategories())
                        && Objects.equals(expectedGenres, actualVideo.getGenres())
                        && Objects.equals(expectedMembers, actualVideo.getCastMembers())
                        && Objects.equals(expectedVideo.name(), actualVideo.getVideo().get().name())
                        && Objects.equals(expectedTrailer.name(), actualVideo.getTrailer().get().name())
                        && Objects.equals(expectedBanner.name(), actualVideo.getBanner().get().name())
                        && Objects.equals(expectedThumb.name(), actualVideo.getThumbnail().get().name())
                        && Objects.equals(expectedThumbHalf.name(), actualVideo.getThumbnailHalf().get().name())
                        && Objects.equals(video.getCreatedAt(), actualVideo.getCreatedAt())
                        && video.getUpdatedAt().isBefore(actualVideo.getUpdatedAt())
        ));
    }

    @Test
    public void givenValidCommandWithoutCastMembers_whenUpdateVideo_shouldReturnVideoId() {
        final var video = Fixture.Videos.systemDesign();

        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(Fixture.Categories.aulas().getId());
        final var expectedGenres = Set.of(Fixture.Genres.tech().getId());
        final var expectedMembers = Set.<CastMemberID>of();
        final Resource expectedVideo = Fixture.Videos.resource(VideoMediaType.VIDEO);
        final Resource expectedTrailer = Fixture.Videos.resource(VideoMediaType.TRAILER);
        final Resource expectedBanner = Fixture.Videos.resource(VideoMediaType.BANNER);
        final Resource expectedThumb = Fixture.Videos.resource(VideoMediaType.THUMBNAIL);
        final Resource expectedThumbHalf = Fixture.Videos.resource(VideoMediaType.THUMBNAIL_HALF);

        final var aCommand = UpdateVideoCommand.with(
                video.getId().getValue(),
                expectedTitle,
                expectedDescription,
                expectedLaunchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf
        );

        Mockito.when(videoGateway.findById(Mockito.any()))
                .thenReturn(Optional.of(Video.with(video)));

        Mockito.when(categoryGateway.existsByIds(Mockito.any()))
                .thenReturn(new ArrayList<>(expectedCategories));

        Mockito.when(genreGateway.existsByIds(Mockito.any()))
                .thenReturn(new ArrayList<>(expectedGenres));

        mockImageMedia();
        mockAudioVideoMedia();

        Mockito.when(videoGateway.update(Mockito.any()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());

        final var actualResult = useCase.execute(aCommand);

        Assertions.assertNotNull(actualResult);
        Assertions.assertNotNull(actualResult.id());

        Mockito.verify(videoGateway).findById(Mockito.eq(video.getId()));

        Mockito.verify(videoGateway).update(Mockito.argThat(actualVideo ->
                Objects.equals(expectedTitle, actualVideo.getTitle())
                        && Objects.equals(expectedDescription, actualVideo.getDescription())
                        && Objects.equals(expectedLaunchYear, actualVideo.getLaunchedAt())
                        && Objects.equals(expectedDuration, actualVideo.getDuration())
                        && Objects.equals(expectedOpened, actualVideo.getOpened())
                        && Objects.equals(expectedPublished, actualVideo.getPublished())
                        && Objects.equals(expectedRating, actualVideo.getRating())
                        && Objects.equals(expectedCategories, actualVideo.getCategories())
                        && Objects.equals(expectedGenres, actualVideo.getGenres())
                        && Objects.equals(expectedMembers, actualVideo.getCastMembers())
                        && Objects.equals(expectedVideo.name(), actualVideo.getVideo().get().name())
                        && Objects.equals(expectedTrailer.name(), actualVideo.getTrailer().get().name())
                        && Objects.equals(expectedBanner.name(), actualVideo.getBanner().get().name())
                        && Objects.equals(expectedThumb.name(), actualVideo.getThumbnail().get().name())
                        && Objects.equals(expectedThumbHalf.name(), actualVideo.getThumbnailHalf().get().name())
                        && Objects.equals(video.getCreatedAt(), actualVideo.getCreatedAt())
                        && video.getUpdatedAt().isBefore(actualVideo.getUpdatedAt())
        ));
    }

    @Test
    public void givenValidCommandWithoutResources_whenUpdateVideo_shouldReturnVideoId() {
        final var video = Fixture.Videos.systemDesign();

        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(Fixture.Categories.aulas().getId());
        final var expectedGenres = Set.of(Fixture.Genres.tech().getId());
        final var expectedMembers = Set.of(
                Fixture.CastMembers.lucas().getId(),
                Fixture.CastMembers.geo().getId()
        );
        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumb = null;
        final Resource expectedThumbHalf = null;

        final var aCommand = UpdateVideoCommand.with(
                video.getId().getValue(),
                expectedTitle,
                expectedDescription,
                expectedLaunchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf
        );

        Mockito.when(videoGateway.findById(Mockito.any()))
                .thenReturn(Optional.of(Video.with(video)));

        Mockito.when(categoryGateway.existsByIds(Mockito.any()))
                .thenReturn(new ArrayList<>(expectedCategories));

        Mockito.when(castMemberGateway.existsByIds(Mockito.any()))
                .thenReturn(new ArrayList<>(expectedMembers));

        Mockito.when(genreGateway.existsByIds(Mockito.any()))
                .thenReturn(new ArrayList<>(expectedGenres));

        Mockito.when(videoGateway.update(Mockito.any()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());

        final var actualResult = useCase.execute(aCommand);

        Assertions.assertNotNull(actualResult);
        Assertions.assertNotNull(actualResult.id());

        Mockito.verify(videoGateway).findById(Mockito.eq(video.getId()));

        Mockito.verify(videoGateway).update(Mockito.argThat(actualVideo ->
                Objects.equals(expectedTitle, actualVideo.getTitle())
                        && Objects.equals(expectedDescription, actualVideo.getDescription())
                        && Objects.equals(expectedLaunchYear, actualVideo.getLaunchedAt())
                        && Objects.equals(expectedDuration, actualVideo.getDuration())
                        && Objects.equals(expectedOpened, actualVideo.getOpened())
                        && Objects.equals(expectedPublished, actualVideo.getPublished())
                        && Objects.equals(expectedRating, actualVideo.getRating())
                        && Objects.equals(expectedCategories, actualVideo.getCategories())
                        && Objects.equals(expectedGenres, actualVideo.getGenres())
                        && Objects.equals(expectedMembers, actualVideo.getCastMembers())
                        && actualVideo.getVideo().isEmpty()
                        && actualVideo.getTrailer().isEmpty()
                        && actualVideo.getBanner().isEmpty()
                        && actualVideo.getThumbnail().isEmpty()
                        && actualVideo.getThumbnailHalf().isEmpty()
                        && Objects.equals(video.getCreatedAt(), actualVideo.getCreatedAt())
                        && video.getUpdatedAt().isBefore(actualVideo.getUpdatedAt())
        ));
    }

    @Test
    public void givenNullTitle_whenUpdateVideo_shouldReturnDomainException() {
        final var video = Fixture.Videos.systemDesign();

        final var expectedErrorMessage = "'title' should not be null";
        final var expectedErrorCount = 1;

        final String expectedTitle = null;
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.<GenreID>of();
        final var expectedMembers = Set.<CastMemberID>of();
        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumb = null;
        final Resource expectedThumbHalf = null;

        final var aCommand = UpdateVideoCommand.with(
                video.getId().getValue(),
                expectedTitle,
                expectedDescription,
                expectedLaunchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf
        );

        Mockito.when(videoGateway.findById(Mockito.any()))
                .thenReturn(Optional.of(Video.with(video)));

        final var actualException = Assertions.assertThrows(DomainException.class, () -> {
            useCase.execute(aCommand);
        });

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        Mockito.verify(videoGateway).findById(Mockito.eq(video.getId()));

        Mockito.verify(categoryGateway, Mockito.times(0)).existsByIds(Mockito.any());
        Mockito.verify(castMemberGateway, Mockito.times(0)).existsByIds(Mockito.any());
        Mockito.verify(genreGateway, Mockito.times(0)).existsByIds(Mockito.any());
        Mockito.verify(mediaResourceGateway, Mockito.times(0)).storeAudioVideo(Mockito.any(), Mockito.any());
        Mockito.verify(videoGateway, Mockito.times(0)).update(Mockito.any());
    }

    @Test
    public void givenEmptyTitle_whenUpdateVideo_shouldReturnDomainException() {
        final var video = Fixture.Videos.systemDesign();

        final var expectedErrorMessage = "'title' should not be empty";
        final var expectedErrorCount = 1;

        final String expectedTitle = " ";
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.<GenreID>of();
        final var expectedMembers = Set.<CastMemberID>of();
        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumb = null;
        final Resource expectedThumbHalf = null;

        final var aCommand = UpdateVideoCommand.with(
                video.getId().getValue(),
                expectedTitle,
                expectedDescription,
                expectedLaunchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf
        );

        Mockito.when(videoGateway.findById(Mockito.any()))
                .thenReturn(Optional.of(Video.with(video)));

        final var actualException = Assertions.assertThrows(DomainException.class, () -> {
            useCase.execute(aCommand);
        });

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        Mockito.verify(videoGateway).findById(Mockito.eq(video.getId()));

        Mockito.verify(categoryGateway, Mockito.times(0)).existsByIds(Mockito.any());
        Mockito.verify(castMemberGateway, Mockito.times(0)).existsByIds(Mockito.any());
        Mockito.verify(genreGateway, Mockito.times(0)).existsByIds(Mockito.any());
        Mockito.verify(mediaResourceGateway, Mockito.times(0)).storeAudioVideo(Mockito.any(), Mockito.any());
        Mockito.verify(videoGateway, Mockito.times(0)).update(Mockito.any());
    }

    @Test
    public void givenNullRating_whenUpdateVideo_shouldReturnDomainException() {
        final var video = Fixture.Videos.systemDesign();

        final var expectedErrorMessage = "'rating' should not be null";
        final var expectedErrorCount = 1;

        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final String expectedRating = null;
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.<GenreID>of();
        final var expectedMembers = Set.<CastMemberID>of();
        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumb = null;
        final Resource expectedThumbHalf = null;

        final var aCommand = UpdateVideoCommand.with(
                video.getId().getValue(),
                expectedTitle,
                expectedDescription,
                expectedLaunchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating,
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf
        );

        Mockito.when(videoGateway.findById(Mockito.any()))
                .thenReturn(Optional.of(Video.with(video)));

        final var actualException = Assertions.assertThrows(DomainException.class, () -> {
            useCase.execute(aCommand);
        });

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        Mockito.verify(videoGateway).findById(Mockito.eq(video.getId()));

        Mockito.verify(categoryGateway, Mockito.times(0)).existsByIds(Mockito.any());
        Mockito.verify(castMemberGateway, Mockito.times(0)).existsByIds(Mockito.any());
        Mockito.verify(genreGateway, Mockito.times(0)).existsByIds(Mockito.any());
        Mockito.verify(mediaResourceGateway, Mockito.times(0)).storeAudioVideo(Mockito.any(), Mockito.any());
        Mockito.verify(videoGateway, Mockito.times(0)).update(Mockito.any());
    }

    @Test
    public void givenInvalidRating_whenUpdateVideo_shouldReturnDomainException() {
        final var video = Fixture.Videos.systemDesign();

        final var expectedErrorMessage = "'rating' should not be null";
        final var expectedErrorCount = 1;

        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final String expectedRating = "ADASDA";
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.<GenreID>of();
        final var expectedMembers = Set.<CastMemberID>of();
        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumb = null;
        final Resource expectedThumbHalf = null;

        final var aCommand = UpdateVideoCommand.with(
                video.getId().getValue(),
                expectedTitle,
                expectedDescription,
                expectedLaunchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating,
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf
        );

        Mockito.when(videoGateway.findById(Mockito.any()))
                .thenReturn(Optional.of(Video.with(video)));

        final var actualException = Assertions.assertThrows(DomainException.class, () -> {
            useCase.execute(aCommand);
        });

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        Mockito.verify(videoGateway).findById(Mockito.eq(video.getId()));

        Mockito.verify(categoryGateway, Mockito.times(0)).existsByIds(Mockito.any());
        Mockito.verify(castMemberGateway, Mockito.times(0)).existsByIds(Mockito.any());
        Mockito.verify(genreGateway, Mockito.times(0)).existsByIds(Mockito.any());
        Mockito.verify(mediaResourceGateway, Mockito.times(0)).storeAudioVideo(Mockito.any(), Mockito.any());
        Mockito.verify(videoGateway, Mockito.times(0)).update(Mockito.any());
    }

    @Test
    public void givenNullLaunchedAt_whenUpdateVideo_shouldReturnDomainException() {
        final var video = Video.newVideo(
                "System Design no Mercado Livre na prática",
                """
                        lorem ipsum dolor sit amet consectetur adipiscing elit lacus cursus montes dictum duis class est
                        ullamcorper placerat senectus vivamus diam auctor magnis condimentum turpis egestas massa maecenas
                        """,
                Year.of(2022),
                Fixture.duration(),
                Fixture.bool(),
                Fixture.bool(),
                Rating.L,
                Set.of(Fixture.Categories.aulas().getId()),
                Set.of(Fixture.Genres.tech().getId()),
                Set.of(Fixture.CastMembers.lucas().getId(), Fixture.CastMembers.geo().getId())
        );


        final var expectedErrorMessage = "'launchedAt' should not be null";
        final var expectedErrorCount = 1;

        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final Integer expectedLaunchYear = null;
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.<GenreID>of();
        final var expectedMembers = Set.<CastMemberID>of();
        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumb = null;
        final Resource expectedThumbHalf = null;

        final var aCommand = UpdateVideoCommand.with(
                video.getId().getValue(),
                expectedTitle,
                expectedDescription,
                expectedLaunchYear,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf
        );

        Mockito.when(videoGateway.findById(Mockito.any()))
                .thenReturn(Optional.of(Video.with(video)));

        final var actualException = Assertions.assertThrows(DomainException.class, () -> {
            useCase.execute(aCommand);
        });

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        Mockito.verify(videoGateway).findById(Mockito.eq(video.getId()));

        Mockito.verify(categoryGateway, Mockito.times(0)).existsByIds(Mockito.any());
        Mockito.verify(castMemberGateway, Mockito.times(0)).existsByIds(Mockito.any());
        Mockito.verify(genreGateway, Mockito.times(0)).existsByIds(Mockito.any());
        Mockito.verify(mediaResourceGateway, Mockito.times(0)).storeAudioVideo(Mockito.any(), Mockito.any());
        Mockito.verify(videoGateway, Mockito.times(0)).update(Mockito.any());
    }

    @Test
    public void givenValidCommand_whenUpdateVideoAndSomeCategoriesDoesNotExists_shouldReturnDomainException() {
        final var video = Fixture.Videos.systemDesign();
        final var aulasId = Fixture.Categories.aulas().getId();

        final var expectedErrorMessage = "Some categories could not be found: %s".formatted(aulasId.getValue());
        final var expectedErrorCount = 1;

        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Fixture.year();
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(aulasId);
        final var expectedGenres = Set.of(Fixture.Genres.tech().getId());
        final var expectedMembers = Set.of(Fixture.CastMembers.lucas().getId());
        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumb = null;
        final Resource expectedThumbHalf = null;

        final var aCommand = UpdateVideoCommand.with(
                video.getId().getValue(),
                expectedTitle,
                expectedDescription,
                expectedLaunchYear,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf
        );

        Mockito.when(videoGateway.findById(Mockito.any()))
                .thenReturn(Optional.of(Video.with(video)));

        Mockito.when(categoryGateway.existsByIds(Mockito.any()))
                .thenReturn(new ArrayList<>());

        Mockito.when(castMemberGateway.existsByIds(Mockito.any()))
                .thenReturn(new ArrayList<>(expectedMembers));

        Mockito.when(genreGateway.existsByIds(Mockito.any()))
                .thenReturn(new ArrayList<>(expectedGenres));

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            useCase.execute(aCommand);
        });

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        Mockito.verify(categoryGateway, Mockito.times(1)).existsByIds(Mockito.eq(expectedCategories));
        Mockito.verify(castMemberGateway, Mockito.times(1)).existsByIds(Mockito.eq(expectedMembers));
        Mockito.verify(genreGateway, Mockito.times(1)).existsByIds(Mockito.eq(expectedGenres));
        Mockito.verify(mediaResourceGateway, Mockito.times(0)).storeImage(Mockito.any(), Mockito.any());
        Mockito.verify(videoGateway, Mockito.times(0)).update(Mockito.any());
    }

    @Test
    public void givenValidCommand_whenUpdateVideoAndSomeGenresDoesNotExists_shouldReturnDomainException() {
        final var video = Fixture.Videos.systemDesign();
        final var techId = Fixture.Genres.tech().getId();

        final var expectedErrorMessage = "Some genres could not be found: %s".formatted(techId.getValue());
        final var expectedErrorCount = 1;

        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Fixture.year();
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(Fixture.Categories.aulas().getId());
        final var expectedGenres = Set.of(techId);
        final var expectedMembers = Set.of(Fixture.CastMembers.lucas().getId());
        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumb = null;
        final Resource expectedThumbHalf = null;

        final var aCommand = UpdateVideoCommand.with(
                video.getId().getValue(),
                expectedTitle,
                expectedDescription,
                expectedLaunchYear,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf
        );

        Mockito.when(videoGateway.findById(Mockito.any()))
                .thenReturn(Optional.of(Video.with(video)));

        Mockito.when(categoryGateway.existsByIds(Mockito.any()))
                .thenReturn(new ArrayList<>(expectedCategories));

        Mockito.when(castMemberGateway.existsByIds(Mockito.any()))
                .thenReturn(new ArrayList<>(expectedMembers));

        Mockito.when(genreGateway.existsByIds(Mockito.any()))
                .thenReturn(new ArrayList<>());

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            useCase.execute(aCommand);
        });

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        Mockito.verify(categoryGateway, Mockito.times(1)).existsByIds(Mockito.eq(expectedCategories));
        Mockito.verify(castMemberGateway, Mockito.times(1)).existsByIds(Mockito.eq(expectedMembers));
        Mockito.verify(genreGateway, Mockito.times(1)).existsByIds(Mockito.eq(expectedGenres));
        Mockito.verify(mediaResourceGateway, Mockito.times(0)).storeImage(Mockito.any(), Mockito.any());
        Mockito.verify(videoGateway, Mockito.times(0)).create(Mockito.any());
    }

    @Test
    public void givenValidCommand_whenUpdateVideoAndSomeCastMembersDoesNotExists_shouldReturnDomainException() {
        final var video = Fixture.Videos.systemDesign();
        final var lucasId = Fixture.CastMembers.lucas().getId();

        final var expectedErrorMessage = "Some cast members could not be found: %s".formatted(lucasId.getValue());
        final var expectedErrorCount = 1;

        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Fixture.year();
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(Fixture.Categories.aulas().getId());
        final var expectedGenres = Set.of(Fixture.Genres.tech().getId());
        final var expectedMembers = Set.of(lucasId);
        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumb = null;
        final Resource expectedThumbHalf = null;

        final var aCommand = UpdateVideoCommand.with(
                video.getId().getValue(),
                expectedTitle,
                expectedDescription,
                expectedLaunchYear,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf
        );

        Mockito.when(videoGateway.findById(Mockito.any()))
                .thenReturn(Optional.of(Video.with(video)));

        Mockito.when(categoryGateway.existsByIds(Mockito.any()))
                .thenReturn(new ArrayList<>(expectedCategories));

        Mockito.when(castMemberGateway.existsByIds(Mockito.any()))
                .thenReturn(new ArrayList<>());

        Mockito.when(genreGateway.existsByIds(Mockito.any()))
                .thenReturn(new ArrayList<>(expectedGenres));

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            useCase.execute(aCommand);
        });

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        Mockito.verify(categoryGateway, Mockito.times(1)).existsByIds(Mockito.eq(expectedCategories));
        Mockito.verify(castMemberGateway, Mockito.times(1)).existsByIds(Mockito.eq(expectedMembers));
        Mockito.verify(genreGateway, Mockito.times(1)).existsByIds(Mockito.eq(expectedGenres));
        Mockito.verify(mediaResourceGateway, Mockito.times(0)).storeImage(Mockito.any(), Mockito.any());
        Mockito.verify(videoGateway, Mockito.times(0)).create(Mockito.any());
    }

    @Test
    public void givenValidCommand_whenCreateVideoThrowsException_shouldCallClearResources() {
        final var video = Fixture.Videos.systemDesign();
        final var expectedErrorMessage = "An error on create video was observed [videoId:";

        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(Fixture.Categories.aulas().getId());
        final var expectedGenres = Set.of(Fixture.Genres.tech().getId());
        final var expectedMembers = Set.of(
                Fixture.CastMembers.lucas().getId(),
                Fixture.CastMembers.geo().getId()
        );
        final Resource expectedVideo = Fixture.Videos.resource(VideoMediaType.VIDEO);
        final Resource expectedTrailer = Fixture.Videos.resource(VideoMediaType.TRAILER);
        final Resource expectedBanner = Fixture.Videos.resource(VideoMediaType.BANNER);
        final Resource expectedThumb = Fixture.Videos.resource(VideoMediaType.THUMBNAIL);
        final Resource expectedThumbHalf = Fixture.Videos.resource(VideoMediaType.THUMBNAIL_HALF);

        final var aCommand = UpdateVideoCommand.with(
                video.getId().getValue(),
                expectedTitle,
                expectedDescription,
                expectedLaunchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf
        );

        Mockito.when(videoGateway.findById(Mockito.any()))
                .thenReturn(Optional.of(Video.with(video)));

        Mockito.when(categoryGateway.existsByIds(Mockito.any()))
                .thenReturn(new ArrayList<>(expectedCategories));

        Mockito.when(castMemberGateway.existsByIds(Mockito.any()))
                .thenReturn(new ArrayList<>(expectedMembers));

        Mockito.when(genreGateway.existsByIds(Mockito.any()))
                .thenReturn(new ArrayList<>(expectedGenres));

        mockImageMedia();
        mockAudioVideoMedia();

        Mockito.when(videoGateway.update(Mockito.any()))
                .thenThrow(new RuntimeException("Internal Server Error"));

        final var actualResult = Assertions.assertThrows(InternalErrorException.class, () -> {
            useCase.execute(aCommand);
        });

        Assertions.assertNotNull(actualResult);
        Assertions.assertTrue(actualResult.getMessage().startsWith(expectedErrorMessage));

        Mockito.verify(mediaResourceGateway, Mockito.times(0)).clearResources(Mockito.any());
    }

    private void mockImageMedia() {
        Mockito.when(mediaResourceGateway.storeImage(Mockito.any(), Mockito.any())).thenAnswer(t -> {
            final var videoResource = t.getArgument(1, VideoResource.class);
            final var resource = videoResource.resource();
            return ImageMedia.with(resource.checksum(), resource.name(), "/img");
        });
    }

    private void mockAudioVideoMedia() {
        Mockito.when(mediaResourceGateway.storeAudioVideo(Mockito.any(), Mockito.any())).thenAnswer(t -> {
            final var videoResource = t.getArgument(1, VideoResource.class);
            final var resource = videoResource.resource();
            return AudioVideoMedia.with(
                    resource.checksum(),
                    resource.name(),
                    "/img"
            );
        });
    }
}
