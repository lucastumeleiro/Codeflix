package com.codeflix.admin.catalogo.infrastructure.amqp;

import com.codeflix.admin.catalogo.application.video.media.update.UpdateMediaStatusCommand;
import com.codeflix.admin.catalogo.application.video.media.update.UpdateMediaStatusUseCase;
import com.codeflix.admin.catalogo.domain.video.MediaStatus;
import com.codeflix.admin.catalogo.infrastructure.configuration.json.Json;
import com.codeflix.admin.catalogo.infrastructure.video.models.VideoEncoderCompleted;
import com.codeflix.admin.catalogo.infrastructure.video.models.VideoEncoderError;
import com.codeflix.admin.catalogo.infrastructure.video.models.VideoEncoderResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class VideoEncoderListener {

    private static final Logger log = LoggerFactory.getLogger(VideoEncoderListener.class);

    static final String LISTENER_ID = "videoEncodedListener";

    private final UpdateMediaStatusUseCase updateMediaStatusUseCase;

    public VideoEncoderListener(final UpdateMediaStatusUseCase updateMediaStatusUseCase) {
        this.updateMediaStatusUseCase = Objects.requireNonNull(updateMediaStatusUseCase);
    }

    @RabbitListener(id = LISTENER_ID, queues = "${amqp.queues.video-encoded.queue}")
    public void onVideoEncodedMessage(@Payload final String message) {
        final var result = Json.readValue(message, VideoEncoderResult.class);

        if (result instanceof VideoEncoderCompleted dto) {
            log.error("[message:video.listener.income] [status:completed] [payload:{}]", message);
            final var command = new UpdateMediaStatusCommand(
                    MediaStatus.COMPLETED,
                    dto.id(),
                    dto.video().resourceId(),
                    dto.video().encodedVideoFolder(),
                    dto.video().filePath()
            );

            this.updateMediaStatusUseCase.execute(command);
        } else if (result instanceof VideoEncoderError) {
            log.error("[message:video.listener.income] [status:error] [payload:{}]", message);
        } else {
            log.error("[message:video.listener.income] [status:unknown] [payload:{}]", message);
        }
    }
}
