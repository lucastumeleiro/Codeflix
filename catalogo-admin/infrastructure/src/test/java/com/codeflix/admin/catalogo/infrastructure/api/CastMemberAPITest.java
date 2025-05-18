package com.codeflix.admin.catalogo.infrastructure.api;

import com.codeflix.admin.catalogo.ApiTest;
import com.codeflix.admin.catalogo.ControllerTest;
import com.codeflix.admin.catalogo.application.castmember.create.CreateCastMemberOutput;
import com.codeflix.admin.catalogo.application.castmember.create.DefaultCreateCastMemberUseCase;
import com.codeflix.admin.catalogo.application.castmember.delete.DefaultDeleteCastMemberUseCase;
import com.codeflix.admin.catalogo.application.castmember.retrieve.get.CastMemberOutput;
import com.codeflix.admin.catalogo.application.castmember.retrieve.get.DefaultGetCastMemberUseCase;
import com.codeflix.admin.catalogo.application.castmember.retrieve.list.CastMemberListOutput;
import com.codeflix.admin.catalogo.application.castmember.retrieve.list.DefaultListCastMembersUseCase;
import com.codeflix.admin.catalogo.application.castmember.update.DefaultUpdateCastMemberUseCase;
import com.codeflix.admin.catalogo.application.castmember.update.UpdateCastMemberOutput;
import com.codeflix.admin.catalogo.domain.castmember.CastMember;
import com.codeflix.admin.catalogo.domain.castmember.CastMemberID;
import com.codeflix.admin.catalogo.domain.castmember.CastMemberType;
import com.codeflix.admin.catalogo.domain.exceptions.NotFoundException;
import com.codeflix.admin.catalogo.domain.exceptions.NotificationException;
import com.codeflix.admin.catalogo.domain.pagination.Pagination;
import com.codeflix.admin.catalogo.domain.validation.Error;
import com.codeflix.admin.catalogo.infrastructure.castmember.models.CreateCastMemberRequest;
import com.codeflix.admin.catalogo.infrastructure.castmember.models.UpdateCastMemberRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Objects;

@ControllerTest(controllers = CastMemberAPI.class)
public class CastMemberAPITest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;

    @MockitoBean
    private DefaultCreateCastMemberUseCase createCastMemberUseCase;
    @MockitoBean
    private DefaultDeleteCastMemberUseCase deleteCastMemberUseCase;
    @MockitoBean
    private DefaultGetCastMemberUseCase getCastMemberUseCase;
    @MockitoBean
    private DefaultListCastMembersUseCase listCastMembersUseCase;
    @MockitoBean
    private DefaultUpdateCastMemberUseCase updateCastMemberUseCase;

    @Test
    public void givenValidCommand_whenCreateCastMember_shouldReturnItsIdentifier() throws Exception {
        final var expectedName = "Lucas";
        final var expectedType = CastMemberType.ACTOR;
        final var expectedId = CastMemberID.from("o1i2u3i1o");

        final var command =
                new CreateCastMemberRequest(expectedName, expectedType);

        Mockito.when(createCastMemberUseCase.execute(Mockito.any()))
                .thenReturn(CreateCastMemberOutput.from(expectedId));

        final var request = MockMvcRequestBuilders.post("/cast_members")
                .with(ApiTest.CAST_MEMBERS_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(command));

        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", "/cast_members/" + expectedId.getValue()))
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(expectedId.getValue())));

        Mockito.verify(createCastMemberUseCase).execute(Mockito.argThat(actualCommand ->
                Objects.equals(expectedName, actualCommand.name())
                        && Objects.equals(expectedType, actualCommand.type())
        ));
    }

    @Test
    public void givenInvalidName_whenCreateCastMember_shouldReturnNotification() throws Exception {
        final String expectedName = null;
        final var expectedType = CastMemberType.ACTOR;

        final var expectedErrorMessage = "'name' should not be null";

        final var command =
                new CreateCastMemberRequest(expectedName, expectedType);

        Mockito.when(createCastMemberUseCase.execute(Mockito.any()))
                .thenThrow(NotificationException.with(new Error(expectedErrorMessage)));

        final var request = MockMvcRequestBuilders.post("/cast_members")
                .with(ApiTest.CAST_MEMBERS_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(command));

        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        response.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Location", Matchers.nullValue()))
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", Matchers.equalTo(expectedErrorMessage)));

        Mockito.verify(createCastMemberUseCase).execute(Mockito.argThat(actualCommand ->
                Objects.equals(expectedName, actualCommand.name())
                        && Objects.equals(expectedType, actualCommand.type())
        ));
    }

    @Test
    public void givenValidId_whenGetById_shouldReturn() throws Exception {
        final var expectedName = "Lucas";
        final var expectedType = CastMemberType.ACTOR;

        final var aMember = CastMember.newMember(expectedName, expectedType);
        final var expectedId = aMember.getId().getValue();

        Mockito.when(getCastMemberUseCase.execute(Mockito.any()))
                .thenReturn(CastMemberOutput.from(aMember));

        final var request = MockMvcRequestBuilders.get("/cast_members/{id}", expectedId)
                .with(ApiTest.CAST_MEMBERS_JWT)
                .accept(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request);

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(expectedId)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.equalTo(expectedName)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.type", Matchers.equalTo(expectedType.name())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.created_at", Matchers.equalTo(aMember.getCreatedAt().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.updated_at", Matchers.equalTo(aMember.getUpdatedAt().toString())));

        Mockito.verify(getCastMemberUseCase).execute(Mockito.eq(expectedId));
    }

    @Test
    public void givenInvalidId_whenGetByIdAndCastMemberDoesntExists_shouldReturnNotFound() throws Exception {
        final var expectedErrorMessage = "CastMember with ID 123 was not found";
        final var expectedId = CastMemberID.from("123");

        Mockito.when(getCastMemberUseCase.execute(Mockito.any()))
                .thenThrow(NotFoundException.with(CastMember.class, expectedId));

        final var request = MockMvcRequestBuilders.get("/cast_members/{id}", expectedId.getValue())
                .with(ApiTest.CAST_MEMBERS_JWT)
                .accept(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request);

        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo(expectedErrorMessage)));

        Mockito.verify(getCastMemberUseCase).execute(Mockito.eq(expectedId.getValue()));
    }

    @Test
    public void givenValidCommand_whenUpdateCastMember_shouldReturnItsIdentifier() throws Exception {
        final var expectedName = "Lucas";
        final var expectedType = CastMemberType.ACTOR;

        final var aMember = CastMember.newMember(expectedName, expectedType);
        final var expectedId = aMember.getId();

        final var command =
                new UpdateCastMemberRequest(expectedName, expectedType);

        Mockito.when(updateCastMemberUseCase.execute(Mockito.any()))
                .thenReturn(UpdateCastMemberOutput.from(expectedId));

        final var request = MockMvcRequestBuilders.put("/cast_members/{id}", expectedId.getValue())
                .with(ApiTest.CAST_MEMBERS_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(command));

        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(expectedId.getValue())));

        Mockito.verify(updateCastMemberUseCase).execute(Mockito.argThat(actualCommand ->
                Objects.equals(expectedId.getValue(), actualCommand.id())
                        && Objects.equals(expectedName, actualCommand.name())
                        && Objects.equals(expectedType, actualCommand.type())
        ));
    }

    @Test
    public void givenInvalidName_whenUpdateCastMember_shouldReturnNotification() throws Exception {
        final var aMember = CastMember.newMember("Vin Di", CastMemberType.DIRECTOR);
        final var expectedId = aMember.getId();

        final String expectedName = null;
        final var expectedType = CastMemberType.ACTOR;

        final var expectedErrorMessage = "'name' should not be null";

        final var command =
                new UpdateCastMemberRequest(expectedName, expectedType);

        Mockito.when(updateCastMemberUseCase.execute(Mockito.any()))
                .thenThrow(NotificationException.with(new Error(expectedErrorMessage)));

        final var request = MockMvcRequestBuilders.put("/cast_members/{id}", expectedId.getValue())
                .with(ApiTest.CAST_MEMBERS_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(command));

        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        response.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Location", Matchers.nullValue()))
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", Matchers.equalTo(expectedErrorMessage)));

        Mockito.verify(updateCastMemberUseCase).execute(Mockito.argThat(actualCommand ->
                Objects.equals(expectedId.getValue(), actualCommand.id())
                        && Objects.equals(expectedName, actualCommand.name())
                        && Objects.equals(expectedType, actualCommand.type())
        ));
    }

    @Test
    public void givenInvalidId_whenUpdateCastMember_shouldReturnNotFound() throws Exception {
        final var expectedId = CastMemberID.from("123");

        final var expectedName = "Lucas";
        final var expectedType = CastMemberType.ACTOR;

        final var expectedErrorMessage = "CastMember with ID 123 was not found";

        final var command =
                new UpdateCastMemberRequest(expectedName, expectedType);

        Mockito.when(updateCastMemberUseCase.execute(Mockito.any()))
                .thenThrow(NotFoundException.with(CastMember.class, expectedId));

        final var request = MockMvcRequestBuilders.put("/cast_members/{id}", expectedId.getValue())
                .with(ApiTest.CAST_MEMBERS_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(command));

        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.header().string("Location", Matchers.nullValue()))
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo(expectedErrorMessage)));

        Mockito.verify(updateCastMemberUseCase).execute(Mockito.argThat(actualCommand ->
                Objects.equals(expectedId.getValue(), actualCommand.id())
                        && Objects.equals(expectedName, actualCommand.name())
                        && Objects.equals(expectedType, actualCommand.type())
        ));
    }

    @Test
    public void givenValidId_whenDeleteById_shouldDelete() throws Exception {
        final var expectedId = "123";

        Mockito.doNothing()
                .when(deleteCastMemberUseCase).execute(Mockito.any());

        final var request = MockMvcRequestBuilders.delete("/cast_members/{id}", expectedId)
                .with(ApiTest.CAST_MEMBERS_JWT);

        final var response = this.mvc.perform(request);

        response.andExpect(MockMvcResultMatchers.status().isNoContent());

        Mockito.verify(deleteCastMemberUseCase).execute(Mockito.eq(expectedId));
    }

    @Test
    public void givenValidParams_whenCallListCastMembers_shouldReturn() throws Exception {
        final var aMember = CastMember.newMember("Lucas", CastMemberType.ACTOR);

        final var expectedPage = 1;
        final var expectedPerPage = 20;
        final var expectedTerms = "Alg";
        final var expectedSort = "type";
        final var expectedDirection = "desc";

        final var expectedItemsCount = 1;
        final var expectedTotal = 1;

        final var expectedItems = List.of(CastMemberListOutput.from(aMember));

        Mockito.when(listCastMembersUseCase.execute(Mockito.any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems));

        final var request = MockMvcRequestBuilders.get("/cast_members")
                .with(ApiTest.CAST_MEMBERS_JWT)
                .queryParam("page", String.valueOf(expectedPage))
                .queryParam("perPage", String.valueOf(expectedPerPage))
                .queryParam("search", expectedTerms)
                .queryParam("sort", expectedSort)
                .queryParam("dir", expectedDirection)
                .accept(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request);

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(expectedPage)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(expectedPerPage)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(expectedTotal)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(expectedItemsCount)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].id", Matchers.equalTo(aMember.getId().getValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo(aMember.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].type", Matchers.equalTo(aMember.getType().name())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].created_at", Matchers.equalTo(aMember.getCreatedAt().toString())));

        Mockito.verify(listCastMembersUseCase).execute(Mockito.argThat(aQuery ->
                Objects.equals(expectedPage, aQuery.page())
                        && Objects.equals(expectedPerPage, aQuery.perPage())
                        && Objects.equals(expectedTerms, aQuery.terms())
                        && Objects.equals(expectedSort, aQuery.sort())
                        && Objects.equals(expectedDirection, aQuery.direction())
        ));
    }

    @Test
    public void givenEmptyParams_whenCallListCastMembers_shouldUseDefaultsAndReturn() throws Exception {
        final var aMember = CastMember.newMember("Lucas", CastMemberType.ACTOR);

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var expectedItemsCount = 1;
        final var expectedTotal = 1;

        final var expectedItems = List.of(CastMemberListOutput.from(aMember));

        Mockito.when(listCastMembersUseCase.execute(Mockito.any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems));

        final var request = MockMvcRequestBuilders.get("/cast_members")
                .with(ApiTest.CAST_MEMBERS_JWT)
                .accept(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request);

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(expectedPage)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(expectedPerPage)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(expectedTotal)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(expectedItemsCount)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].id", Matchers.equalTo(aMember.getId().getValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo(aMember.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].type", Matchers.equalTo(aMember.getType().name())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].created_at", Matchers.equalTo(aMember.getCreatedAt().toString())));

        Mockito.verify(listCastMembersUseCase).execute(Mockito.argThat(aQuery ->
                Objects.equals(expectedPage, aQuery.page())
                        && Objects.equals(expectedPerPage, aQuery.perPage())
                        && Objects.equals(expectedTerms, aQuery.terms())
                        && Objects.equals(expectedSort, aQuery.sort())
                        && Objects.equals(expectedDirection, aQuery.direction())
        ));
    }
}
