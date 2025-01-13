package com.codeflix.admin.catalogo.application.castmember.create;

import com.codeflix.admin.catalogo.application.UseCaseTest;
import com.codeflix.admin.catalogo.domain.castmember.CastMemberGateway;
import com.codeflix.admin.catalogo.domain.castmember.CastMemberType;
import com.codeflix.admin.catalogo.domain.exceptions.NotificationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Objects;

import static org.mockito.AdditionalAnswers.returnsFirstArg;

public class CreateCastMemberUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultCreateCastMemberUseCase useCase;
    @Mock
    private CastMemberGateway castMemberGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(castMemberGateway);
    }

    @Test
    public void givenValidCommand_whenCallsCreateCastMember_shouldReturnIt() {
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type();

        final var command = CreateCastMemberCommand.with(expectedName, expectedType);

        Mockito.when(castMemberGateway.create(Mockito.any()))
                .thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(command);

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Mockito.verify(castMemberGateway).create(Mockito.argThat(member ->
                Objects.nonNull(member.getId())
                        && Objects.equals(expectedName, member.getName())
                        && Objects.equals(expectedType, member.getType())
                        && Objects.nonNull(member.getCreatedAt())
                        && Objects.nonNull(member.getUpdatedAt())
        ));
    }

    @Test
    public void givenInvalidName_whenCallsCreateCastMember_shouldThrowsNotificationException() {
        final String expectedName = null;
        final var expectedType = Fixture.CastMembers.type();

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var command = CreateCastMemberCommand.with(expectedName, expectedType);

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> useCase.execute(command));

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        Mockito.verify(castMemberGateway, Mockito.times(0)).create(Mockito.any());
    }

    @Test
    public void givenInvalidType_whenCallsCreateCastMember_shouldThrowsNotificationException() {
        final var expectedName = Fixture.name();
        final CastMemberType expectedType = null;

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'type' should not be null";

        final var command = CreateCastMemberCommand.with(expectedName, expectedType);

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> useCase.execute(command));

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        Mockito.verify(castMemberGateway, Mockito.times(0)).create(Mockito.any());
    }
}