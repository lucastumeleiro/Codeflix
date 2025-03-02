package com.codeflix.admin.catalogo.application.castmember.update;

import com.codeflix.admin.catalogo.application.UseCaseTest;
import com.codeflix.admin.catalogo.domain.Fixture;
import com.codeflix.admin.catalogo.domain.castmember.CastMember;
import com.codeflix.admin.catalogo.domain.castmember.CastMemberGateway;
import com.codeflix.admin.catalogo.domain.castmember.CastMemberID;
import com.codeflix.admin.catalogo.domain.castmember.CastMemberType;
import com.codeflix.admin.catalogo.domain.exceptions.NotFoundException;
import com.codeflix.admin.catalogo.domain.exceptions.NotificationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;

public class UpdateCastMemberUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultUpdateCastMemberUseCase useCase;

    @Mock
    private CastMemberGateway castMemberGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(castMemberGateway);
    }

    @Test
    public void givenValidCommand_whenCallsUpdateCastMember_shouldReturnItsIdentifier() {
        final var member = CastMember.newMember("vin diesel", CastMemberType.DIRECTOR);

        final var expectedId = member.getId();
        final var expectedName = Fixture.name();
        final var expectedType = CastMemberType.ACTOR;

        final var command = UpdateCastMemberCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedType
        );

        Mockito.when(castMemberGateway.findById(Mockito.any()))
                .thenReturn(Optional.of(CastMember.with(member)));

        Mockito.when(castMemberGateway.update(Mockito.any()))
                .thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(command);

        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedId.getValue(), actualOutput.id());

        Mockito.verify(castMemberGateway).findById(Mockito.eq(expectedId));

        Mockito.verify(castMemberGateway).update(Mockito.argThat(aUpdatedMember ->
                Objects.equals(expectedId, aUpdatedMember.getId())
                        && Objects.equals(expectedName, aUpdatedMember.getName())
                        && Objects.equals(expectedType, aUpdatedMember.getType())
                        && Objects.equals(member.getCreatedAt(), aUpdatedMember.getCreatedAt())
                        && member.getUpdatedAt().isBefore(aUpdatedMember.getUpdatedAt())
        ));
    }

    @Test
    public void givenInvalidName_whenCallsUpdateCastMember_shouldThrowsNotificationException() {
        final var member = CastMember.newMember("vin diesel", CastMemberType.DIRECTOR);

        final var expectedId = member.getId();
        final String expectedName = null;
        final var expectedType = CastMemberType.ACTOR;

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var command = UpdateCastMemberCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedType
        );

        Mockito.when(castMemberGateway.findById(Mockito.any()))
                .thenReturn(Optional.of(member));

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> useCase.execute(command));

        Assertions.assertNotNull(actualException);

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        Mockito.verify(castMemberGateway).findById(Mockito.eq(expectedId));
        Mockito.verify(castMemberGateway, Mockito.times(0)).update(Mockito.any());
    }

    @Test
    public void givenInvalidType_whenCallsUpdateCastMember_shouldThrowsNotificationException() {
        final var member = CastMember.newMember("vin diesel", CastMemberType.DIRECTOR);

        final var expectedId = member.getId();
        final var expectedName = Fixture.name();
        final CastMemberType expectedType = null;

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'type' should not be null";

        final var command = UpdateCastMemberCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedType
        );

        Mockito.when(castMemberGateway.findById(Mockito.any()))
                .thenReturn(Optional.of(member));

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> useCase.execute(command));

        Assertions.assertNotNull(actualException);

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        Mockito.verify(castMemberGateway).findById(Mockito.eq(expectedId));
        Mockito.verify(castMemberGateway, Mockito.times(0)).update(Mockito.any());
    }

    @Test
    public void givenInvalidId_whenCallsUpdateCastMember_shouldThrowsNotFoundException() {
        final var member = CastMember.newMember("vin diesel", CastMemberType.DIRECTOR);

        final var expectedId = CastMemberID.from("123");
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type();

        final var expectedErrorMessage = "CastMember with ID 123 was not found";

        final var command = UpdateCastMemberCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedType
        );

        Mockito.when(castMemberGateway.findById(Mockito.any()))
                .thenReturn(Optional.empty());

        final var actualException = Assertions.assertThrows(NotFoundException.class, () -> useCase.execute(command));

        Assertions.assertNotNull(actualException);

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        Mockito.verify(castMemberGateway).findById(Mockito.eq(expectedId));
        Mockito.verify(castMemberGateway, Mockito.times(0)).update(Mockito.any());
    }
}

