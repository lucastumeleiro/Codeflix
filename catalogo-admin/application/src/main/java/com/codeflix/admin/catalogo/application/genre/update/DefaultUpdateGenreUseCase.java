package com.codeflix.admin.catalogo.application.genre.update;

import com.codeflix.admin.catalogo.domain.Identifier;
import com.codeflix.admin.catalogo.domain.category.CategoryGateway;
import com.codeflix.admin.catalogo.domain.category.CategoryID;
import com.codeflix.admin.catalogo.domain.exceptions.DomainException;
import com.codeflix.admin.catalogo.domain.exceptions.NotFoundException;
import com.codeflix.admin.catalogo.domain.exceptions.NotificationException;
import com.codeflix.admin.catalogo.domain.genre.Genre;
import com.codeflix.admin.catalogo.domain.genre.GenreGateway;
import com.codeflix.admin.catalogo.domain.genre.GenreID;
import com.codeflix.admin.catalogo.domain.validation.Error;
import com.codeflix.admin.catalogo.domain.validation.ValidationHandler;
import com.codeflix.admin.catalogo.domain.validation.handler.Notification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class DefaultUpdateGenreUseCase extends UpdateGenreUseCase {

    private final CategoryGateway categoryGateway;
    private final GenreGateway genreGateway;

    public DefaultUpdateGenreUseCase(
            final CategoryGateway categoryGateway,
            final GenreGateway genreGateway
    ) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Override
    public UpdateGenreOutput execute(final UpdateGenreCommand command) {
        final var id = GenreID.from(command.id());
        final var name = command.name();
        final var isActive = command.isActive();
        final var categories = toCategoryId(command.categories());

        final var genre = this.genreGateway.findById(id)
                .orElseThrow(notFound(id));

        final var notification = Notification.create();
        notification.append(validateCategories(categories));
        notification.validate(() -> genre.update(name, isActive, categories));

        if (notification.hasError()) {
            throw new NotificationException(
                    "Could not update Aggregate Genre %s".formatted(command.id()), notification
            );
        }

        return UpdateGenreOutput.from(this.genreGateway.update(genre));
    }

    private ValidationHandler validateCategories(List<CategoryID> ids) {
        final var notification = Notification.create();
        if (ids == null || ids.isEmpty()) {
            return notification;
        }

        final var retrievedIds = categoryGateway.existsByIds(ids);

        if (ids.size() != retrievedIds.size()) {
            final var missingIds = new ArrayList<>(ids);
            missingIds.removeAll(retrievedIds);

            final var missingIdsMessage = missingIds.stream()
                    .map(CategoryID::getValue)
                    .collect(Collectors.joining(", "));

            notification.append(new Error("Some categories could not be found: %s".formatted(missingIdsMessage)));
        }

        return notification;
    }

    private Supplier<DomainException> notFound(final Identifier id) {
        return () -> NotFoundException.with(Genre.class, id);
    }

    private List<CategoryID> toCategoryId(final List<String> categories) {
        return categories.stream()
                .map(CategoryID::from)
                .toList();
    }
}
