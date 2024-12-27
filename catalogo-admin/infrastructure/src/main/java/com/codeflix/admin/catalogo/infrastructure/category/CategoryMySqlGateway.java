package com.codeflix.admin.catalogo.infrastructure.category;

import com.codeflix.admin.catalogo.domain.category.Category;
import com.codeflix.admin.catalogo.domain.category.CategoryGateway;
import com.codeflix.admin.catalogo.domain.category.CategoryID;
import com.codeflix.admin.catalogo.domain.category.CategorySearchQuery;
import com.codeflix.admin.catalogo.domain.pagination.Pagination;
import com.codeflix.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import com.codeflix.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryMySqlGateway implements CategoryGateway {

    private final CategoryRepository repository;

    public CategoryMySqlGateway(final CategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public Category create(final Category category) {
        return this.repository.save(CategoryJpaEntity.from(category)).toAggregate();
    }

    @Override
    public void deleteById(CategoryID id) {

    }

    @Override
    public Optional<Category> findById(CategoryID id) {
        return Optional.empty();
    }

    @Override
    public Pagination<Category> findAll(CategorySearchQuery query) {
        return null;
    }

    @Override
    public Category update(Category category) {
        return null;
    }
}
