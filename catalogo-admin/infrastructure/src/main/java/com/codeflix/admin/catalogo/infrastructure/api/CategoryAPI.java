package com.codeflix.admin.catalogo.infrastructure.api;

import com.codeflix.admin.catalogo.domain.pagination.Pagination;
import com.codeflix.admin.catalogo.infrastructure.category.models.CategoryApiOutput;
import com.codeflix.admin.catalogo.infrastructure.category.models.CreateCategoryApiInput;
import com.codeflix.admin.catalogo.infrastructure.category.models.UpdateCategoryApiInput;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = "categories")
@Tag(name = "Categories")
public interface CategoryAPI {

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create a new category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created successfully"),
            @ApiResponse(responseCode = "422", description = "A validation error was thrown"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
    })
    ResponseEntity<?> createCategory(@RequestBody CreateCategoryApiInput input);

    @GetMapping
    @Operation(summary = "List paginated categories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Listed successfully"),
            @ApiResponse(responseCode = "422", description = "A invalidation parameter was received"),
            @ApiResponse(responseCode = "500", description = "A internal server error was thrown"),
    })
    Pagination<?> listCategories(
            @RequestParam(name = "search", required = false, defaultValue = "") final String search,
            @RequestParam(name = "page", required = false, defaultValue = "0") final int page,
            @RequestParam(name = "perPage", required = false, defaultValue = "10") final int perPage,
            @RequestParam(name = "sort", required = false, defaultValue = "name") final String sort,
            @RequestParam(name = "dir", required = false, defaultValue = "asc") final String dir
    );

    @GetMapping(value = "{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Get a category by it`s identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Category was not found"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
    })
    CategoryApiOutput getById(@PathVariable(name = "id") String id);

    @PutMapping(value = "{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Update a category by it`s identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category updated successfully"),
            @ApiResponse(responseCode = "404", description = "Category was not found"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
    })
    ResponseEntity<?> updateById(@PathVariable(name = "id") String id, @RequestBody UpdateCategoryApiInput input);
}