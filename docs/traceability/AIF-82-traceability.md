# AIF-82 Traceability Matrix

## Jira scope
- Epic: AIF-79 — Regulatory Document Ingestion and Rule Extraction
- Story: AIF-82 — Upload regulatory PDF for processing

## Requirement coverage
| Requirement | Implementation artifacts |
|---|---|
| BR-001 | Upload API, PDF validation, storage, metadata persistence |
| BR-003 | Downstream readiness status only via ProcessingHandoffService |
| BR-015 | Audit logging and metadata timestamps/uploader/trace ID |
| NFR-002 | Traceable metadata, audit log events, checksum |
| NFR-005 | Security config, filename sanitization, PDF validation, secret templates only |
| NFR-006 | Validation and structured error handling |
| NFR-008 | Trace ID filter, structured logs, metrics, health endpoints |
| NFR-009 | Dockerfile and Kubernetes manifests |

## Files
- `src/main/java/com/hclclient/compliance/**`
- `src/test/java/com/hclclient/compliance/**`
- `src/test/resources/**`
- `deploy/k8s/**`
- `docs/design/AIF-82-regulatory-pdf-upload-technical-design.md`
- `docs/api/AIF-82-regulatory-document-upload-api.md`

No secrets, tokens, credentials, or real customer/regulatory content are included.
