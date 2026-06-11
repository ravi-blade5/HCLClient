package com.hclclient.compliance.document.service;

import com.hclclient.compliance.document.model.DocumentStatus;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ProcessingHandoffService {
  private static final Logger log = LoggerFactory.getLogger(ProcessingHandoffService.class);
  public DocumentStatus markReadyForExtraction(UUID documentId, String traceId) { log.info("Document ready for future extraction documentId={} traceId={}", documentId, traceId); return DocumentStatus.READY_FOR_EXTRACTION; }
}
