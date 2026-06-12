package com.hclclient.compliance.document.error;

import java.time.Instant;

public record ApiErrorResponse(Instant timestamp, int status, String errorCode, String message, String path, String traceId) {}
