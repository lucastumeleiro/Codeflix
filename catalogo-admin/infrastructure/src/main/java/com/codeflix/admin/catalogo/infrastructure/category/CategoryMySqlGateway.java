package com.codeflix.admin.catalogo.infrastructure.category;

import com.codeflix.admin.catalogo.domain.category.Category;
import com.codeflix.admin.catalogo.domain.category.CategoryGateway;
import com.codeflix.admin.catalogo.domain.category.CategoryID;
import com.codeflix.admin.catalogo.domain.pagination.Pagination;
import com.codeflix.admin.catalogo.domain.pagination.SearchQuery;
import com.codeflix.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import com.codeflix.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import com.codeflix.admin.catalogo.infrastructure.utils.SpecificationUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Component
public class CategoryMySqlGateway implements CategoryGateway {

    private final CategoryRepository repository;

    public CategoryMySqlGateway(final CategoryRepository repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    @Override
    public Category create(final Category category) {
        return save(category);
    }

    @Override
    public Category update(final Category category) {
        return save(category);
    }

    @Override
    public List<CategoryID> existsByIds(final Iterable<CategoryID> categoryIDs) {

        final var ids = StreamSupport.stream(categoryIDs.spliterator(), false)
                .map(CategoryID::getValue)
                .toList();

        return this.repository.existsByIds(ids).stream()
                .map(CategoryID::from)
                .toList();
    }

    @Override
    public void deleteById(CategoryID id) {
        if (this.repository.existsById(id.getValue())) {
            this.repository.deleteById(id.getValue());
        }
    }

    @Override
    public Optional<Category> findById(CategoryID id) {
        return this.repository.findById(id.getValue())
                .map(CategoryJpaEntity::toAggregate);
    }

    @Override
    public Pagination<Category> findAll(final SearchQuery query) {

        final var specifications = Optional.ofNullable(query.terms())
                .filter(term -> !term.isBlank())
                .map(this::AssembleSpecification)
                .orElse(null);

        final var page = PageRequest.of(
                query.page(),
                query.perPage(),
                Sort.by(Sort.Direction.fromString(query.direction()), query.sort())
        );

        final var pageResult = this.repository.findAll(Specification.where(specifications), page);

        return new Pagination<>(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.map(CategoryJpaEntity::toAggregate).toList()
        );
    }

    private Category save(Category category) {
        return this.repository.save(CategoryJpaEntity.from(category)).toAggregate();
    }

    private Specification<CategoryJpaEntity> AssembleSpecification(final String term) {
        final Specification<CategoryJpaEntity> nameLike = SpecificationUtils.like("name", term);
        final Specification<CategoryJpaEntity> descriptionLike = SpecificationUtils.like("description", term);

        return nameLike.or(descriptionLike);
    }
}
