package com.hclclient.compliance.document.error;

import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

@RestControllerAdvice
public class GlobalExceptionHandler {
  private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
  @ExceptionHandler(UploadValidationException.class) public ResponseEntity<ApiErrorResponse> handleUpload(UploadValidationException ex, HttpServletRequest req) { return build(ex.getErrorCode(), ex.getMessage(), req); }
  @ExceptionHandler(MissingServletRequestPartException.class) public ResponseEntity<ApiErrorResponse> handleMissing(MissingServletRequestPartException ex, HttpServletRequest req) { return build(ErrorCode.PDF_FILE_REQUIRED, ErrorCode.PDF_FILE_REQUIRED.defaultMessage(), req); }
  @ExceptionHandler(MaxUploadSizeExceededException.class) public ResponseEntity<ApiErrorResponse> handleMax(MaxUploadSizeExceededException ex, HttpServletRequest req) { return build(ErrorCode.PDF_FILE_TOO_LARGE, ErrorCode.PDF_FILE_TOO_LARGE.defaultMessage(), req); }
  @ExceptionHandler(DocumentStorageException.class) public ResponseEntity<ApiErrorResponse> handleStorage(DocumentStorageException ex, HttpServletRequest req) { log.error("Document storage failure traceId={}", traceId(), ex); return build(ErrorCode.DOCUMENT_STORAGE_FAILED, ErrorCode.DOCUMENT_STORAGE_FAILED.defaultMessage(), req); }
  @ExceptionHandler(DocumentMetadataException.class) public ResponseEntity<ApiErrorResponse> handleMetadata(DocumentMetadataException ex, HttpServletRequest req) { log.error("Document metadata failure traceId={}", traceId(), ex); return build(ErrorCode.DOCUMENT_METADATA_FAILED, ErrorCode.DOCUMENT_METADATA_FAILED.defaultMessage(), req); }
  @ExceptionHandler(DocumentNotFoundException.class) public ResponseEntity<ApiErrorResponse> handleNotFound(DocumentNotFoundException ex, HttpServletRequest req) { return build(ErrorCode.DOCUMENT_NOT_FOUND, ErrorCode.DOCUMENT_NOT_FOUND.defaultMessage(), req); }
  @ExceptionHandler(Exception.class) public ResponseEntity<ApiErrorResponse> handleUnexpected(Exception ex, HttpServletRequest req) { log.error("Unexpected service failure traceId={}", traceId(), ex); return build(ErrorCode.INTERNAL_ERROR, ErrorCode.INTERNAL_ERROR.defaultMessage(), req); }
  private ResponseEntity<ApiErrorResponse> build(ErrorCode code, String message, HttpServletRequest req) { HttpStatus s = code.httpStatus(); return ResponseEntity.status(s).body(new ApiErrorResponse(Instant.now(), s.value(), code.name(), message, req.getRequestURI(), traceId())); }
  private String traceId() { String id = MDC.get("traceId"); return id == null || id.isBlank() ? "unknown" : id; }
}
