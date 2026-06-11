package com.hclclient.compliance.document.service;

import com.hclclient.compliance.document.api.dto.RegulatoryDocumentResponse;
import com.hclclient.compliance.document.audit.AuditEventService;
import com.hclclient.compliance.document.error.*;
import com.hclclient.compliance.document.model.DocumentStatus;
import com.hclclient.compliance.document.model.UploadMetadata;
import com.hclclient.compliance.document.persistence.RegulatoryDocumentEntity;
import com.hclclient.compliance.document.persistence.RegulatoryDocumentRepository;
import com.hclclient.compliance.document.storage.DocumentStorageResult;
import com.hclclient.compliance.document.storage.DocumentStorageService;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.HexFormat;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class RegulatoryDocumentService {
  private static final Logger log = LoggerFactory.getLogger(RegulatoryDocumentService.class);
  private final PdfValidationService validator; private final DocumentStorageService storage; private final RegulatoryDocumentRepository repository; private final AuditEventService audit; private final ProcessingHandoffService handoff; private final MeterRegistry meterRegistry;
  public RegulatoryDocumentService(PdfValidationService validator, DocumentStorageService storage, RegulatoryDocumentRepository repository, AuditEventService audit, ProcessingHandoffService handoff, MeterRegistry meterRegistry) { this.validator=validator; this.storage=storage; this.repository=repository; this.audit=audit; this.handoff=handoff; this.meterRegistry=meterRegistry; }
  @Transactional public RegulatoryDocumentResponse upload(MultipartFile file, UploadMetadata metadata, String uploadedBy, String traceId) {
    Timer.Sample sample = Timer.start(meterRegistry); meterRegistry.counter("regulatory_document_upload_requests_total").increment(); UUID id = UUID.randomUUID(); DocumentStorageResult stored = null;
    try {
      PdfValidationResult vr = validator.validateAndSanitize(file); String checksum = sha256(file); stored = storage.store(id, vr.sanitizedFilename(), file); DocumentStatus status = handoff.markReadyForExtraction(id, traceId);
      RegulatoryDocumentEntity entity = new RegulatoryDocumentEntity(id, vr.sanitizedFilename(), file.getContentType(), file.getSize(), checksum, stored.storageReference(), stored.storageKey(), status, truncate(clean(metadata.jurisdiction()),100), truncate(clean(metadata.documentTitle()),255), metadata.effectiveDate(), truncate(clean(metadata.sourceReference()),512), defaulted(truncate(clean(uploadedBy),255),"system"), Instant.now(), defaulted(truncate(clean(traceId),100),"unknown"));
      RegulatoryDocumentEntity saved = repository.save(entity); audit.uploadSucceeded(id, uploadedBy, traceId); meterRegistry.counter("regulatory_document_upload_success_total").increment(); log.info("Regulatory document upload accepted documentId={} status={} sizeBytes={} traceId={}", id, status, file.getSize(), traceId); return RegulatoryDocumentResponse.fromEntity(saved, traceId);
    } catch (UploadValidationException ex) { audit.uploadFailed(uploadedBy, traceId, ex.getErrorCode().name()); meterRegistry.counter("regulatory_document_upload_validation_failures_total", "errorCode", ex.getErrorCode().name()).increment(); throw ex; }
      catch (DataAccessException ex) { cleanup(stored, traceId); audit.uploadFailed(uploadedBy, traceId, "DOCUMENT_METADATA_FAILED"); throw new DocumentMetadataException("Unable to persist regulatory document metadata.", ex); }
      catch (RuntimeException ex) { cleanup(stored, traceId); audit.uploadFailed(uploadedBy, traceId, "DOCUMENT_UPLOAD_FAILED"); throw ex; }
      finally { sample.stop(meterRegistry.timer("regulatory_document_upload_latency_seconds")); }
  }
  @Transactional(readOnly = true) public RegulatoryDocumentResponse getByDocumentId(UUID id, String traceId) { return RegulatoryDocumentResponse.fromEntity(repository.findById(id).orElseThrow(() -> new DocumentNotFoundException(id)), traceId); }
  private String sha256(MultipartFile file) { try { MessageDigest digest = MessageDigest.getInstance("SHA-256"); try (InputStream in = file.getInputStream(); DigestInputStream din = new DigestInputStream(in, digest)) { din.transferTo(OutputStream.nullOutputStream()); } return HexFormat.of().formatHex(digest.digest()); } catch (IOException | NoSuchAlgorithmException ex) { throw new UploadValidationException(ErrorCode.INVALID_PDF_SIGNATURE, "Unable to read uploaded PDF for checksum calculation."); } }
  private void cleanup(DocumentStorageResult r, String traceId) { if (r == null) return; try { storage.delete(r.storageKey()); } catch (RuntimeException ex) { log.warn("Compensating storage cleanup failed storageKey={} traceId={}", r.storageKey(), traceId, ex); } }
  private String clean(String v) { return v == null || v.isBlank() ? null : v.strip().replaceAll("[\\r\\n\\t]", " "); }
  private String truncate(String v, int max) { return v == null || v.length() <= max ? v : v.substring(0, max); }
  private String defaulted(String v, String d) { return v == null ? d : v; }
}
