package com.hclclient.compliance.document.error;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
  PDF_FILE_REQUIRED(HttpStatus.BAD_REQUEST, "Regulatory PDF file is required."),
  PDF_FILE_EMPTY(HttpStatus.BAD_REQUEST, "Regulatory PDF file must not be empty."),
  PDF_FILE_TOO_LARGE(HttpStatus.PAYLOAD_TOO_LARGE, "Regulatory PDF file exceeds the configured maximum size."),
  UNSUPPORTED_FILE_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Only PDF uploads are supported."),
  INVALID_PDF_SIGNATURE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Uploaded file does not contain a valid PDF signature."),
  DOCUMENT_STORAGE_FAILED(HttpStatus.SERVICE_UNAVAILABLE, "Document storage is currently unavailable."),
  DOCUMENT_METADATA_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Document metadata could not be persisted."),
  DOCUMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "Regulatory document was not found."),
  INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected server error.");
  private final HttpStatus status; private final String msg;
  ErrorCode(HttpStatus status, String msg) { this.status = status; this.msg = msg; }
  public HttpStatus httpStatus() { return status; }
  public String defaultMessage() { return msg; }
}
