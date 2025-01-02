package com.codeflix.admin.catalogo.application.category.retrieve.get;

import com.codeflix.admin.catalogo.domain.category.Category;
import com.codeflix.admin.catalogo.domain.category.CategoryGateway;
import com.codeflix.admin.catalogo.domain.category.CategoryID;
import com.codeflix.admin.catalogo.domain.exceptions.NotFoundException;

import java.util.Objects;
import java.util.function.Supplier;

public class DefaultGetCategoryUseCase extends GetCategoryUseCase {

    private final CategoryGateway gateway;

    public DefaultGetCategoryUseCase(final CategoryGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    private static Supplier<NotFoundException> notFound(CategoryID id) {
        return () -> NotFoundException.with(Category.class, id);
    }

    @Override
    public CategoryOutput execute(String in) {
        final var categoryId = CategoryID.from(in);

        return this.gateway.findById(categoryId)
                .map(CategoryOutput::from)
                .orElseThrow(notFound(categoryId));
    }
}
