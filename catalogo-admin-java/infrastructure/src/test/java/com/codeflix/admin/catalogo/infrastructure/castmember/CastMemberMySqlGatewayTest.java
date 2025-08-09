package com.codeflix.admin.catalogo.infrastructure.castmember;

import com.codeflix.admin.catalogo.MySqlGatewayTest;
import com.codeflix.admin.catalogo.domain.castmember.CastMember;
import com.codeflix.admin.catalogo.domain.castmember.CastMemberID;
import com.codeflix.admin.catalogo.domain.castmember.CastMemberType;
import com.codeflix.admin.catalogo.domain.pagination.SearchQuery;
import com.codeflix.admin.catalogo.infrastructure.castmember.persistence.CastMemberJpaEntity;
import com.codeflix.admin.catalogo.infrastructure.castmember.persistence.CastMemberRepository;
import com.codeflix.admin.catalogo.infrastructure.utils.InstantUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Stream;

@MySqlGatewayTest
public class CastMemberMySqlGatewayTest {

    @Autowired
    private CastMemberMySqlGateway castMemberGateway;

    @Autowired
    private CastMemberRepository castMemberRepository;

    @Test
    public void testDependencies() {
        Assertions.assertNotNull(castMemberGateway);
        Assertions.assertNotNull(castMemberRepository);
    }

    @Test
    public void givenValidCastMember_whenCreate_shouldPersist() {
        final var expectedName = "Lucas";
        final var expectedType = CastMemberType.ACTOR;

        final var member = CastMember.newMember(expectedName, expectedType);
        final var expectedId = member.getId();

        Assertions.assertEquals(0, castMemberRepository.count());

        final var actualMember = castMemberGateway.create(CastMember.with(member));

        Assertions.assertEquals(1, castMemberRepository.count());

        Assertions.assertEquals(expectedId, actualMember.getId());
        Assertions.assertEquals(expectedName, actualMember.getName());
        Assertions.assertEquals(expectedType, actualMember.getType());
        Assertions.assertEquals(member.getCreatedAt(), actualMember.getCreatedAt());
        Assertions.assertEquals(member.getUpdatedAt(), actualMember.getUpdatedAt());

        final var persistedMember = castMemberRepository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(expectedId.getValue(), persistedMember.getId());
        Assertions.assertEquals(expectedName, persistedMember.getName());
        Assertions.assertEquals(expectedType, persistedMember.getType());
        Assertions.assertEquals(member.getCreatedAt(), persistedMember.getCreatedAt());
        Assertions.assertEquals(member.getUpdatedAt(), persistedMember.getUpdatedAt());
    }

    @Test
    public void givenValidCastMember_whenUpdate_shouldRefresh() {
        final var expectedName = "Lucas";
        final var expectedType = CastMemberType.ACTOR;

        final var member = CastMember.newMember("Geo", CastMemberType.DIRECTOR);
        final var expectedId = member.getId();

        final var currentMember = castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(member));

        Assertions.assertEquals(1, castMemberRepository.count());
        Assertions.assertEquals("Geo", currentMember.getName());
        Assertions.assertEquals(CastMemberType.DIRECTOR, currentMember.getType());

        final var actualMember = castMemberGateway.update(
                CastMember.with(member).update(expectedName, expectedType)
        );

        Assertions.assertEquals(1, castMemberRepository.count());

        Assertions.assertEquals(expectedId, actualMember.getId());
        Assertions.assertEquals(expectedName, actualMember.getName());
        Assertions.assertEquals(expectedType, actualMember.getType());
        Assertions.assertEquals(member.getCreatedAt(), actualMember.getCreatedAt());
        Assertions.assertTrue(member.getUpdatedAt().isBefore(actualMember.getUpdatedAt()));

        final var persistedMember = castMemberRepository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(expectedId.getValue(), persistedMember.getId());
        Assertions.assertEquals(expectedName, persistedMember.getName());
        Assertions.assertEquals(expectedType, persistedMember.getType());
        Assertions.assertEquals(member.getCreatedAt(), persistedMember.getCreatedAt());
        Assertions.assertTrue(member.getUpdatedAt().isBefore(persistedMember.getUpdatedAt()));
    }

    @Test
    public void givenTwoCastMembersAndOnePersisted_whenExistsByIds_shouldReturnPersistedID() {
        final var member = CastMember.newMember("Geo", CastMemberType.DIRECTOR);

        final var expectedItems = 1;
        final var expectedId = member.getId();

        Assertions.assertEquals(0, castMemberRepository.count());

        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(member));

        final var actualMember = castMemberGateway.existsByIds(List.of(CastMemberID.from("123"), expectedId));

        Assertions.assertEquals(expectedItems, actualMember.size());
        Assertions.assertEquals(expectedId.getValue().trim(), actualMember.get(0).getValue().trim());
    }


    @Test
    public void givenValidCastMember_whenDeleteById_shouldDeleteIt() {
        final var member = CastMember.newMember("Lucas", CastMemberType.ACTOR);

        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(member));

        Assertions.assertEquals(1, castMemberRepository.count());

        castMemberGateway.deleteById(member.getId());

        Assertions.assertEquals(0, castMemberRepository.count());
    }

    @Test
    public void givenInvalidId_whenDeleteById_shouldBeIgnored() {
        final var member = CastMember.newMember("Lucas", CastMemberType.ACTOR);

        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(member));

        Assertions.assertEquals(1, castMemberRepository.count());

        castMemberGateway.deleteById(CastMemberID.from("123"));

        Assertions.assertEquals(1, castMemberRepository.count());
    }

    @Test
    public void givenValidCastMember_whenFindById_shouldReturn() {
        final var expectedName = "Lucas";
        final var expectedType = CastMemberType.ACTOR;

        final var member = CastMember.newMember(expectedName, expectedType);
        final var expectedId = member.getId();

        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(member));

        Assertions.assertEquals(1, castMemberRepository.count());

        final var actualMember = castMemberGateway.findById(expectedId).get();

        Assertions.assertEquals(expectedId, actualMember.getId());
        Assertions.assertEquals(expectedName, actualMember.getName());
        Assertions.assertEquals(expectedType, actualMember.getType());
        Assertions.assertEquals(member.getCreatedAt(), actualMember.getCreatedAt());
        Assertions.assertEquals(member.getUpdatedAt(), actualMember.getUpdatedAt());
    }

    @Test
    public void givenInvalidId_whenFindById_shouldReturnEmpty() {
        final var member = CastMember.newMember("Lucas", CastMemberType.ACTOR);

        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(member));

        Assertions.assertEquals(1, castMemberRepository.count());

        final var actualMember = castMemberGateway.findById(CastMemberID.from("123"));

        Assertions.assertTrue(actualMember.isEmpty());
    }

    @Test
    public void givenEmptyCastMembers_whenFindAll_shouldReturnEmpty() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;

        final var query =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualPage = castMemberGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedTotal, actualPage.items().size());
    }

    @ParameterizedTest
    @CsvSource({
            "vin,0,10,1,1,Vin Diesel",
            "taran,0,10,1,1,Quentin Tarantino",
            "jas,0,10,1,1,Jason Momoa",
            "har,0,10,1,1,Kit Harington",
            "MAR,0,10,1,1,Martin Scorsese",
    })
    public void givenValidTerm_whenFindAll_shouldReturnFiltered(
            final String expectedTerms,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedName
    ) {
        mockMembers();

        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var query =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualPage = castMemberGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedItemsCount, actualPage.items().size());
        Assertions.assertEquals(expectedName, actualPage.items().get(0).getName());
    }

    @ParameterizedTest
    @CsvSource({
            "name,asc,0,10,5,5,Jason Momoa",
            "name,desc,0,10,5,5,Vin Diesel",
            "createdAt,asc,0,10,5,5,Kit Harington",
            "createdAt,desc,0,10,5,5,Martin Scorsese",
    })
    public void givenValidSortAndDirection_whenFindAll_shouldReturnSorted(
            final String expectedSort,
            final String expectedDirection,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedName
    ) {
        mockMembers();

        final var expectedTerms = "";

        final var query =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualPage = castMemberGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedItemsCount, actualPage.items().size());
        Assertions.assertEquals(expectedName, actualPage.items().get(0).getName());
    }

    @ParameterizedTest
    @CsvSource({
            "0,2,2,5,Jason Momoa;Kit Harington",
            "1,2,2,5,Martin Scorsese;Quentin Tarantino",
            "2,2,1,5,Vin Diesel",
    })
    public void givenValidPagination_whenFindAll_shouldReturnPaginated(
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedNames
    ) {
        mockMembers();

        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var query =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualPage = castMemberGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedItemsCount, actualPage.items().size());

        int index = 0;
        for (final var expectedName : expectedNames.split(";")) {
            Assertions.assertEquals(expectedName, actualPage.items().get(index).getName());
            index++;
        }
    }

    private void mockMembers() {
        final var members = Stream.of(
                        CastMember.newMember("Kit Harington", CastMemberType.ACTOR),
                        CastMember.newMember("Vin Diesel", CastMemberType.ACTOR),
                        CastMember.newMember("Quentin Tarantino", CastMemberType.DIRECTOR),
                        CastMember.newMember("Jason Momoa", CastMemberType.ACTOR),
                        CastMember.newMember("Martin Scorsese", CastMemberType.DIRECTOR)
                ).map(CastMemberJpaEntity::from)
                .toList();

        members.forEach(member -> {
            member.setCreatedAt(InstantUtils.now());
            castMemberRepository.saveAndFlush(member);
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }
}