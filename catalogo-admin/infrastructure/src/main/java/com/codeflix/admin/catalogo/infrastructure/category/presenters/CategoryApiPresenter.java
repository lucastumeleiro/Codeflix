package com.codeflix.admin.catalogo.infrastructure.category.presenters;

import com.codeflix.admin.catalogo.application.category.retrieve.get.CategoryOutput;
import com.codeflix.admin.catalogo.infrastructure.category.models.CategoryApiOutput;
import com.codeflix.admin.catalogo.infrastructure.category.models.CategoryListOutput;

public interface CategoryApiPresenter {

    static CategoryApiOutput present(final CategoryOutput output) {
        return new CategoryApiOutput(
                output.id().getValue(),
                output.name(),
                output.description(),
                output.isActive(),
                output.createdAt(),
                output.updatedAt(),
                output.deleteAt()
        );
    }

    static CategoryListOutput present(final com.codeflix.admin.catalogo.application.category.retrieve.list.CategoryListOutput output) {
        return new CategoryListOutput(
                output.id().getValue(),
                output.name(),
                output.description(),
                output.isActive(),
                output.createdAt(),
                output.deleteAt()
        );
    }
}
