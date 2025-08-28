package com.danieldomingues.lms.web.error;

import java.time.Instant;
import java.util.List;

public record ApiError(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path,
        List<FieldErrorItem> fieldErrors
) {
    public static record FieldErrorItem(String field, String message) {}
}
