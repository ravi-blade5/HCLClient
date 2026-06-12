package com.hclclient.compliance.document.persistence;

import com.hclclient.compliance.document.model.DocumentStatus;
import jakarta.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "regulatory_documents", indexes = {@Index(name = "idx_regulatory_documents_checksum", columnList = "checksum_sha256"), @Index(name = "idx_regulatory_documents_status", columnList = "status")})
public class RegulatoryDocumentEntity {
  @Id @Column(name = "document_id", nullable = false, updatable = false) private UUID documentId;
  @Column(name = "original_filename", nullable = false, length = 255) private String originalFilename;
  @Column(name = "content_type", nullable = false, length = 100) private String contentType;
  @Column(name = "file_size_bytes", nullable = false) private long fileSizeBytes;
  @Column(name = "checksum_sha256", nullable = false, length = 64) private String checksumSha256;
  @Column(name = "storage_reference", nullable = false, length = 512) private String storageReference;
  @Column(name = "storage_key", nullable = false, length = 512) private String storageKey;
  @Enumerated(EnumType.STRING) @Column(name = "status", nullable = false, length = 50) private DocumentStatus status;
  @Column(name = "jurisdiction", length = 100) private String jurisdiction;
  @Column(name = "document_title", length = 255) private String documentTitle;
  @Column(name = "effective_date") private LocalDate effectiveDate;
  @Column(name = "source_reference", length = 512) private String sourceReference;
  @Column(name = "uploaded_by", nullable = false, length = 255) private String uploadedBy;
  @Column(name = "uploaded_at", nullable = false) private Instant uploadedAt;
  @Column(name = "trace_id", nullable = false, length = 100) private String traceId;
  protected RegulatoryDocumentEntity() {}
  public RegulatoryDocumentEntity(UUID documentId, String originalFilename, String contentType, long fileSizeBytes, String checksumSha256, String storageReference, String storageKey, DocumentStatus status, String jurisdiction, String documentTitle, LocalDate effectiveDate, String sourceReference, String uploadedBy, Instant uploadedAt, String traceId) { this.documentId=documentId; this.originalFilename=originalFilename; this.contentType=contentType; this.fileSizeBytes=fileSizeBytes; this.checksumSha256=checksumSha256; this.storageReference=storageReference; this.storageKey=storageKey; this.status=status; this.jurisdiction=jurisdiction; this.documentTitle=documentTitle; this.effectiveDate=effectiveDate; this.sourceReference=sourceReference; this.uploadedBy=uploadedBy; this.uploadedAt=uploadedAt; this.traceId=traceId; }
  public UUID getDocumentId(){return documentId;} public String getOriginalFilename(){return originalFilename;} public String getContentType(){return contentType;} public long getFileSizeBytes(){return fileSizeBytes;} public String getChecksumSha256(){return checksumSha256;} public String getStorageReference(){return storageReference;} public String getStorageKey(){return storageKey;} public DocumentStatus getStatus(){return status;} public String getJurisdiction(){return jurisdiction;} public String getDocumentTitle(){return documentTitle;} public LocalDate getEffectiveDate(){return effectiveDate;} public String getSourceReference(){return sourceReference;} public String getUploadedBy(){return uploadedBy;} public Instant getUploadedAt(){return uploadedAt;} public String getTraceId(){return traceId;}
}
