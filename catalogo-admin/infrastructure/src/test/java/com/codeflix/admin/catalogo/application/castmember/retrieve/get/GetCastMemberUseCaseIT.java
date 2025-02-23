package com.codeflix.admin.catalogo.application.castmember.retrieve.get;

import com.codeflix.admin.catalogo.IntegrationTest;
import com.codeflix.admin.catalogo.domain.castmember.CastMember;
import com.codeflix.admin.catalogo.domain.castmember.CastMemberGateway;
import com.codeflix.admin.catalogo.domain.castmember.CastMemberID;
import com.codeflix.admin.catalogo.domain.castmember.CastMemberType;
import com.codeflix.admin.catalogo.domain.exceptions.NotFoundException;
import com.codeflix.admin.catalogo.infrastructure.castmember.persistence.CastMemberJpaEntity;
import com.codeflix.admin.catalogo.infrastructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

@IntegrationTest
public class GetCastMemberUseCaseIT {

    @Autowired
    private GetCastMemberUseCase useCase;

    @Autowired
    private CastMemberRepository castMemberRepository;

    @MockitoSpyBean
    private CastMemberGateway castMemberGateway;

    @Test
    public void givenValidId_whenGetCastMember_shouldReturnIt() {
        final var expectedName = "Lucas";
        final var expectedType = CastMemberType.ACTOR;

        final var member = CastMember.newMember(expectedName, expectedType);

        final var expectedId = member.getId();

        this.castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(member));

        Assertions.assertEquals(1, this.castMemberRepository.count());

        final var actualOutput = useCase.execute(expectedId.getValue());

        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedId.getValue(), actualOutput.id());
        Assertions.assertEquals(expectedName, actualOutput.name());
        Assertions.assertEquals(expectedType, actualOutput.type());
        Assertions.assertEquals(member.getCreatedAt(), actualOutput.createdAt());
        Assertions.assertEquals(member.getUpdatedAt(), actualOutput.updatedAt());

        Mockito.verify(castMemberGateway).findById(Mockito.any());
    }

    @Test
    public void givenInvalidId_whenGetCastMemberAndDoesNotExists_shouldReturnNotFoundException() {
        final var expectedId = CastMemberID.from("123");

        final var expectedErrorMessage = "CastMember with ID 123 was not found";

        final var actualOutput = Assertions.assertThrows(NotFoundException.class, () -> {
            useCase.execute(expectedId.getValue());
        });

        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedErrorMessage, actualOutput.getMessage());

        Mockito.verify(castMemberGateway).findById(Mockito.eq(expectedId));
    }
}
