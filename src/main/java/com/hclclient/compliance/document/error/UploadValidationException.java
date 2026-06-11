package com.hclclient.compliance.document.error;

public class UploadValidationException extends RuntimeException {
  private final ErrorCode errorCode;
  public UploadValidationException(ErrorCode errorCode) { super(errorCode.defaultMessage()); this.errorCode = errorCode; }
  public UploadValidationException(ErrorCode errorCode, String message) { super(message); this.errorCode = errorCode; }
  public ErrorCode getErrorCode() { return errorCode; }
}
