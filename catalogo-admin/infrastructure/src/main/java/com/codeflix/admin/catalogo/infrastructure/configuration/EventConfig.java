package com.codeflix.admin.catalogo.infrastructure.configuration;

import com.codeflix.admin.catalogo.infrastructure.configuration.properties.amqp.QueueProperties;
import com.codeflix.admin.catalogo.infrastructure.configuration.annotations.VideoCreatedQueue;
import com.codeflix.admin.catalogo.infrastructure.services.EventService;
import com.codeflix.admin.catalogo.infrastructure.services.impl.RabbitEventService;
import com.codeflix.admin.catalogo.infrastructure.services.local.InMemoryEventService;
import org.springframework.amqp.rabbit.core.RabbitOperations;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class EventConfig {

    @Bean
    @VideoCreatedQueue
    @Profile({"development"})
    EventService localVideoCreatedEventService() {
        return new InMemoryEventService();
    }

    @Bean
    @VideoCreatedQueue
    @ConditionalOnMissingBean
    EventService videoCreatedEventService(
            @VideoCreatedQueue final QueueProperties props,
            final RabbitOperations rabbitOperations
    ) {
        return new RabbitEventService(props.getExchange(), props.getRoutingKey(), rabbitOperations);
    }
}