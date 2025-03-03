package com.codeflix.admin.catalogo.domain.category;

import com.codeflix.admin.catalogo.domain.validation.Error;
import com.codeflix.admin.catalogo.domain.validation.ValidationHandler;
import com.codeflix.admin.catalogo.domain.validation.Validator;

public class CategoryValidator extends Validator {

    private static final int NAME_MIN_LENGTH = 3;
    private static final int NAME_MAX_LENGTH = 255;

    private final Category category;

    protected CategoryValidator(
            final Category category,
            final ValidationHandler handler
    ) {
        super(handler);
        this.category = category;
    }

    @Override
    public void validate() {
        CheckNameConstraints();
    }

    private void CheckNameConstraints() {
        final var name = this.category.getName();

        if (name == null) {
            this.validationHandler().append(new Error("'name' should not be null"));
            return;
        }

        if (name.isBlank()) {
            this.validationHandler().append(new Error("'name' should not be empty"));
            return;
        }

        final int length = name.trim().length();
        if (length > NAME_MAX_LENGTH || length < NAME_MIN_LENGTH) {
            this.validationHandler().append(new Error("'name' must be between 3 and 255 characters"));
        }
    }
}
