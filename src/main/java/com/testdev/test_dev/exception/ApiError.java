package com.testdev.test_dev.exception;

import java.time.Instant;

public record ApiError(Instant timestamp, int status, String error, String message) {
}
