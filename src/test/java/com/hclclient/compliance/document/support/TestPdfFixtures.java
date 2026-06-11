package com.hclclient.compliance.document.support;

import java.nio.charset.StandardCharsets;

public final class TestPdfFixtures {
  private TestPdfFixtures() {}
  public static byte[] validPdfBytes() { return "%PDF-1.4\n% Synthetic minimal PDF fixture for AIF-82\n1 0 obj\n<< /Type /Catalog >>\nendobj\n%%EOF\n".getBytes(StandardCharsets.UTF_8); }
  public static byte[] invalidSignatureBytes() { return "NOT_A_PDF_SIGNATURE".getBytes(StandardCharsets.UTF_8); }
  public static byte[] emptyBytes() { return new byte[0]; }
}
