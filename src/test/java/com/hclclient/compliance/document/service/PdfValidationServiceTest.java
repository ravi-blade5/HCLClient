package com.hclclient.compliance.document.service;

import static org.assertj.core.api.Assertions.*;
import com.hclclient.compliance.document.config.UploadProperties;
import com.hclclient.compliance.document.error.ErrorCode;
import com.hclclient.compliance.document.error.UploadValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.unit.DataSize;

class PdfValidationServiceTest {
  private PdfValidationService service;
  @BeforeEach void setUp(){ UploadProperties p=new UploadProperties(); p.setMaxFileSize(DataSize.ofKilobytes(10)); service=new PdfValidationService(p); }
  @Test void shouldAcceptValidPdf(){ MockMultipartFile f=new MockMultipartFile("file","regulation.pdf","application/pdf","%PDF-1.4\n".getBytes()); assertThat(service.validateAndSanitize(f).sanitizedFilename()).isEqualTo("regulation.pdf"); }
  @Test void shouldRejectMissingFile(){ assertThatThrownBy(() -> service.validateAndSanitize(null)).isInstanceOf(UploadValidationException.class).extracting("errorCode").isEqualTo(ErrorCode.PDF_FILE_REQUIRED); }
  @Test void shouldRejectEmptyFile(){ MockMultipartFile f=new MockMultipartFile("file","empty.pdf","application/pdf",new byte[0]); assertThatThrownBy(() -> service.validateAndSanitize(f)).isInstanceOf(UploadValidationException.class).extracting("errorCode").isEqualTo(ErrorCode.PDF_FILE_EMPTY); }
  @Test void shouldRejectNonPdfExtension(){ MockMultipartFile f=new MockMultipartFile("file","regulation.txt","application/pdf","%PDF-1.4\n".getBytes()); assertThatThrownBy(() -> service.validateAndSanitize(f)).isInstanceOf(UploadValidationException.class).extracting("errorCode").isEqualTo(ErrorCode.UNSUPPORTED_FILE_TYPE); }
  @Test void shouldRejectInvalidMimeType(){ MockMultipartFile f=new MockMultipartFile("file","regulation.pdf","text/plain","%PDF-1.4\n".getBytes()); assertThatThrownBy(() -> service.validateAndSanitize(f)).isInstanceOf(UploadValidationException.class).extracting("errorCode").isEqualTo(ErrorCode.UNSUPPORTED_FILE_TYPE); }
  @Test void shouldRejectInvalidPdfSignature(){ MockMultipartFile f=new MockMultipartFile("file","regulation.pdf","application/pdf","not a pdf".getBytes()); assertThatThrownBy(() -> service.validateAndSanitize(f)).isInstanceOf(UploadValidationException.class).extracting("errorCode").isEqualTo(ErrorCode.INVALID_PDF_SIGNATURE); }
  @Test void shouldRejectOversizedFile(){ byte[] c=new byte[11*1024]; c[0]='%'; c[1]='P'; c[2]='D'; c[3]='F'; c[4]='-'; MockMultipartFile f=new MockMultipartFile("file","large.pdf","application/pdf",c); assertThatThrownBy(() -> service.validateAndSanitize(f)).isInstanceOf(UploadValidationException.class).extracting("errorCode").isEqualTo(ErrorCode.PDF_FILE_TOO_LARGE); }
  @Test void shouldSanitizeUnsafeFilename(){ MockMultipartFile f=new MockMultipartFile("file","../state rules final.pdf","application/pdf","%PDF-1.4\n".getBytes()); assertThat(service.validateAndSanitize(f).sanitizedFilename()).isEqualTo("state_rules_final.pdf"); }
}
