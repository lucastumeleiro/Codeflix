package com.codeflix.admin.catalogo.application.video.media.upload;

import com.codeflix.admin.catalogo.domain.exceptions.NotFoundException;
import com.codeflix.admin.catalogo.domain.video.MediaResourceGateway;
import com.codeflix.admin.catalogo.domain.video.Video;
import com.codeflix.admin.catalogo.domain.video.VideoGateway;
import com.codeflix.admin.catalogo.domain.video.VideoID;

import java.util.Objects;

public class DefaultUploadMediaUseCase extends UploadMediaUseCase {

    private final MediaResourceGateway mediaResourceGateway;
    private final VideoGateway videoGateway;

    public DefaultUploadMediaUseCase(
            final MediaResourceGateway mediaResourceGateway,
            final VideoGateway videoGateway
    ) {
        this.mediaResourceGateway = Objects.requireNonNull(mediaResourceGateway);
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Override
    public UploadMediaOutput execute(final UploadMediaCommand aCmd) {
        final var id = VideoID.from(aCmd.videoId());
        final var resource = aCmd.videoResource();

        final var aVideo = this.videoGateway.findById(id)
                .orElseThrow(() -> notFound(id));

        switch (resource.type()) {
            case VIDEO -> aVideo.updateVideoMedia(mediaResourceGateway.storeAudioVideo(id, resource));
            case TRAILER -> aVideo.updateTrailerMedia(mediaResourceGateway.storeAudioVideo(id, resource));
            case BANNER -> aVideo.updateBannerMedia(mediaResourceGateway.storeImage(id, resource));
            case THUMBNAIL -> aVideo.updateThumbnailMedia(mediaResourceGateway.storeImage(id, resource));
            case THUMBNAIL_HALF -> aVideo.updateThumbnailHalfMedia(mediaResourceGateway.storeImage(id, resource));
        }

        return UploadMediaOutput.with(videoGateway.update(aVideo), resource.type());
    }

    private NotFoundException notFound(final VideoID id) {
        return NotFoundException.with(Video.class, id);
    }
}
