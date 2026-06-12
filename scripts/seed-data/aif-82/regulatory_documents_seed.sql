-- Synthetic seed data for AIF-79 / AIF-82. No real customer or regulatory content included.
INSERT INTO regulatory_documents (
  document_id, original_filename, content_type, file_size_bytes, checksum_sha256, storage_reference, storage_key, status,
  jurisdiction, document_title, effective_date, source_reference, uploaded_by, uploaded_at, trace_id
) VALUES (
  '2bb6f2f0-6df3-4cdd-8c3a-7bcd78c7b101',
  'state-retail-minimum-inventory-rules.pdf',
  'application/pdf',
  1542,
  '7b3f0d2f2c3f9e7a9f1d0a11d24e8b5df3f4b9d2d1c8e1c6b2d9f4c0a6e5a112',
  's3://synthetic-bucket/regulatory-documents/2026/01/15/2bb6f2f0-6df3-4cdd-8c3a-7bcd78c7b101.pdf',
  'regulatory-documents/2026/01/15/2bb6f2f0-6df3-4cdd-8c3a-7bcd78c7b101.pdf',
  'READY_FOR_EXTRACTION',
  'State of Example',
  'Retail Minimum Inventory Rules',
  DATE '2026-01-15',
  'State publication reference 2026-01',
  'compliance.bot',
  TIMESTAMP '2026-01-15 10:15:30',
  'trace-aif82-valid-001'
);
