package com.hclclient.compliance.document.storage;

import com.hclclient.compliance.document.config.StorageProperties;
import com.hclclient.compliance.document.error.DocumentStorageException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@ConditionalOnProperty(name = "app.storage.type", havingValue = "local", matchIfMissing = true)
public class LocalDocumentStorageService implements DocumentStorageService {
  private static final Logger log = LoggerFactory.getLogger(LocalDocumentStorageService.class); private final Path root;
  public LocalDocumentStorageService(StorageProperties props) { this.root = Path.of(props.getLocalRootLocation()).toAbsolutePath().normalize(); try { Files.createDirectories(root); } catch (IOException ex) { throw new DocumentStorageException("Unable to initialize local document storage.", ex); } }
  public DocumentStorageResult store(UUID id, String filename, MultipartFile file) { LocalDate now = LocalDate.now(); String key = "%d/%02d/%02d/%s.pdf".formatted(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), id); Path target = root.resolve(key).normalize(); if (!target.startsWith(root)) throw new DocumentStorageException("Invalid storage key."); try { Files.createDirectories(target.getParent()); try (InputStream in = file.getInputStream()) { Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING); } log.info("Stored regulatory document documentId={} storageKey={} sizeBytes={}", id, key, file.getSize()); return new DocumentStorageResult(key, "local://" + key, file.getSize()); } catch (IOException ex) { throw new DocumentStorageException("Unable to store regulatory PDF.", ex); } }
  public void delete(String key) { if (key == null || key.isBlank()) return; Path target = root.resolve(key).normalize(); if (!target.startsWith(root)) throw new DocumentStorageException("Invalid storage key."); try { Files.deleteIfExists(target); } catch (IOException ex) { throw new DocumentStorageException("Unable to delete stored regulatory PDF.", ex); } }
}
