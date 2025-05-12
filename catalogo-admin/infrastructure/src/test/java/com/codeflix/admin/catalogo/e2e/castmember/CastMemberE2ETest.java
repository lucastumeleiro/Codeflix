package com.codeflix.admin.catalogo.e2e.castmember;

import com.codeflix.admin.catalogo.E2ETest;
import com.codeflix.admin.catalogo.domain.castmember.CastMemberID;
import com.codeflix.admin.catalogo.domain.castmember.CastMemberType;
import com.codeflix.admin.catalogo.e2e.MockDsl;
import com.codeflix.admin.catalogo.infrastructure.castmember.persistence.CastMemberRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@E2ETest
@Testcontainers
public class CastMemberE2ETest implements MockDsl {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CastMemberRepository castMemberRepository;
    // Revisar futuramente

//    @Container
//    private static final MySQLContainer MYSQL_CONTAINER = new MySQLContainer("mysql:latest")
//            .withPassword("123456")
//            .withUsername("root")
//            .withDatabaseName("adm_videos");
//
//    @DynamicPropertySource
//    public static void setDatasourceProperties(final DynamicPropertyRegistry registry) {
//        registry.add("mysql.port", () -> MYSQL_CONTAINER.getMappedPort(3306));
//    }

    @Override
    public MockMvc mvc() {
        return this.mvc;
    }

//    @Test
//    public void asACatalogAdminIShouldBeAbleToCreateANewCastMemberWithValidValues() throws Exception {
//        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
//        Assertions.assertEquals(0, castMemberRepository.count());
//
//        final var expectedName = "Lucas";
//        final var expectedType = CastMemberType.ACTOR;
//
//        final var actualMemberId = givenACastMember(expectedName, expectedType);
//
//        final var actualMember = castMemberRepository.findById(actualMemberId.getValue()).get();
//
//        Assertions.assertEquals(expectedName, actualMember.getName());
//        Assertions.assertEquals(expectedType, actualMember.getType());
//        Assertions.assertNotNull(actualMember.getCreatedAt());
//        Assertions.assertNotNull(actualMember.getUpdatedAt());
//        Assertions.assertEquals(actualMember.getCreatedAt(), actualMember.getUpdatedAt());
//    }
//
//    @Test
//    public void asACatalogAdminIShouldBeAbleToSeeATreatedErrorByCreatingANewCastMemberWithInvalidValues() throws Exception {
//        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
//        Assertions.assertEquals(0, castMemberRepository.count());
//
//        final String expectedName = null;
//        final var expectedType = CastMemberType.ACTOR;
//        final var expectedErrorMessage = "'name' should not be null";
//
//        givenACastMemberResult(expectedName, expectedType)
//                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
//                .andExpect(MockMvcResultMatchers.header().string("Location", Matchers.nullValue()))
//                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", Matchers.hasSize(1)))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", Matchers.equalTo(expectedErrorMessage)));
//    }
//
//    @Test
//    public void asACatalogAdminIShouldBeAbleToNavigateThruAllMembers() throws Exception {
//        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
//        Assertions.assertEquals(0, castMemberRepository.count());
//
//        givenACastMember("Vin Diesel", CastMemberType.ACTOR);
//        givenACastMember("Quentin Tarantino", CastMemberType.DIRECTOR);
//        givenACastMember("Jason Momoa", CastMemberType.ACTOR);
//
//        listCastMembers(0, 1)
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(0)))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(1)))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(3)))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(1)))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo("Jason Momoa")));
//
//        listCastMembers(1, 1)
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(1)))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(1)))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(3)))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(1)))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo("Quentin Tarantino")));
//
//        listCastMembers(2, 1)
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(2)))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(1)))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(3)))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(1)))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo("Vin Diesel")));
//
//        listCastMembers(3, 1)
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(3)))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(1)))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(3)))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(0)));
//    }
//
//    @Test
//    public void asACatalogAdminIShouldBeAbleToSearchThruAllMembers() throws Exception {
//        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
//        Assertions.assertEquals(0, castMemberRepository.count());
//
//        givenACastMember("Vin Diesel", CastMemberType.ACTOR);
//        givenACastMember("Quentin Tarantino", CastMemberType.DIRECTOR);
//        givenACastMember("Jason Momoa", CastMemberType.ACTOR);
//
//        listCastMembers(0, 1, "vin")
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(0)))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(1)))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(1)))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(1)))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo("Vin Diesel")));
//    }
//
//    @Test
//    public void asACatalogAdminIShouldBeAbleToSortAllMembersByNameDesc() throws Exception {
//        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
//        Assertions.assertEquals(0, castMemberRepository.count());
//
//        givenACastMember("Vin Diesel", CastMemberType.ACTOR);
//        givenACastMember("Quentin Tarantino", CastMemberType.DIRECTOR);
//        givenACastMember("Jason Momoa", CastMemberType.ACTOR);
//
//        listCastMembers(0, 3, "", "name", "desc")
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(0)))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(3)))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(3)))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(3)))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo("Vin Diesel")))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.items[1].name", Matchers.equalTo("Quentin Tarantino")))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.items[2].name", Matchers.equalTo("Jason Momoa")));
//    }
//
//    @Test
//    public void asACatalogAdminIShouldBeAbleToGetACastMemberByItsIdentifier() throws Exception {
//        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
//        Assertions.assertEquals(0, castMemberRepository.count());
//
//        final var expectedName = "Lucas";
//        final var expectedType = CastMemberType.ACTOR;
//
//        givenACastMember("Lucas", CastMemberType.ACTOR);
//        givenACastMember("Lucas2", CastMemberType.ACTOR);
//        final var actualId = givenACastMember(expectedName, expectedType);
//
//        final var actualMember = retrieveACastMember(actualId);
//
//        Assertions.assertEquals(expectedName, actualMember.name());
//        Assertions.assertEquals(expectedType.name(), actualMember.type());
//        Assertions.assertNotNull(actualMember.createdAt());
//        Assertions.assertNotNull(actualMember.updatedAt());
//        Assertions.assertEquals(actualMember.createdAt(), actualMember.updatedAt());
//    }
//
//    @Test
//    public void asACatalogAdminIShouldBeAbleToSeeATreatedErrorByGettingANotFoundCastMember() throws Exception {
//        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
//        Assertions.assertEquals(0, castMemberRepository.count());
//
//        givenACastMember("Lucas", CastMemberType.ACTOR);
//        givenACastMember("Lucas2", CastMemberType.ACTOR);
//
//        retrieveACastMemberResult(CastMemberID.from("123"))
//                .andExpect(MockMvcResultMatchers.status().isNotFound())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo("CastMember with ID 123 was not found")));
//    }
//
//    @Test
//    public void asACatalogAdminIShouldBeAbleToUpdateACastMemberByItsIdentifier() throws Exception {
//        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
//        Assertions.assertEquals(0, castMemberRepository.count());
//
//        final var expectedName = "Vin Diesel";
//        final var expectedType = CastMemberType.ACTOR;
//
//        givenACastMember("Lucas", CastMemberType.ACTOR);
//        final var actualId = givenACastMember("vin d", CastMemberType.DIRECTOR);
//
//        updateACastMember(actualId, expectedName, expectedType)
//                .andExpect(MockMvcResultMatchers.status().isOk());
//
//        final var actualMember = retrieveACastMember(actualId);
//
//        Assertions.assertEquals(expectedName, actualMember.name());
//        Assertions.assertEquals(expectedType.name(), actualMember.type());
//        Assertions.assertNotNull(actualMember.createdAt());
//        Assertions.assertNotNull(actualMember.updatedAt());
//        Assertions.assertNotEquals(actualMember.createdAt(), actualMember.updatedAt());
//    }
//
//    @Test
//    public void asACatalogAdminIShouldBeAbleToSeeATreatedErrorByUpdatingACastMemberWithInvalidValue() throws Exception {
//        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
//        Assertions.assertEquals(0, castMemberRepository.count());
//
//        final var expectedName = "";
//        final var expectedType = CastMemberType.ACTOR;
//        final var expectedErrorMessage = "'name' should not be empty";
//
//        givenACastMember("Lucas", CastMemberType.ACTOR);
//        final var actualId = givenACastMember("vin d", CastMemberType.DIRECTOR);
//
//        updateACastMember(actualId, expectedName, expectedType)
//                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", Matchers.hasSize(1)))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", Matchers.equalTo(expectedErrorMessage)));
//    }
//
//    @Test
//    public void asACatalogAdminIShouldBeAbleToDeleteACastMemberByItsIdentifier() throws Exception {
//        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
//        Assertions.assertEquals(0, castMemberRepository.count());
//
//        givenACastMember("Lucas", CastMemberType.ACTOR);
//        final var actualId = givenACastMember("Lucas", CastMemberType.ACTOR);
//
//        Assertions.assertEquals(2, castMemberRepository.count());
//
//        deleteACastMember(actualId)
//                .andExpect(MockMvcResultMatchers.status().isNoContent());
//
//        Assertions.assertEquals(1, castMemberRepository.count());
//        Assertions.assertFalse(castMemberRepository.existsById(actualId.getValue()));
//    }
//
//    @Test
//    public void asACatalogAdminIShouldBeAbleToDeleteACastMemberWithInvalidIdentifier() throws Exception {
//        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
//        Assertions.assertEquals(0, castMemberRepository.count());
//
//        givenACastMember("Lucas", CastMemberType.ACTOR);
//        givenACastMember("Lucas2", CastMemberType.ACTOR);
//
//        Assertions.assertEquals(2, castMemberRepository.count());
//
//        deleteACastMember(CastMemberID.from("123"))
//                .andExpect(MockMvcResultMatchers.status().isNoContent());
//
//        Assertions.assertEquals(2, castMemberRepository.count());
//    }
}
