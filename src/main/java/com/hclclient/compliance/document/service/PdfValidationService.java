package com.hclclient.compliance.document.service;

import com.hclclient.compliance.document.config.UploadProperties;
import com.hclclient.compliance.document.error.ErrorCode;
import com.hclclient.compliance.document.error.UploadValidationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PdfValidationService {
  private static final byte[] PDF_SIGNATURE = new byte[]{0x25,0x50,0x44,0x46,0x2D};
  private final UploadProperties properties;
  public PdfValidationService(UploadProperties properties) { this.properties = properties; }
  public PdfValidationResult validateAndSanitize(MultipartFile file) {
    if (file == null) throw new UploadValidationException(ErrorCode.PDF_FILE_REQUIRED);
    if (file.isEmpty() || file.getSize() <= 0) throw new UploadValidationException(ErrorCode.PDF_FILE_EMPTY);
    if (file.getSize() > properties.getMaxFileSize().toBytes()) throw new UploadValidationException(ErrorCode.PDF_FILE_TOO_LARGE);
    String name = sanitizeFilename(file.getOriginalFilename());
    if (!name.toLowerCase(Locale.ROOT).endsWith(".pdf")) throw new UploadValidationException(ErrorCode.UNSUPPORTED_FILE_TYPE);
    String type = file.getContentType();
    if (type == null || !properties.getAllowedContentTypes().contains(type.toLowerCase(Locale.ROOT))) throw new UploadValidationException(ErrorCode.UNSUPPORTED_FILE_TYPE);
    if (!hasPdfSignature(file)) throw new UploadValidationException(ErrorCode.INVALID_PDF_SIGNATURE);
    return new PdfValidationResult(name);
  }
  private boolean hasPdfSignature(MultipartFile file) {
    byte[] header = new byte[PDF_SIGNATURE.length];
    try (InputStream in = file.getInputStream()) { int read = in.read(header); if (read < PDF_SIGNATURE.length) return false; for (int i=0;i<PDF_SIGNATURE.length;i++) if (header[i] != PDF_SIGNATURE[i]) return false; return true; }
    catch (IOException ex) { throw new UploadValidationException(ErrorCode.INVALID_PDF_SIGNATURE); }
  }
  private String sanitizeFilename(String original) {
    if (original == null || original.isBlank()) throw new UploadValidationException(ErrorCode.UNSUPPORTED_FILE_TYPE);
    String cleaned = StringUtils.cleanPath(original).replace('\\','/');
    String base = cleaned.substring(cleaned.lastIndexOf('/') + 1);
    String sanitized = base.replaceAll("[^A-Za-z0-9._-]", "_");
    if (sanitized.isBlank() || sanitized.equals(".") || sanitized.equals("..")) throw new UploadValidationException(ErrorCode.UNSUPPORTED_FILE_TYPE);
    return sanitized.length() > 255 ? sanitized.substring(sanitized.length() - 255) : sanitized;
  }
}
