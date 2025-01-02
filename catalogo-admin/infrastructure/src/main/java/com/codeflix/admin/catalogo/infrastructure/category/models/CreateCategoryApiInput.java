package com.codeflix.admin.catalogo.infrastructure.category.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public record CreateCategoryApiInput(
        @JsonProperty("name") String name,
        @JsonProperty("description") String description,
        @JsonProperty("is_active") Boolean active
) implements Serializable {
}
