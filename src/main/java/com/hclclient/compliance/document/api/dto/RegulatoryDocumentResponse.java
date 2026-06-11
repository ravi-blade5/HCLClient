package com.hclclient.compliance.document.api.dto;

import com.hclclient.compliance.document.persistence.RegulatoryDocumentEntity;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record RegulatoryDocumentResponse(UUID documentId, String originalFilename, String contentType, long fileSizeBytes, String checksum, String status, Instant uploadedAt, String uploadedBy, String storageReference, String jurisdiction, String documentTitle, LocalDate effectiveDate, String sourceReference, String traceId) {
  public static RegulatoryDocumentResponse fromEntity(RegulatoryDocumentEntity e, String traceId) {
    return new RegulatoryDocumentResponse(e.getDocumentId(), e.getOriginalFilename(), e.getContentType(), e.getFileSizeBytes(), e.getChecksumSha256(), e.getStatus().name(), e.getUploadedAt(), e.getUploadedBy(), e.getStorageReference(), e.getJurisdiction(), e.getDocumentTitle(), e.getEffectiveDate(), e.getSourceReference(), traceId);
  }
}
