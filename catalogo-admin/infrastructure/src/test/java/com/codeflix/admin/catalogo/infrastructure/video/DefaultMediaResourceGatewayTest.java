package com.codeflix.admin.catalogo.infrastructure.video;

import com.codeflix.admin.catalogo.IntegrationTest;
import com.codeflix.admin.catalogo.domain.Utils.IdUtils;
import com.codeflix.admin.catalogo.domain.video.*;
import com.codeflix.admin.catalogo.infrastructure.services.StorageService;
import com.codeflix.admin.catalogo.infrastructure.services.local.InMemoryStorageService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Random;

import static io.vavr.API.*;
import static io.vavr.API.$;

@IntegrationTest
class DefaultMediaResourceGatewayTest {

    @Autowired
    private MediaResourceGateway mediaResourceGateway;

    @Autowired
    private StorageService storageService;

    @BeforeEach
    public void setUp() {
        storageService().clear();
    }

    @Test
    public void testInjection() {
        Assertions.assertNotNull(mediaResourceGateway);
        Assertions.assertInstanceOf(DefaultMediaResourceGateway.class, mediaResourceGateway);

        Assertions.assertNotNull(storageService);
        Assertions.assertInstanceOf(InMemoryStorageService.class, storageService);
    }

    @Test
    public void givenValidResource_whenCallsStorageAudioVideo_shouldStoreIt() {
        final var expectedVideoId = VideoID.unique();
        final var expectedType = VideoMediaType.VIDEO;
        final var expectedResource = getResource(expectedType);
        final var expectedLocation = "videoId-%s/type-%s".formatted(expectedVideoId.getValue(), expectedType.name());
        final var expectedStatus = MediaStatus.PENDING;
        final var expectedEncodedLocation = "";

        final var actualMedia =
                this.mediaResourceGateway.storeAudioVideo(expectedVideoId, VideoResource.with(expectedType, expectedResource));

        Assertions.assertNotNull(actualMedia.id());
        Assertions.assertEquals(expectedLocation, actualMedia.rawLocation());
        Assertions.assertEquals(expectedResource.name(), actualMedia.name());
        Assertions.assertEquals(expectedResource.checksum(), actualMedia.checksum());
        Assertions.assertEquals(expectedStatus, actualMedia.status());
        Assertions.assertEquals(expectedEncodedLocation, actualMedia.encodedLocation());

        final var actualStored = storageService().storage().get(expectedLocation);

        Assertions.assertEquals(expectedResource, actualStored);
    }

    @Test
    public void givenValidResource_whenCallsStorageImage_shouldStoreIt() {
        final var expectedVideoId = VideoID.unique();
        final var expectedType = VideoMediaType.BANNER;
        final var expectedResource = getResource(expectedType);
        final var expectedLocation = "videoId-%s/type-%s".formatted(expectedVideoId.getValue(), expectedType.name());

        final var actualMedia =
                this.mediaResourceGateway.storeImage(expectedVideoId, VideoResource.with(expectedType, expectedResource));

        Assertions.assertNotNull(actualMedia.id());
        Assertions.assertEquals(expectedLocation, actualMedia.location());
        Assertions.assertEquals(expectedResource.name(), actualMedia.name());
        Assertions.assertEquals(expectedResource.checksum(), actualMedia.checksum());

        final var actualStored = storageService().storage().get(expectedLocation);

        Assertions.assertEquals(expectedResource, actualStored);
    }

    @Test
    public void givenValidVideoId_whenCallsGetResource_shouldReturnIt() {
        final var videoOne = VideoID.unique();
        final var expectedType = VideoMediaType.VIDEO;
        final var expectedResource = getResource(expectedType);

        storageService().store("videoId-%s/type-%s".formatted(videoOne.getValue(), expectedType), expectedResource);
        storageService().store("videoId-%s/type-%s".formatted(videoOne.getValue(), VideoMediaType.TRAILER.name()), getResource(mediaType()));
        storageService().store("videoId-%s/type-%s".formatted(videoOne.getValue(), VideoMediaType.BANNER.name()), getResource(mediaType()));

        Assertions.assertEquals(3, storageService().storage().size());

        final var actualResult = this.mediaResourceGateway.getResource(videoOne, expectedType).get();

        Assertions.assertEquals(expectedResource, actualResult);
    }

    @Test
    public void givenInvalidType_whenCallsGetResource_shouldReturnEmpty() {
        final var videoOne = VideoID.unique();
        final var expectedType = VideoMediaType.THUMBNAIL;

        storageService().store("videoId-%s/type-%s".formatted(videoOne.getValue(), VideoMediaType.VIDEO.name()), getResource(mediaType()));
        storageService().store("videoId-%s/type-%s".formatted(videoOne.getValue(), VideoMediaType.TRAILER.name()), getResource(mediaType()));
        storageService().store("videoId-%s/type-%s".formatted(videoOne.getValue(), VideoMediaType.BANNER.name()), getResource(mediaType()));

        Assertions.assertEquals(3, storageService().storage().size());

        final var actualResult = this.mediaResourceGateway.getResource(videoOne, expectedType);

        Assertions.assertTrue(actualResult.isEmpty());
    }

    @Test
    public void givenValidVideoId_whenCallsClearResources_shouldDeleteAll() {
        final var videoOne = VideoID.unique();
        final var videoTwo = VideoID.unique();

        final var toBeDeleted = new ArrayList<String>();
        toBeDeleted.add("videoId-%s/type-%s".formatted(videoOne.getValue(), VideoMediaType.VIDEO.name()));
        toBeDeleted.add("videoId-%s/type-%s".formatted(videoOne.getValue(), VideoMediaType.TRAILER.name()));
        toBeDeleted.add("videoId-%s/type-%s".formatted(videoOne.getValue(), VideoMediaType.BANNER.name()));

        final var expectedValues = new ArrayList<String>();
        expectedValues.add("videoId-%s/type-%s".formatted(videoTwo.getValue(), VideoMediaType.VIDEO.name()));
        expectedValues.add("videoId-%s/type-%s".formatted(videoTwo.getValue(), VideoMediaType.BANNER.name()));

        toBeDeleted.forEach(id -> storageService().store(id, getResource(mediaType())));
        expectedValues.forEach(id -> storageService().store(id, getResource(mediaType())));

        Assertions.assertEquals(5, storageService().storage().size());

        this.mediaResourceGateway.clearResources(videoOne);

        Assertions.assertEquals(2, storageService().storage().size());

        final var actualKeys = storageService().storage().keySet();

        Assertions.assertTrue(
                expectedValues.size() == actualKeys.size()
                        && actualKeys.containsAll(expectedValues)
        );
    }

    private InMemoryStorageService storageService() {
        return (InMemoryStorageService) storageService;
    }

    private static Resource getResource(final VideoMediaType type) {
        final String contentType = Match(type).of(
                Case($(List(VideoMediaType.VIDEO, VideoMediaType.TRAILER)::contains), "video/mp4"),
                Case($(), "image/jpg")
        );

        final String checksum = IdUtils.uuid();
        final byte[] content = "Conteudo".getBytes();

        return Resource.with(content, checksum, contentType, type.name().toLowerCase());
    }

    private static VideoMediaType mediaType() {
        VideoMediaType[] values = VideoMediaType.values();
        return values[new Random().nextInt(values.length)];
    }
}
