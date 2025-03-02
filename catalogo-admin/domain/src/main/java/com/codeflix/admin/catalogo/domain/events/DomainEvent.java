package com.codeflix.admin.catalogo.domain.events;

import java.io.Serializable;
import java.time.Instant;

public class DomainEvent extends Serializable {
    Instant occurredOn();
}
