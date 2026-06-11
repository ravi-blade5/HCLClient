# AIF-82 Integration Tests

Integration tests cover valid PDF upload, missing/empty file, invalid MIME type, invalid PDF signature, oversized file, filename sanitization, document not found, trace ID propagation, and structured error responses.

Traceability: AIF-79, AIF-82, BR-001, BR-015, NFR-002, NFR-005, NFR-006, NFR-008, NFR-009.

Run:

```bash
mvn test
```
