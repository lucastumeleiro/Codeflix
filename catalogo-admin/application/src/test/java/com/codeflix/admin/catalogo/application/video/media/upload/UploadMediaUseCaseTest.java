package com.codeflix.admin.catalogo.application.video.media.upload;

import com.codeflix.admin.catalogo.application.UseCaseTest;
import com.codeflix.admin.catalogo.domain.Fixture;
import com.codeflix.admin.catalogo.domain.exceptions.NotFoundException;
import com.codeflix.admin.catalogo.domain.video.MediaResourceGateway;
import com.codeflix.admin.catalogo.domain.video.VideoGateway;
import com.codeflix.admin.catalogo.domain.video.VideoMediaType;
import com.codeflix.admin.catalogo.domain.video.VideoResource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class UploadMediaUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultUploadMediaUseCase useCase;

    @Mock
    private MediaResourceGateway mediaResourceGateway;

    @Mock
    private VideoGateway videoGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(mediaResourceGateway, videoGateway);
    }

    @Test
    public void givenCmdToUpload_whenIsValid_shouldUpdateVideoMediaAndPersistIt() {
        final var video = Fixture.Videos.systemDesign();
        final var expectedId = video.getId();
        final var expectedType = VideoMediaType.VIDEO;
        final var expectedResource = Fixture.Videos.resource(expectedType);
        final var expectedVideoResource = VideoResource.with(expectedType, expectedResource);
        final var expectedMedia = Fixture.Videos.audioVideo(expectedType);

        Mockito.when(videoGateway.findById(Mockito.any()))
                .thenReturn(Optional.of(video));

        Mockito.when(mediaResourceGateway.storeAudioVideo(Mockito.any(), Mockito.any()))
                .thenReturn(expectedMedia);

        Mockito.when(videoGateway.update(Mockito.any()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());

        final var aCmd = UploadMediaCommand.with(expectedId.getValue(), expectedVideoResource);

        final var actualOutput = useCase.execute(aCmd);

        Assertions.assertEquals(expectedType, actualOutput.mediaType());
        Assertions.assertEquals(expectedId.getValue(), actualOutput.videoId());

        Mockito.verify(videoGateway, Mockito.times(1)).findById(Mockito.eq(expectedId));

        Mockito.verify(mediaResourceGateway, Mockito.times(1)).storeAudioVideo(Mockito.eq(expectedId), Mockito.eq(expectedVideoResource));

        Mockito.verify(videoGateway, Mockito.times(1)).update(Mockito.argThat(actualVideo ->
                Objects.equals(expectedMedia, actualVideo.getVideo().get())
                        && actualVideo.getTrailer().isEmpty()
                        && actualVideo.getBanner().isEmpty()
                        && actualVideo.getThumbnail().isEmpty()
                        && actualVideo.getThumbnailHalf().isEmpty()
        ));
    }

    @Test
    public void givenCmdToUpload_whenIsValid_shouldUpdateTrailerMediaAndPersistIt() {
        final var video = Fixture.Videos.systemDesign();
        final var expectedId = video.getId();
        final var expectedType = VideoMediaType.TRAILER;
        final var expectedResource = Fixture.Videos.resource(expectedType);
        final var expectedVideoResource = VideoResource.with(expectedType, expectedResource);
        final var expectedMedia = Fixture.Videos.audioVideo(expectedType);

        Mockito.when(videoGateway.findById(Mockito.any()))
                .thenReturn(Optional.of(video));

        Mockito.when(mediaResourceGateway.storeAudioVideo(Mockito.any(), Mockito.any()))
                .thenReturn(expectedMedia);

        Mockito.when(videoGateway.update(Mockito.any()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());

        final var aCmd = UploadMediaCommand.with(expectedId.getValue(), expectedVideoResource);

        final var actualOutput = useCase.execute(aCmd);

        Assertions.assertEquals(expectedType, actualOutput.mediaType());
        Assertions.assertEquals(expectedId.getValue(), actualOutput.videoId());

        Mockito.verify(videoGateway, Mockito.times(1)).findById(Mockito.eq(expectedId));

        Mockito.verify(mediaResourceGateway, Mockito.times(1)).storeAudioVideo(Mockito.eq(expectedId), Mockito.eq(expectedVideoResource));

        Mockito.verify(videoGateway, Mockito.times(1)).update(Mockito.argThat(actualVideo ->
                actualVideo.getVideo().isEmpty()
                        && Objects.equals(expectedMedia, actualVideo.getTrailer().get())
                        && actualVideo.getBanner().isEmpty()
                        && actualVideo.getThumbnail().isEmpty()
                        && actualVideo.getThumbnailHalf().isEmpty()
        ));
    }

    @Test
    public void givenCmdToUpload_whenIsValid_shouldUpdateBannerMediaAndPersistIt() {
        final var video = Fixture.Videos.systemDesign();
        final var expectedId = video.getId();
        final var expectedType = VideoMediaType.BANNER;
        final var expectedResource = Fixture.Videos.resource(expectedType);
        final var expectedVideoResource = VideoResource.with(expectedType, expectedResource);
        final var expectedMedia = Fixture.Videos.image(expectedType);

        Mockito.when(videoGateway.findById(Mockito.any()))
                .thenReturn(Optional.of(video));

        Mockito.when(mediaResourceGateway.storeImage(Mockito.any(), Mockito.any()))
                .thenReturn(expectedMedia);

        Mockito.when(videoGateway.update(Mockito.any()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());

        final var aCmd = UploadMediaCommand.with(expectedId.getValue(), expectedVideoResource);

        final var actualOutput = useCase.execute(aCmd);

        Assertions.assertEquals(expectedType, actualOutput.mediaType());
        Assertions.assertEquals(expectedId.getValue(), actualOutput.videoId());

        Mockito.verify(videoGateway, Mockito.times(1)).findById(Mockito.eq(expectedId));

        Mockito.verify(mediaResourceGateway, Mockito.times(1)).storeImage(Mockito.eq(expectedId), Mockito.eq(expectedVideoResource));

        Mockito.verify(videoGateway, Mockito.times(1)).update(Mockito.argThat(actualVideo ->
                actualVideo.getVideo().isEmpty()
                        && actualVideo.getTrailer().isEmpty()
                        && Objects.equals(expectedMedia, actualVideo.getBanner().get())
                        && actualVideo.getThumbnail().isEmpty()
                        && actualVideo.getThumbnailHalf().isEmpty()
        ));
    }

    @Test
    public void givenCmdToUpload_whenIsValid_shouldUpdateThumbnailMediaAndPersistIt() {
        final var video = Fixture.Videos.systemDesign();
        final var expectedId = video.getId();
        final var expectedType = VideoMediaType.THUMBNAIL;
        final var expectedResource = Fixture.Videos.resource(expectedType);
        final var expectedVideoResource = VideoResource.with(expectedType, expectedResource);
        final var expectedMedia = Fixture.Videos.image(expectedType);

        Mockito.when(videoGateway.findById(Mockito.any()))
                .thenReturn(Optional.of(video));

        Mockito.when(mediaResourceGateway.storeImage(Mockito.any(), Mockito.any()))
                .thenReturn(expectedMedia);

        Mockito.when(videoGateway.update(Mockito.any()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());

        final var aCmd = UploadMediaCommand.with(expectedId.getValue(), expectedVideoResource);

        final var actualOutput = useCase.execute(aCmd);

        Assertions.assertEquals(expectedType, actualOutput.mediaType());
        Assertions.assertEquals(expectedId.getValue(), actualOutput.videoId());

        Mockito.verify(videoGateway, Mockito.times(1)).findById(Mockito.eq(expectedId));

        Mockito.verify(mediaResourceGateway, Mockito.times(1)).storeImage(Mockito.eq(expectedId), Mockito.eq(expectedVideoResource));

        Mockito.verify(videoGateway, Mockito.times(1)).update(Mockito.argThat(actualVideo ->
                actualVideo.getVideo().isEmpty()
                        && actualVideo.getTrailer().isEmpty()
                        && actualVideo.getBanner().isEmpty()
                        && Objects.equals(expectedMedia, actualVideo.getThumbnail().get())
                        && actualVideo.getThumbnailHalf().isEmpty()
        ));
    }

    @Test
    public void givenCmdToUpload_whenIsValid_shouldUpdateThumbnailHalfMediaAndPersistIt() {
        final var video = Fixture.Videos.systemDesign();
        final var expectedId = video.getId();
        final var expectedType = VideoMediaType.THUMBNAIL_HALF;
        final var expectedResource = Fixture.Videos.resource(expectedType);
        final var expectedVideoResource = VideoResource.with(expectedType, expectedResource);
        final var expectedMedia = Fixture.Videos.image(expectedType);

        Mockito.when(videoGateway.findById(Mockito.any()))
                .thenReturn(Optional.of(video));

        Mockito.when(mediaResourceGateway.storeImage(Mockito.any(), Mockito.any()))
                .thenReturn(expectedMedia);

        Mockito.when(videoGateway.update(Mockito.any()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());

        final var aCmd = UploadMediaCommand.with(expectedId.getValue(), expectedVideoResource);

        final var actualOutput = useCase.execute(aCmd);

        Assertions.assertEquals(expectedType, actualOutput.mediaType());
        Assertions.assertEquals(expectedId.getValue(), actualOutput.videoId());

        Mockito.verify(videoGateway, Mockito.times(1)).findById(Mockito.eq(expectedId));

        Mockito.verify(mediaResourceGateway, Mockito.times(1)).storeImage(Mockito.eq(expectedId), Mockito.eq(expectedVideoResource));

        Mockito.verify(videoGateway, Mockito.times(1)).update(Mockito.argThat(actualVideo ->
                actualVideo.getVideo().isEmpty()
                        && actualVideo.getTrailer().isEmpty()
                        && actualVideo.getBanner().isEmpty()
                        && actualVideo.getThumbnail().isEmpty()
                        && Objects.equals(expectedMedia, actualVideo.getThumbnailHalf().get())
        ));
    }

    @Test
    public void givenCmdToUpload_whenVideoIsInvalid_shouldReturnNotFound() {
        final var video = Fixture.Videos.systemDesign();
        final var expectedId = video.getId();
        final var expectedType = VideoMediaType.THUMBNAIL_HALF;
        final var expectedResource = Fixture.Videos.resource(expectedType);
        final var expectedVideoResource = VideoResource.with(expectedType, expectedResource);

        final var expectedErrorMessage = "Video with ID %s was not found".formatted(expectedId.getValue());

        Mockito.when(videoGateway.findById(Mockito.any()))
                .thenReturn(Optional.empty());

        final var aCmd = UploadMediaCommand.with(expectedId.getValue(), expectedVideoResource);

        final var actualException = Assertions.assertThrows(
                NotFoundException.class,
                () -> useCase.execute(aCmd)
        );

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }
}
