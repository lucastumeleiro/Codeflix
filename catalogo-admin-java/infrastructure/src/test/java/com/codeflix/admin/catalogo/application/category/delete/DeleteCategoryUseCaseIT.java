package com.codeflix.admin.catalogo.application.category.delete;

import com.codeflix.admin.catalogo.IntegrationTest;
import com.codeflix.admin.catalogo.domain.category.Category;
import com.codeflix.admin.catalogo.domain.category.CategoryGateway;
import com.codeflix.admin.catalogo.domain.category.CategoryID;
import com.codeflix.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import com.codeflix.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.util.Arrays;

@IntegrationTest
public class DeleteCategoryUseCaseIT {

    @Autowired
    private DeleteCategoryUseCase useCase;
    @Autowired
    private CategoryRepository repository;

    @MockitoSpyBean
    private CategoryGateway gateway;

    @BeforeEach
    void cleanUp() {
        repository.deleteAll();
    }

    @Test
    public void givenValidId_whenCallsDeleteCategory_shouldBeSuccess() {
        final var category = Category.newCategory("Filmes", "A categoria mais assistida", true);
        final var expectedId = category.getId();

        save(category);
        Assertions.assertEquals(1, repository.count());

        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));
        Assertions.assertEquals(0, repository.count());
    }

    @Test
    public void givenInvalidId_whenCallsDeleteCategory_shouldBeNothing() {
        final var expectedId = CategoryID.from("123");

        Assertions.assertEquals(0, repository.count());

        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        Assertions.assertEquals(0, repository.count());
    }

    private void save(final Category... category) {
        repository.saveAllAndFlush(
                Arrays.stream(category)
                        .map(CategoryJpaEntity::from)
                        .toList()
        );
    }
}
