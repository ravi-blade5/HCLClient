# AIF-82 Regulatory Document Upload API

## Traceability
- Epic: AIF-79
- Story: AIF-82
- Requirements: BR-001, BR-015, NFR-002, NFR-005, NFR-006, NFR-008, NFR-009

## Upload regulatory PDF

`POST /api/v1/regulatory-documents`

Content type: `multipart/form-data`

Request parts:
- `file` required binary PDF
- `jurisdiction` optional string
- `documentTitle` optional string
- `effectiveDate` optional ISO date
- `sourceReference` optional string

Success response: `202 Accepted`

Response fields:
- `documentId`
- `originalFilename`
- `contentType`
- `fileSizeBytes`
- `checksum`
- `status`
- `uploadedAt`
- `uploadedBy`
- `storageReference`
- `traceId`

## Get document metadata

`GET /api/v1/regulatory-documents/{documentId}`

Success response: `200 OK`

## Structured error codes
- `PDF_FILE_REQUIRED`
- `PDF_FILE_EMPTY`
- `PDF_FILE_TOO_LARGE`
- `UNSUPPORTED_FILE_TYPE`
- `INVALID_PDF_SIGNATURE`
- `DOCUMENT_STORAGE_FAILED`
- `DOCUMENT_METADATA_FAILED`
- `DOCUMENT_NOT_FOUND`
