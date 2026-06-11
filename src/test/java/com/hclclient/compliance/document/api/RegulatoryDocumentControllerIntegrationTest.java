package com.hclclient.compliance.document.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hclclient.compliance.document.persistence.RegulatoryDocumentRepository;
import com.hclclient.compliance.document.support.TestPdfFixtures;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class RegulatoryDocumentControllerIntegrationTest {
  @Autowired MockMvc mockMvc; @Autowired RegulatoryDocumentRepository repository; @Autowired ObjectMapper objectMapper;
  @Test void shouldUploadValidPdfAndRetrieveMetadata() throws Exception { MockMultipartFile file=new MockMultipartFile("file","state-retail-minimum-inventory-rules.pdf","application/pdf", TestPdfFixtures.validPdfBytes()); MvcResult result=mockMvc.perform(multipart("/api/v1/regulatory-documents").file(file).param("jurisdiction","State of Example").param("documentTitle","Retail Minimum Inventory Rules").param("effectiveDate","2026-01-15").param("sourceReference","State publication reference 2026-01").header("X-Trace-Id","trace-aif82-valid-001")).andExpect(status().isAccepted()).andExpect(header().string("X-Trace-Id","trace-aif82-valid-001")).andExpect(jsonPath("$.documentId").exists()).andExpect(jsonPath("$.status").value("READY_FOR_EXTRACTION")).andExpect(jsonPath("$.traceId").value("trace-aif82-valid-001")).andReturn(); String response=result.getResponse().getContentAsString(StandardCharsets.UTF_8); UUID id=UUID.fromString(objectMapper.readTree(response).get("documentId").asText()); mockMvc.perform(get("/api/v1/regulatory-documents/{documentId}", id).header("X-Trace-Id","trace-aif82-retrieve-001")).andExpect(status().isOk()).andExpect(jsonPath("$.documentId").value(id.toString())).andExpect(jsonPath("$.status").value("READY_FOR_EXTRACTION")).andExpect(jsonPath("$.traceId").value("trace-aif82-retrieve-001")); assertThat(repository.findById(id)).isPresent(); }
  @Test void shouldReturnStructuredErrorForMissingFile() throws Exception { mockMvc.perform(multipart("/api/v1/regulatory-documents").header("X-Trace-Id","trace-aif82-missing-file-001")).andExpect(status().isBadRequest()).andExpect(jsonPath("$.errorCode").value("PDF_FILE_REQUIRED")).andExpect(jsonPath("$.traceId").value("trace-aif82-missing-file-001")); }
  @Test void shouldReturnStructuredErrorForEmptyFile() throws Exception { MockMultipartFile file=new MockMultipartFile("file","empty-file.pdf","application/pdf", new byte[0]); mockMvc.perform(multipart("/api/v1/regulatory-documents").file(file).header("X-Trace-Id","trace-aif82-empty-file-001")).andExpect(status().isBadRequest()).andExpect(jsonPath("$.errorCode").value("PDF_FILE_EMPTY")); }
  @Test void shouldReturnStructuredErrorForInvalidMimeType() throws Exception { MockMultipartFile file=new MockMultipartFile("file","regulatory-rules.pdf","text/plain", TestPdfFixtures.validPdfBytes()); mockMvc.perform(multipart("/api/v1/regulatory-documents").file(file).header("X-Trace-Id","trace-aif82-invalid-mime-001")).andExpect(status().isUnsupportedMediaType()).andExpect(jsonPath("$.errorCode").value("UNSUPPORTED_FILE_TYPE")); }
  @Test void shouldReturnStructuredErrorForInvalidPdfSignature() throws Exception { MockMultipartFile file=new MockMultipartFile("file","fake-regulation.pdf","application/pdf", "not-a-pdf".getBytes(StandardCharsets.UTF_8)); mockMvc.perform(multipart("/api/v1/regulatory-documents").file(file).header("X-Trace-Id","trace-aif82-invalid-signature-001")).andExpect(status().isUnsupportedMediaType()).andExpect(jsonPath("$.errorCode").value("INVALID_PDF_SIGNATURE")); }
  @Test void shouldRejectOversizedFile() throws Exception { byte[] oversized=new byte[12*1024]; oversized[0]='%'; oversized[1]='P'; oversized[2]='D'; oversized[3]='F'; oversized[4]='-'; MockMultipartFile file=new MockMultipartFile("file","oversized-regulation.pdf","application/pdf", oversized); mockMvc.perform(multipart("/api/v1/regulatory-documents").file(file).header("X-Trace-Id","trace-aif82-oversized-001")).andExpect(status().isPayloadTooLarge()).andExpect(jsonPath("$.errorCode").value("PDF_FILE_TOO_LARGE")); }
  @Test void shouldSanitizeUnsafeFilenameAndAcceptUpload() throws Exception { MockMultipartFile file=new MockMultipartFile("file","../state rules final.pdf","application/pdf", TestPdfFixtures.validPdfBytes()); mockMvc.perform(multipart("/api/v1/regulatory-documents").file(file).header("X-Trace-Id","trace-aif82-sanitize-001")).andExpect(status().isAccepted()).andExpect(jsonPath("$.originalFilename").value("state_rules_final.pdf")); }
  @Test void shouldReturnNotFoundForUnknownDocumentId() throws Exception { mockMvc.perform(get("/api/v1/regulatory-documents/{documentId}", UUID.randomUUID()).header("X-Trace-Id","trace-aif82-notfound-001")).andExpect(status().isNotFound()).andExpect(jsonPath("$.errorCode").value("DOCUMENT_NOT_FOUND")); }
}
