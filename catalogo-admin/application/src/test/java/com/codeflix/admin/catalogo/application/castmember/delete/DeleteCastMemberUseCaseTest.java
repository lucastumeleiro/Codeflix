package com.codeflix.admin.catalogo.application.castmember.delete;

import com.codeflix.admin.catalogo.application.UseCaseTest;
import com.codeflix.admin.catalogo.domain.Fixture;
import com.codeflix.admin.catalogo.domain.castmember.CastMember;
import com.codeflix.admin.catalogo.domain.castmember.CastMemberGateway;
import com.codeflix.admin.catalogo.domain.castmember.CastMemberID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

public class DeleteCastMemberUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultDeleteCastMemberUseCase useCase;

    @Mock
    private CastMemberGateway castMemberGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(castMemberGateway);
    }

    @Test
    public void givenValidId_whenCallsDeleteCastMember_shouldDeleteIt() {
        final var member = CastMember.newMember(Fixture.name(), Fixture.CastMembers.type());

        final var expectedId = member.getId();

        Mockito.doNothing()
                .when(castMemberGateway).deleteById(Mockito.any());

        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        Mockito.verify(castMemberGateway).deleteById(Mockito.eq(expectedId));
    }

    @Test
    public void givenInvalidId_whenCallsDeleteCastMember_shouldBeOk() {
        final var expectedId = CastMemberID.from("123");

        Mockito.doNothing()
                .when(castMemberGateway).deleteById(Mockito.any());

        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        Mockito.verify(castMemberGateway).deleteById(Mockito.eq(expectedId));
    }

    @Test
    public void givenValidId_whenCallsDeleteCastMemberAndGatewayThrowsException_shouldReceiveException() {
        final var member = CastMember.newMember(Fixture.name(), Fixture.CastMembers.type());

        final var expectedId = member.getId();

        Mockito.doThrow(new IllegalStateException("Gateway error"))
                .when(castMemberGateway).deleteById(Mockito.any());

        Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));

        Mockito.verify(castMemberGateway).deleteById(Mockito.eq(expectedId));
    }
}
