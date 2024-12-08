package com.codeflix.admin.catalogo.application.category.create;

import com.codeflix.admin.catalogo.domain.category.Category;
import com.codeflix.admin.catalogo.domain.category.CategoryGateway;
import com.codeflix.admin.catalogo.domain.validation.handler.Notification;
import io.vavr.API;
import io.vavr.control.Either;

import java.util.Objects;

public class DefaultCreateCategoryUseCase extends CreateCategoryUseCase {

    private final CategoryGateway gateway;

    public DefaultCreateCategoryUseCase(CategoryGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    private Either<Notification, CreateCategoryOutput> create(Category category) {
        return API.Try(() -> this.gateway.create(category))
                .toEither()
                .bimap(Notification::create, CreateCategoryOutput::from);
    }

    @Override
    public Either<Notification, CreateCategoryOutput> execute(final CreateCategoryCommand command) {
        final var notification = Notification.create();
        final var category = Category.newCategory(command.name(), command.description(), command.isActive());
        category.validate(notification);

        return notification.hasError() ? API.Left(notification) : create(category);
    }


}
