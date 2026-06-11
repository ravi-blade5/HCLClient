package com.hclclient.compliance.document.storage;

import com.hclclient.compliance.document.config.StorageProperties;
import com.hclclient.compliance.document.error.DocumentStorageException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

@Service
@ConditionalOnProperty(name = "app.storage.type", havingValue = "s3")
public class S3DocumentStorageService implements DocumentStorageService {
  private static final Logger log = LoggerFactory.getLogger(S3DocumentStorageService.class); private final S3Client s3; private final String bucket; private final String prefix;
  public S3DocumentStorageService(StorageProperties props) { StorageProperties.S3 cfg = props.getS3(); if (cfg.getBucket() == null || cfg.getBucket().isBlank()) throw new DocumentStorageException("S3 bucket must be configured when app.storage.type=s3."); bucket=cfg.getBucket(); prefix=normalize(cfg.getPrefix()); s3=S3Client.builder().region(Region.of(cfg.getRegion())).credentialsProvider(DefaultCredentialsProvider.create()).build(); }
  public DocumentStorageResult store(UUID id, String filename, MultipartFile file) { LocalDate now=LocalDate.now(); String key="%s/%d/%02d/%02d/%s.pdf".formatted(prefix,now.getYear(),now.getMonthValue(),now.getDayOfMonth(),id); try { PutObjectRequest req=PutObjectRequest.builder().bucket(bucket).key(key).contentType(file.getContentType()).contentLength(file.getSize()).serverSideEncryption(ServerSideEncryption.AES256).metadata(java.util.Map.of("original-filename", filename, "document-id", id.toString())).build(); s3.putObject(req, RequestBody.fromInputStream(file.getInputStream(), file.getSize())); log.info("Stored regulatory document in object storage documentId={} storageKey={} sizeBytes={}", id, key, file.getSize()); return new DocumentStorageResult(key, "s3://" + bucket + "/" + key, file.getSize()); } catch (IOException | RuntimeException ex) { throw new DocumentStorageException("Unable to store regulatory PDF in object storage.", ex); } }
  public void delete(String key) { if (key == null || key.isBlank()) return; try { s3.deleteObject(DeleteObjectRequest.builder().bucket(bucket).key(key).build()); } catch (RuntimeException ex) { throw new DocumentStorageException("Unable to delete regulatory PDF from object storage.", ex); } }
  private String normalize(String v) { return v == null || v.isBlank() ? "regulatory-documents" : v.replaceAll("^/+", "").replaceAll("/+$", ""); }
}
