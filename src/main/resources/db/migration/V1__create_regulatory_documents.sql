CREATE TABLE regulatory_documents (
  document_id UUID PRIMARY KEY,
  original_filename VARCHAR(255) NOT NULL,
  content_type VARCHAR(100) NOT NULL,
  file_size_bytes BIGINT NOT NULL,
  checksum_sha256 VARCHAR(64) NOT NULL,
  storage_reference VARCHAR(512) NOT NULL,
  storage_key VARCHAR(512) NOT NULL,
  status VARCHAR(50) NOT NULL,
  jurisdiction VARCHAR(100),
  document_title VARCHAR(255),
  effective_date DATE,
  source_reference VARCHAR(512),
  uploaded_by VARCHAR(255) NOT NULL,
  uploaded_at TIMESTAMP NOT NULL,
  trace_id VARCHAR(100) NOT NULL
);
CREATE INDEX idx_regulatory_documents_checksum ON regulatory_documents (checksum_sha256);
CREATE INDEX idx_regulatory_documents_status ON regulatory_documents (status);
