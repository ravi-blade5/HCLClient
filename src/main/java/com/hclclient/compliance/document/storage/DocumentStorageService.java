package com.hclclient.compliance.document.storage;

import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface DocumentStorageService { DocumentStorageResult store(UUID documentId, String sanitizedFilename, MultipartFile file); void delete(String storageKey); }
