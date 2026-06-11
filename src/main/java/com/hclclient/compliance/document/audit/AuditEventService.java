package com.hclclient.compliance.document.audit;

import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AuditEventService {
  private static final Logger AUDIT_LOG = LoggerFactory.getLogger("AUDIT");
  public void uploadSucceeded(UUID documentId, String uploadedBy, String traceId) { AUDIT_LOG.info("eventType=REGULATORY_DOCUMENT_UPLOAD outcome=SUCCESS documentId={} actor={} traceId={}", documentId, safe(uploadedBy), safe(traceId)); }
  public void uploadFailed(String uploadedBy, String traceId, String errorCode) { AUDIT_LOG.warn("eventType=REGULATORY_DOCUMENT_UPLOAD outcome=FAILURE actor={} traceId={} errorCode={}", safe(uploadedBy), safe(traceId), safe(errorCode)); }
  private String safe(String v) { return v == null || v.isBlank() ? "unknown" : v.replaceAll("[\\r\\n\\t]", "_"); }
}
