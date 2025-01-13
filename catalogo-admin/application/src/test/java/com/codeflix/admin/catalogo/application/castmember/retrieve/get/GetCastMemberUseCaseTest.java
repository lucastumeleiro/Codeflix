package com.codeflix.admin.catalogo.application.castmember.retrieve.get;

import com.codeflix.admin.catalogo.application.UseCaseTest;
import com.codeflix.admin.catalogo.domain.castmember.CastMember;
import com.codeflix.admin.catalogo.domain.castmember.CastMemberGateway;
import com.codeflix.admin.catalogo.domain.castmember.CastMemberID;
import com.codeflix.admin.catalogo.domain.exceptions.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

public class GetCastMemberUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultGetCastMemberUseCase useCase;

    @Mock
    private CastMemberGateway castMemberGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(castMemberGateway);
    }

    @Test
    public void givenValidId_whenCallsGetCastMember_shouldReturnIt() {
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type();

        final var member = CastMember.newMember(expectedName, expectedType);

        final var expectedId = member.getId();

        Mockito.when(castMemberGateway.findById(Mockito.any()))
                .thenReturn(Optional.of(member));

        final var actualOutput = useCase.execute(expectedId.getValue());

        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedId.getValue(), actualOutput.id());
        Assertions.assertEquals(expectedName, actualOutput.name());
        Assertions.assertEquals(expectedType, actualOutput.type());
        Assertions.assertEquals(member.getCreatedAt(), actualOutput.createdAt());
        Assertions.assertEquals(member.getUpdatedAt(), actualOutput.updatedAt());

        Mockito.verify(castMemberGateway).findById(Mockito.eq(expectedId));
    }

    @Test
    public void givenInvalidId_whenCallsGetCastMemberAndDoesNotExists_shouldReturnNotFoundException() {
        final var expectedId = CastMemberID.from("123");

        final var expectedErrorMessage = "CastMember with ID 123 was not found";

        Mockito.when(castMemberGateway.findById(Mockito.any()))
                .thenReturn(Optional.empty());

        final var actualOutput = Assertions.assertThrows(NotFoundException.class, () -> useCase.execute(expectedId.getValue()));

        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedErrorMessage, actualOutput.getMessage());

        Mockito.verify(castMemberGateway).findById(Mockito.eq(expectedId));
    }
}
