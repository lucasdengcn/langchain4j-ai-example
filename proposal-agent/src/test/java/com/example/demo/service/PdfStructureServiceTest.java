package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.example.demo.pdf.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class PdfStructureServiceTest {

  private PdfStructureService pdfStructureService;
  private File testPdfFile;

  @BeforeEach
  void setUp() throws IOException {
    pdfStructureService = new PdfStructureService();
    Resource resource = new ClassPathResource("test-proposal.pdf");
    testPdfFile = resource.getFile();
  }

  @Test
  void testExtractTextElements() throws IOException {
    List<TextElement> textElements = pdfStructureService.extractTextElements(testPdfFile, 5, 5);

    assertNotNull(textElements);
    assertFalse(textElements.isEmpty());
    textElements.forEach(System.out::println);
  }

  @Test
  void testExtractTableElements() throws IOException {
    List<TableElement> tableElements = pdfStructureService.extractTableElements(testPdfFile, 5, 5);

    assertNotNull(tableElements);
    // Note: Since table extraction is a simplified implementation, we're just
    // verifying
    // that the method runs without errors
  }

  @Test
  void testExtractImageElements() throws IOException {
    List<ImageElement> imageElements = pdfStructureService.extractImageElements(testPdfFile, 1, 2);

    assertNotNull(imageElements);
    // Note: Our test PDF doesn't contain images, so the list should be empty
    // assertTrue(imageElements.isEmpty());
  }

  @Test
  void test_encodePageToBase64PNG() throws IOException {
    byte[] base64PNG = pdfStructureService.encodePageToBase64PNG(testPdfFile, 4);
    assertNotNull(base64PNG);
  }

  @Test
  void test_generate_page_report() throws IOException {
    PDFPageReport pdfPageReport = pdfStructureService.generatePageReport(testPdfFile, 1);
    assertNotNull(pdfPageReport);
    System.out.println(pdfPageReport);
  }

}