# AIF-82 Regulatory Document Upload API

## Upload regulatory PDF

`POST /api/v1/regulatory-documents`

Content type: `multipart/form-data`

Parts:
- `file` required PDF
- `jurisdiction` optional string
- `documentTitle` optional string
- `