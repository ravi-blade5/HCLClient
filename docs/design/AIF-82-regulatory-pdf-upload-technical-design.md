# AIF-82 Regulatory PDF Upload Technical Design

## Selected scope
- Epic: AIF-79 Regulatory Document Ingestion and Rule Extraction
- Story: AIF-82 Upload regulatory PDF for processing
- Technology: Java 17, Spring Boot 3, Maven
- Runtime: Kubernetes

## Design objective
Implement a Java-based regulatory PDF upload capability that validates uploaded PDFs, stores source documents through a storage abstraction, persists upload metadata, records audit events, returns structured errors, and marks accepted documents ready for future extraction processing.

## Components
- RegulatoryDocumentController
- RegulatoryDocumentService
- PdfValidationService
- DocumentStorageService
- RegulatoryDocumentRepository
- AuditEventService
- ProcessingHandoffService
- GlobalExceptionHandler

## API
- POST `/api/v1/regulatory-documents`
- GET `/api/v1/regulatory-documents/{documentId}`

## Traceability
Requirements: BR-001, BR-003 downstream readiness, BR-015, NFR-002, NFR-005, NFR-006, NFR-008, NFR-009.
