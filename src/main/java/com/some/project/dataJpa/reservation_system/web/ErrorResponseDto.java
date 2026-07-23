package com.some.project.dataJpa.reservation_system.web;

import java.time.LocalDateTime;

public record ErrorResponseDto(
        String message,
        String errorMessage,
        LocalDateTime errorTime
) {
}
