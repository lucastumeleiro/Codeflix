package com.codeflix.admin.catalogo.infrastructure.utils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public final class InstantUtils {
    private InstantUtils() {
    }

    public static Instant now() {
        return Instant.now().truncatedTo(ChronoUnit.MICROS);
    }
}
