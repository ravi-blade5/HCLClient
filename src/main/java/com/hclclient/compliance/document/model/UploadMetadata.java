package com.hclclient.compliance.document.model;

import java.time.LocalDate;
public record UploadMetadata(String jurisdiction, String documentTitle, LocalDate effectiveDate, String sourceReference) {}
