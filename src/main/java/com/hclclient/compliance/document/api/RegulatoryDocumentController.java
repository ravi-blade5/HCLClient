package com.hclclient.compliance.document.api;

import com.hclclient.compliance.document.api.dto.RegulatoryDocumentResponse;
import com.hclclient.compliance.document.model.UploadMetadata;
import com.hclclient.compliance.document.service.RegulatoryDocumentService;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.time.LocalDate;
import java.util.UUID;
import org.slf4j.MDC;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/regulatory-documents")
public class RegulatoryDocumentController {
  private final RegulatoryDocumentService service;
  public RegulatoryDocumentController(RegulatoryDocumentService service) { this.service = service; }

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<RegulatoryDocumentResponse> uploadRegulatoryPdf(
      @RequestPart(value = "file", required = false) MultipartFile file,
      @RequestParam(value = "jurisdiction", required = false) String jurisdiction,
      @RequestParam(value = "documentTitle", required = false) String documentTitle,
      @RequestParam(value = "effectiveDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate effectiveDate,
      @RequestParam(value = "sourceReference", required = false) String sourceReference,
      Principal principal,
      HttpServletRequest request) {
    String uploadedBy = principal == null ? "system" : principal.getName();
    RegulatoryDocumentResponse response = service.upload(file, new UploadMetadata(jurisdiction, documentTitle, effectiveDate, sourceReference), uploadedBy, traceId(request));
    return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
  }

  @GetMapping(value = "/{documentId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<RegulatoryDocumentResponse> getRegulatoryDocument(@PathVariable UUID documentId, HttpServletRequest request) {
    return ResponseEntity.ok(service.getByDocumentId(documentId, traceId(request)));
  }

  private String traceId(HttpServletRequest request) {
    String value = MDC.get("traceId");
    if (value != null && !value.isBlank()) return value;
    Object attr = request.getAttribute("traceId");
    return attr == null ? UUID.randomUUID().toString() : attr.toString();
  }
}
