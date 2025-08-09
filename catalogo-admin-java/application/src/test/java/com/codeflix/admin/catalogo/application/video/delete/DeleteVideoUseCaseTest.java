package com.codeflix.admin.catalogo.application.video.delete;

import com.codeflix.admin.catalogo.application.UseCaseTest;
import com.codeflix.admin.catalogo.domain.exceptions.InternalErrorException;
import com.codeflix.admin.catalogo.domain.video.MediaResourceGateway;
import com.codeflix.admin.catalogo.domain.video.VideoGateway;
import com.codeflix.admin.catalogo.domain.video.VideoID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

public class DeleteVideoUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultDeleteVideoUseCase useCase;

    @Mock
    private VideoGateway videoGateway;

    @Mock
    private MediaResourceGateway mediaResourceGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(videoGateway, mediaResourceGateway);
    }

    @Test
    public void givenAValidId_whenCallsDeleteVideo_shouldDeleteIt() {
        final var expectedId = VideoID.unique();

        Mockito.doNothing()
                .when(videoGateway).deleteById(Mockito.any());

        Mockito.doNothing()
                .when(mediaResourceGateway).clearResources(Mockito.any());

        Assertions.assertDoesNotThrow(() -> this.useCase.execute(expectedId.getValue()));

        Mockito.verify(videoGateway).deleteById(Mockito.eq(expectedId));
        Mockito.verify(mediaResourceGateway).clearResources(Mockito.eq(expectedId));
    }

    @Test
    public void givenAnInvalidId_whenCallsDeleteVideo_shouldBeOk() {
        final var expectedId = VideoID.from("1231");

        Mockito.doNothing()
                .when(videoGateway).deleteById(Mockito.any());

        Assertions.assertDoesNotThrow(() -> this.useCase.execute(expectedId.getValue()));

        Mockito.verify(videoGateway).deleteById(Mockito.eq(expectedId));
    }

    @Test
    public void givenAValidId_whenCallsDeleteVideoAndGatewayThrowsException_shouldReceiveException() {
        final var expectedId = VideoID.from("1231");

        Mockito.doThrow(InternalErrorException.with("Error on delete video", new RuntimeException()))
                .when(videoGateway).deleteById(Mockito.any());

        Assertions.assertThrows(
                InternalErrorException.class,
                () -> this.useCase.execute(expectedId.getValue())
        );

        Mockito.verify(videoGateway).deleteById(Mockito.eq(expectedId));
    }
}
