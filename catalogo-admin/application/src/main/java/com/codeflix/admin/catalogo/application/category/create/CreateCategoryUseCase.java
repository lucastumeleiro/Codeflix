package com.codeflix.admin.catalogo.application.category.create;

import com.codeflix.admin.catalogo.application.UseCase;
import com.codeflix.admin.catalogo.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class CreateCategoryUseCase extends UseCase<CreateCategoryCommand, Either<Notification, CreateCategoryOutput>> {

}
