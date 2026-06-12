package com.hclclient.compliance.document.error;

import java.util.UUID;
public class DocumentNotFoundException extends RuntimeException { public DocumentNotFoundException(UUID id) { super("Regulatory document was not found for documentId=" + id); } }
