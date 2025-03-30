package com.codeflix.admin.catalogo.infrastructure.services.impl;

import com.codeflix.admin.catalogo.domain.Utils.IdUtils;
import com.codeflix.admin.catalogo.domain.video.Resource;
import com.codeflix.admin.catalogo.domain.video.VideoMediaType;
import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static io.vavr.API.*;
import static io.vavr.API.$;

public class GCStorageAPITest {

    private GCStorageService target;

    private Storage storage;

    private String bucket = "test";

    @BeforeEach
    public void setUp() {
        this.storage = Mockito.mock(Storage.class);
        this.target = new GCStorageService(bucket, storage);
    }

    @Test
    public void givenValidResource_whenCallsStore_shouldStoreIt() {
        final var expectedResource = getResource(VideoMediaType.THUMBNAIL);
        final var expectedId = expectedResource.name();

        final Blob blob = mockBlob(expectedResource);
        Mockito.doReturn(blob).when(storage).get(Mockito.eq(bucket), Mockito.eq(expectedId));

        this.target.store(expectedId, expectedResource);

        final var capturer = ArgumentCaptor.forClass(BlobInfo.class);

        Mockito.verify(storage, Mockito.times(1)).create(capturer.capture(), Mockito.eq(expectedResource.content()));

        final var actualBlob = capturer.getValue();
        Assertions.assertEquals(this.bucket, actualBlob.getBlobId().getBucket());
        Assertions.assertEquals(expectedId, actualBlob.getBlobId().getName());
        Assertions.assertEquals(expectedResource.contentType(), actualBlob.getContentType());
        Assertions.assertEquals(expectedResource.checksum(), actualBlob.getCrc32cToHexString());
    }

    @Test
    public void givenResource_whenCallsGet_shouldRetrieveIt() {
        final var expectedResource = getResource(VideoMediaType.THUMBNAIL);
        final var expectedId = expectedResource.name();

        final Blob blob = mockBlob(expectedResource);
        Mockito.doReturn(blob).when(storage).get(Mockito.eq(bucket), Mockito.eq(expectedId));

        final var actualContent = target.get(expectedId).get();

        Assertions.assertEquals(expectedResource.checksum(), actualContent.checksum());
        Assertions.assertEquals(expectedResource.name(), actualContent.name());
        Assertions.assertEquals(expectedResource.content(), actualContent.content());
        Assertions.assertEquals(expectedResource.contentType(), actualContent.contentType());
    }

    @Test
    public void givenInvalidResource_whenCallsGet_shouldRetrieveEmpty() {
        final var expectedResource = getResource(VideoMediaType.THUMBNAIL);
        final var expectedId = expectedResource.name();

        Mockito.doReturn(null).when(storage).get(Mockito.eq(bucket), Mockito.eq(expectedId));

        final var actualContent = target.get(expectedId);

        Assertions.assertTrue(actualContent.isEmpty());
    }

    @Test
    public void givenPrefix_whenCallsList_shouldRetrieveAll() {
        final var video = getResource(VideoMediaType.VIDEO);
        final var banner = getResource(VideoMediaType.BANNER);
        final var expectedIds = List.of(video.name(), banner.name());

        final var page = Mockito.mock(Page.class);

        final Blob blob1 = mockBlob(video);
        final Blob blob2 = mockBlob(banner);

        Mockito.doReturn(List.of(blob1, blob2)).when(page).iterateAll();
        Mockito.doReturn(page).when(storage).list(Mockito.eq(bucket), Mockito.eq(Storage.BlobListOption.prefix("it")));

        final var actualContent = target.list("it");

        Assertions.assertTrue(
                expectedIds.size() == actualContent.size()
                        && expectedIds.containsAll(actualContent)
        );
    }

    @Test
    public void givenResource_whenCallsDeleteAll_shouldEmptyStorage() {
        final var expectedIds = List.of("item1", "item2");

        target.deleteAll(expectedIds);

        final var capturer = ArgumentCaptor.forClass(List.class);

        Mockito.verify(storage, Mockito.times(1)).delete(capturer.capture());

        final var actualIds = ((List<BlobId>) capturer.getValue()).stream()
                .map(BlobId::getName)
                .toList();

        Assertions.assertTrue(expectedIds.size() == actualIds.size() && actualIds.containsAll(expectedIds));
    }

    private Blob mockBlob(final Resource resource) {
        final var blob = Mockito.mock(Blob.class);
        Mockito.when(blob.getBlobId()).thenReturn(BlobId.of(bucket, resource.name()));
        Mockito.when(blob.getCrc32cToHexString()).thenReturn(resource.checksum());
        Mockito.when(blob.getContent()).thenReturn(resource.content());
        Mockito.when(blob.getContentType()).thenReturn(resource.contentType());
        Mockito.when(blob.getName()).thenReturn(resource.name());
        return blob;
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
}
