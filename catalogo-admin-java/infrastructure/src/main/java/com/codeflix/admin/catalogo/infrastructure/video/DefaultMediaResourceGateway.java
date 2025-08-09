package com.codeflix.admin.catalogo.infrastructure.video;


import com.codeflix.admin.catalogo.domain.video.*;
import com.codeflix.admin.catalogo.infrastructure.configuration.properties.storage.StorageProperties;
import com.codeflix.admin.catalogo.infrastructure.services.StorageService;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DefaultMediaResourceGateway implements MediaResourceGateway {

    private final String filenamePattern;
    private final String locationPattern;
    private final StorageService storageService;

    public DefaultMediaResourceGateway(final StorageProperties props, final StorageService storageService) {
        this.filenamePattern = props.getFilenamePattern();
        this.locationPattern = props.getLocationPattern();
        this.storageService = storageService;
    }

    @Override
    public AudioVideoMedia storeAudioVideo(final VideoID id, final VideoResource videoResource) {
        final var filepath = filepath(id, videoResource.type());
        final var aResource = videoResource.resource();
        store(filepath, aResource);
        return AudioVideoMedia.with(aResource.checksum(), aResource.name(), filepath);
    }

    @Override
    public ImageMedia storeImage(final VideoID id, final VideoResource videoResource) {
        final var filepath = filepath(id, videoResource.type());
        final var aResource = videoResource.resource();
        store(filepath, aResource);
        return ImageMedia.with(aResource.checksum(), aResource.name(), filepath);
    }

    @Override
    public Optional<Resource> getResource(final VideoID id, final VideoMediaType type) {
        return this.storageService.get(filepath(id, type));
    }

    @Override
    public void clearResources(final VideoID id) {
        final var ids = this.storageService.list(folder(id));
        this.storageService.deleteAll(ids);
    }

    private String filename(final VideoMediaType type) {
        return filenamePattern.replace("{type}", type.name());
    }

    private String folder(final VideoID id) {
        return locationPattern.replace("{videoId}", id.getValue());
    }

    private String filepath(final VideoID id, final VideoMediaType type) {
        return folder(id)
                .concat("/")
                .concat(filename(type));
    }

    private void store(final String filepath, final Resource resource) {
        this.storageService.store(filepath, resource);
    }
}
