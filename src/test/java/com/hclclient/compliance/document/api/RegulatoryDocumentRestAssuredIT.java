package com.hclclient.compliance.document.api;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import com.hclclient.compliance.document.support.TestPdfFixtures;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Disabled("Optional REST-assured execution path; enable when pipeline supports live server execution.")
class RegulatoryDocumentRestAssuredIT {
  @LocalServerPort int port;
  @Test void shouldUploadValidPdf() { given().port(port).header("X-Trace-Id","trace-aif82-restassured-001").multiPart("file","regulatory.pdf", TestPdfFixtures.validPdfBytes(), "application/pdf").multiPart("jurisdiction","State of Example").contentType(ContentType.MULTIPART).when().post("/api/v1/regulatory-documents").then().statusCode(202).body("documentId", notNullValue()).body("status", equalTo("READY_FOR_EXTRACTION")); }
}
