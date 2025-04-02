package com.codeflix.admin.catalogo.domain.video;

import java.util.Optional;

public interface MediaResourceGateway {

    AudioVideoMedia storeAudioVideo(VideoID id, VideoResource resource);

    ImageMedia storeImage(VideoID id, VideoResource resource);

    Optional<Resource> getResource(VideoID id, VideoMediaType type);

    void clearResources(VideoID id);
}
