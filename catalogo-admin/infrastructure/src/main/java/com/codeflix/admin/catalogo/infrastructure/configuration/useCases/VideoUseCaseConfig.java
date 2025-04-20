package com.codeflix.admin.catalogo.infrastructure.configuration.useCases;

import com.codeflix.admin.catalogo.application.video.create.CreateVideoUseCase;
import com.codeflix.admin.catalogo.application.video.create.DefaultCreateVideoUseCase;
import com.codeflix.admin.catalogo.application.video.delete.DefaultDeleteVideoUseCase;
import com.codeflix.admin.catalogo.application.video.delete.DeleteVideoUseCase;
import com.codeflix.admin.catalogo.application.video.media.get.DefaultGetMediaUseCase;
import com.codeflix.admin.catalogo.application.video.media.get.GetMediaUseCase;
import com.codeflix.admin.catalogo.application.video.media.update.DefaultUpdateMediaStatusUseCase;
import com.codeflix.admin.catalogo.application.video.media.update.UpdateMediaStatusUseCase;
import com.codeflix.admin.catalogo.application.video.media.upload.DefaultUploadMediaUseCase;
import com.codeflix.admin.catalogo.application.video.media.upload.UploadMediaUseCase;
import com.codeflix.admin.catalogo.application.video.retrieve.get.DefaultGetVideoUseCase;
import com.codeflix.admin.catalogo.application.video.retrieve.get.GetVideoUseCase;
import com.codeflix.admin.catalogo.application.video.retrieve.list.DefaultListVideosUseCase;
import com.codeflix.admin.catalogo.application.video.retrieve.list.ListVideosUseCase;
import com.codeflix.admin.catalogo.application.video.update.DefaultUpdateVideoUseCase;
import com.codeflix.admin.catalogo.application.video.update.UpdateVideoUseCase;
import com.codeflix.admin.catalogo.domain.castmember.CastMemberGateway;
import com.codeflix.admin.catalogo.domain.category.CategoryGateway;
import com.codeflix.admin.catalogo.domain.genre.GenreGateway;
import com.codeflix.admin.catalogo.domain.video.MediaResourceGateway;
import com.codeflix.admin.catalogo.domain.video.VideoGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class VideoUseCaseConfig {

    private final CategoryGateway categoryGateway;
    private final CastMemberGateway castMemberGateway;
    private final GenreGateway genreGateway;
    private final MediaResourceGateway mediaResourceGateway;
    private final VideoGateway videoGateway;

    public VideoUseCaseConfig(
            final CategoryGateway categoryGateway,
            final CastMemberGateway castMemberGateway,
            final GenreGateway genreGateway,
            final MediaResourceGateway mediaResourceGateway,
            final VideoGateway videoGateway
    ) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
        this.genreGateway = Objects.requireNonNull(genreGateway);
        this.mediaResourceGateway = Objects.requireNonNull(mediaResourceGateway);
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Bean
    public CreateVideoUseCase createVideoUseCase() {
        return new DefaultCreateVideoUseCase(categoryGateway, castMemberGateway, genreGateway, mediaResourceGateway, videoGateway);
    }

    @Bean
    public UpdateVideoUseCase updateVideoUseCase() {
        return new DefaultUpdateVideoUseCase(videoGateway, categoryGateway, castMemberGateway, genreGateway, mediaResourceGateway);
    }

    @Bean
    public GetVideoUseCase getVideoByIdUseCase() {
        return new DefaultGetVideoUseCase(videoGateway);
    }

    @Bean
    public DeleteVideoUseCase deleteVideoUseCase() {
        return new DefaultDeleteVideoUseCase(videoGateway, mediaResourceGateway);
    }

    @Bean
    public ListVideosUseCase listVideosUseCase() {
        return new DefaultListVideosUseCase(videoGateway);
    }

    @Bean
    public GetMediaUseCase getMediaUseCase() {
        return new DefaultGetMediaUseCase(mediaResourceGateway);
    }

    @Bean
    public UploadMediaUseCase uploadMediaUseCase() {
        return new DefaultUploadMediaUseCase(mediaResourceGateway, videoGateway);
    }

    @Bean
    public UpdateMediaStatusUseCase updateMediaStatusUseCase() {
        return new DefaultUpdateMediaStatusUseCase(videoGateway);
    }
}
