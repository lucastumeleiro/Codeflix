package com.codeflix.admin.catalogo.infrastructure.video;

import com.codeflix.admin.catalogo.domain.Identifier;
import com.codeflix.admin.catalogo.domain.Utils.CollectionUtils;
import com.codeflix.admin.catalogo.domain.pagination.Pagination;
import com.codeflix.admin.catalogo.domain.video.*;
import com.codeflix.admin.catalogo.infrastructure.utils.SqlUtils;
import com.codeflix.admin.catalogo.infrastructure.video.persistence.VideoJpaEntity;
import com.codeflix.admin.catalogo.infrastructure.video.persistence.VideoRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Component
public class DefaultVideoGateway implements VideoGateway {

    //    private final EventService eventService;
    private final VideoRepository videoRepository;

    public DefaultVideoGateway(
//            @VideoCreatedQueue final EventService eventService,
            final VideoRepository videoRepository
    ) {
//        this.eventService = Objects.requireNonNull(eventService);
        this.videoRepository = Objects.requireNonNull(videoRepository);
    }

    @Override
    @Transactional
    public Video create(final Video video) {
        return save(video);
    }

    @Override
    public void deleteById(final VideoID id) {
        final var videoId = id.getValue();
        if (this.videoRepository.existsById(videoId)) {
            this.videoRepository.deleteById(videoId);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Video> findById(final VideoID id) {
        return this.videoRepository.findById(id.getValue())
                .map(VideoJpaEntity::toAggregate);
    }

    @Override
    @Transactional
    public Video update(final Video video) {
        return save(video);
    }

    @Override
    public Pagination<VideoPreview> findAll(final VideoSearchQuery aQuery) {
        final var page = PageRequest.of(
                aQuery.page(),
                aQuery.perPage(),
                Sort.by(Sort.Direction.fromString(aQuery.direction()), aQuery.sort())
        );

        final var actualPage = this.videoRepository.findAll(
                SqlUtils.like(SqlUtils.upper(aQuery.terms())),
                CollectionUtils.nullIfEmpty(CollectionUtils.mapTo(aQuery.castMembers(), Identifier::getValue)),
                CollectionUtils.nullIfEmpty(CollectionUtils.mapTo(aQuery.categories(), Identifier::getValue)),
                CollectionUtils.nullIfEmpty(CollectionUtils.mapTo(aQuery.genres(), Identifier::getValue)),
                page
        );

        return new Pagination<>(
                actualPage.getNumber(),
                actualPage.getSize(),
                actualPage.getTotalElements(),
                actualPage.toList()
        );
    }

    private Video save(final Video video) {
        final var result = this.videoRepository.save(VideoJpaEntity.from(video))
                .toAggregate();

//        video.publishDomainEvents(this.eventService::send);

        return result;
    }
}