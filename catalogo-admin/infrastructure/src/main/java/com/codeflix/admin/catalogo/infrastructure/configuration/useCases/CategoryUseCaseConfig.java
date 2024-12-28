package com.codeflix.admin.catalogo.infrastructure.configuration.useCases;

import com.codeflix.admin.catalogo.application.category.create.CreateCategoryUseCase;
import com.codeflix.admin.catalogo.application.category.create.DefaultCreateCategoryUseCase;
import com.codeflix.admin.catalogo.application.category.delete.DefaultDeleteCategoryUseCase;
import com.codeflix.admin.catalogo.application.category.delete.DeleteCategoryUseCase;
import com.codeflix.admin.catalogo.application.category.retrieve.get.DefaultGetCategoryUseCase;
import com.codeflix.admin.catalogo.application.category.retrieve.get.GetCategoryUseCase;
import com.codeflix.admin.catalogo.application.category.retrieve.list.DefaultListCategoryUseCase;
import com.codeflix.admin.catalogo.application.category.retrieve.list.ListCategoryUseCase;
import com.codeflix.admin.catalogo.application.category.update.DefaultUpdateCategoryUseCase;
import com.codeflix.admin.catalogo.application.category.update.UpdateCategoryUseCase;
import com.codeflix.admin.catalogo.domain.category.CategoryGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CategoryUseCaseConfig {

    private final CategoryGateway gateway;

    public CategoryUseCaseConfig(
            final CategoryGateway gateway
    ) {
        this.gateway = gateway;
    }

    @Bean
    public CreateCategoryUseCase createCategoryUseCase() {
        return new DefaultCreateCategoryUseCase(gateway);
    }

    @Bean
    public UpdateCategoryUseCase updateCategoryUseCase() {
        return new DefaultUpdateCategoryUseCase(gateway);
    }

    @Bean
    public GetCategoryUseCase getCategoryUseCase() {
        return new DefaultGetCategoryUseCase(gateway);
    }

    @Bean
    public ListCategoryUseCase listCategoryUseCase() {
        return new DefaultListCategoryUseCase(gateway);
    }

    @Bean
    public DeleteCategoryUseCase deleteCategoryUseCase() {
        return new DefaultDeleteCategoryUseCase(gateway);
    }
}
