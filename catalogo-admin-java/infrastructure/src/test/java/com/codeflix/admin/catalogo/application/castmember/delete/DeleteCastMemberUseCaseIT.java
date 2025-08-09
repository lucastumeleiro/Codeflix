package com.codeflix.admin.catalogo.application.castmember.delete;

import com.codeflix.admin.catalogo.IntegrationTest;
import com.codeflix.admin.catalogo.domain.castmember.CastMember;
import com.codeflix.admin.catalogo.domain.castmember.CastMemberGateway;
import com.codeflix.admin.catalogo.domain.castmember.CastMemberID;
import com.codeflix.admin.catalogo.domain.castmember.CastMemberType;
import com.codeflix.admin.catalogo.infrastructure.castmember.persistence.CastMemberJpaEntity;
import com.codeflix.admin.catalogo.infrastructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

@IntegrationTest
public class DeleteCastMemberUseCaseIT {

    @Autowired
    private DeleteCastMemberUseCase useCase;

    @Autowired
    private CastMemberRepository castMemberRepository;

    @MockitoSpyBean
    private CastMemberGateway castMemberGateway;

    @BeforeEach
    void cleanUp() {
        castMemberRepository.deleteAll();
    }

    @Test
    public void givenValidId_whenDeleteCastMember_shouldDeleteIt() {
        final var member = CastMember.newMember("Lucas", CastMemberType.ACTOR);
        final var memberTwo = CastMember.newMember("Lucas", CastMemberType.ACTOR);

        final var expectedId = member.getId();

        this.castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(member));
        this.castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(memberTwo));

        Assertions.assertEquals(2, this.castMemberRepository.count());

        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        Mockito.verify(castMemberGateway).deleteById(Mockito.eq(expectedId));

        Assertions.assertEquals(1, this.castMemberRepository.count());
        Assertions.assertFalse(this.castMemberRepository.existsById(expectedId.getValue()));
        Assertions.assertTrue(this.castMemberRepository.existsById(memberTwo.getId().getValue()));
    }

    @Test
    public void givenInvalidId_whenDeleteCastMember_shouldBeOk() {
        castMemberRepository.saveAndFlush(
                CastMemberJpaEntity.from(
                        CastMember.newMember("Lucas", CastMemberType.ACTOR)
                )
        );

        final var expectedId = CastMemberID.from("123");

        Assertions.assertEquals(1, castMemberRepository.count());

        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        Mockito.verify(castMemberGateway).deleteById(Mockito.eq(expectedId));

        Assertions.assertEquals(1, castMemberRepository.count());
    }

    @Test
    public void givenValidId_whenDeleteCastMemberAndGatewayThrowsException_shouldReceiveException() {
        final var member = CastMember.newMember("Lucas", CastMemberType.ACTOR);

        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(member));

        final var expectedId = member.getId();

        Assertions.assertEquals(1, castMemberRepository.count());

        Mockito.doThrow(new IllegalStateException("Gateway error"))
                .when(castMemberGateway).deleteById(Mockito.any());

        Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));

        Mockito.verify(castMemberGateway).deleteById(Mockito.eq(expectedId));

        Assertions.assertEquals(1, castMemberRepository.count());
    }
}
