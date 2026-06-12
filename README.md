# HCLClient Compliance Mapping Service

Traceability: Epic `AIF-79`, Story `AIF-82`.

This repository contains the Java 17 / Spring Boot 3 artifacts for regulatory PDF upload processing. The selected implementation slice supports multipart PDF upload, validation, storage abstraction, metadata persistence, audit logging, trace ID propagation, tests, Docker packaging, Kubernetes manifests, and traceability documentation.

## Run tests

```bash
mvn test
```

## Run locally

```bash
mvn spring-boot:run
```

## API

- `POST /api/v1/regulatory-documents`
- `GET /api/v1/regulatory-documents/{documentId}`

No real regulatory documents, secrets, tokens, or customer data are included.
