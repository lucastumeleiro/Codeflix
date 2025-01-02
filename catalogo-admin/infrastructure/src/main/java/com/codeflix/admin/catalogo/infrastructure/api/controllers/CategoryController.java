package com.codeflix.admin.catalogo.infrastructure.api.controllers;

import com.codeflix.admin.catalogo.application.category.create.CreateCategoryCommand;
import com.codeflix.admin.catalogo.application.category.create.CreateCategoryOutput;
import com.codeflix.admin.catalogo.application.category.create.CreateCategoryUseCase;
import com.codeflix.admin.catalogo.domain.pagination.Pagination;
import com.codeflix.admin.catalogo.domain.validation.handler.Notification;
import com.codeflix.admin.catalogo.infrastructure.api.CategoryAPI;
import com.codeflix.admin.catalogo.infrastructure.category.models.CreateCategoryApiInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.function.Function;

@RestController
public class CategoryController implements CategoryAPI {

    @Autowired
    private CreateCategoryUseCase useCase;

    @Override
    public ResponseEntity<?> createCategory(final CreateCategoryApiInput input) {
        final var aCommand = CreateCategoryCommand.with(
                input.name(),
                input.description(),
                input.active() != null ? input.active() : true
        );

        final Function<Notification, ResponseEntity<?>> onError = notification ->
                ResponseEntity.unprocessableEntity().body(notification);

        final Function<CreateCategoryOutput, ResponseEntity<?>> onSuccess = output ->
                ResponseEntity.created(URI.create("/categories/" + output.id())).body(output);

        return this.useCase.execute(aCommand)
                .fold(onError, onSuccess);
    }

    @Override
    public Pagination<?> listCategories(String search, int page, int perPage, String sort, String dir) {
        return null;
    }
}
