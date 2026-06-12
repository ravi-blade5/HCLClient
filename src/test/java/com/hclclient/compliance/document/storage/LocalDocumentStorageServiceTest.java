package com.hclclient.compliance.document.storage;

import static org.assertj.core.api.Assertions.assertThat;
import com.hclclient.compliance.document.config.StorageProperties;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockMultipartFile;

class LocalDocumentStorageServiceTest {
  @TempDir Path tempDir;
  @Test void shouldStoreAndDeleteFileUsingGeneratedStorageKey() throws Exception { StorageProperties p=new StorageProperties(); p.setLocalRootLocation(tempDir.toString()); LocalDocumentStorageService s=new LocalDocumentStorageService(p); UUID id=UUID.randomUUID(); MockMultipartFile f=new MockMultipartFile("file","rules.pdf","application/pdf","%PDF-1.4\n".getBytes()); DocumentStorageResult r=s.store(id,"rules.pdf",f); assertThat(r.storageKey()).contains(id.toString()); assertThat(r.storageReference()).startsWith("local://"); assertThat(Files.exists(tempDir.resolve(r.storageKey()))).isTrue(); s.delete(r.storageKey()); assertThat(Files.exists(tempDir.resolve(r.storageKey()))).isFalse(); }
}
