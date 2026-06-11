package com.hclclient.compliance.document.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.storage")
public class StorageProperties {
  private String type = "local";
  private String localRootLocation = "/tmp/hclclient/regulatory-documents";
  private S3 s3 = new S3();
  public String getType() { return type; }
  public void setType(String type) { this.type = type; }
  public String getLocalRootLocation() { return localRootLocation; }
  public void setLocalRootLocation(String localRootLocation) { this.localRootLocation = localRootLocation; }
  public S3 getS3() { return s3; }
  public void setS3(S3 s3) { this.s3 = s3; }
  public static class S3 {
    private String bucket;
    private String region = "us-east-1";
    private String prefix = "regulatory-documents";
    public String getBucket() { return bucket; }
    public void setBucket(String bucket) { this.bucket = bucket; }
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
    public String getPrefix() { return prefix; }
    public void setPrefix(String prefix) { this.prefix = prefix; }
  }
}
