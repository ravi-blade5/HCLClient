package com.hclclient.compliance.document.config;

import java.util.LinkedHashSet;
import java.util.Set;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.unit.DataSize;

@ConfigurationProperties(prefix = "app.upload")
public class UploadProperties {
  private DataSize maxFileSize = DataSize.ofMegabytes(25);
  private Set<String> allowedContentTypes = new LinkedHashSet<>(Set.of("application/pdf", "application/x-pdf"));
  public DataSize getMaxFileSize() { return maxFileSize; }
  public void setMaxFileSize(DataSize maxFileSize) { this.maxFileSize = maxFileSize; }
  public Set<String> getAllowedContentTypes() { return allowedContentTypes; }
  public void setAllowedContentTypes(Set<String> allowedContentTypes) { this.allowedContentTypes = allowedContentTypes; }
}
