package com.codeflix.admin.catalogo.infrastructure.genre;

import com.codeflix.admin.catalogo.domain.genre.Genre;
import com.codeflix.admin.catalogo.domain.genre.GenreGateway;
import com.codeflix.admin.catalogo.domain.genre.GenreID;
import com.codeflix.admin.catalogo.domain.pagination.Pagination;
import com.codeflix.admin.catalogo.domain.pagination.SearchQuery;
import com.codeflix.admin.catalogo.infrastructure.genre.persistence.GenreJpaEntity;
import com.codeflix.admin.catalogo.infrastructure.genre.persistence.GenreRepository;
import com.codeflix.admin.catalogo.infrastructure.utils.SpecificationUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.springframework.data.jpa.domain.Specification.where;

@Component
public class GenreMySqlGateway implements GenreGateway {

    private final GenreRepository genreRepository;

    public GenreMySqlGateway(final GenreRepository genreRepository) {
        this.genreRepository = Objects.requireNonNull(genreRepository);
    }

    @Override
    public Genre create(final Genre genre) {
        return save(genre);
    }

    @Override
    public void deleteById(final GenreID id) {
        final var genreId = id.getValue();
        if (this.genreRepository.existsById(genreId)) {
            this.genreRepository.deleteById(genreId);
        }
    }

    @Override
    public Optional<Genre> findById(final GenreID id) {
        return this.genreRepository.findById(id.getValue())
                .map(GenreJpaEntity::toAggregate);
    }

    @Override
    public Genre update(final Genre genre) {
        return save(genre);
    }

    @Override
    public Pagination<Genre> findAll(final SearchQuery query) {
        final var page = PageRequest.of(
                query.page(),
                query.perPage(),
                Sort.by(Sort.Direction.fromString(query.direction()), query.sort())
        );

        final var where = Optional.ofNullable(query.terms())
                .filter(str -> !str.isBlank())
                .map(this::assembleSpecification)
                .orElse(null);

        final var pageResult =
                this.genreRepository.findAll(where(where), page);

        return new Pagination<>(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.map(GenreJpaEntity::toAggregate).toList()
        );
    }

    @Override
    public List<GenreID> existsByIds(final Iterable<GenreID> genreIDS) {
        return Collections.emptyList();

        //        final var ids = StreamSupport.stream(genreIDS.spliterator(), false)
//                .map(GenreID::getValue)
//                .toList();
//        return this.genreRepository.existsByIds(ids).stream()
//                .map(GenreID::from)
//                .toList();
    }

    private Genre save(final Genre genre) {
        return this.genreRepository.save(GenreJpaEntity.from(genre))
                .toAggregate();
    }

    private Specification<GenreJpaEntity> assembleSpecification(final String terms) {
        return SpecificationUtils.like("name", terms);
    }
}
