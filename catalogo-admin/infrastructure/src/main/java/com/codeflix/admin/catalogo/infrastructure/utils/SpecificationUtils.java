package com.codeflix.admin.catalogo.infrastructure.utils;

import org.springframework.data.jpa.domain.Specification;

public final class SpecificationUtils {

    private SpecificationUtils() {
    }

    public static <T> Specification<T> like(final String prop, final String term) {
        return (root, query1, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.upper(root.get(prop)), likeTerm(term.toUpperCase()));
    }

    private static String likeTerm(final String term) {
        return "%" + term + "%";
    }
}
