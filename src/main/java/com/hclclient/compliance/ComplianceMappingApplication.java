package com.hclclient.compliance;

import com.hclclient.compliance.document.config.SecurityProperties;
import com.hclclient.compliance.document.config.StorageProperties;
import com.hclclient.compliance.document.config.UploadProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/** Traceability: Epic AIF-79, Story AIF-82. */
@SpringBootApplication
@EnableConfigurationProperties({UploadProperties.class, StorageProperties.class, SecurityProperties.class})
public class ComplianceMappingApplication {
  public static void main(String[] args) { SpringApplication.run(ComplianceMappingApplication.class, args); }
}
