package com.example.demo.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PdfVLInstructServiceTest {

    @Autowired
    private PdfVLInstructService pdfVLInstructService;

    @Test
    void test_process_proposal_pdf() throws IOException {
        Resource resource = new ClassPathResource("test-proposal.pdf");
        File testPdfFile = resource.getFile();
        //
        pdfVLInstructService.processPdf(testPdfFile.getAbsolutePath(), 5);
    }

    @Test
    void test_ocr_image() throws IOException {
        Resource resource = new ClassPathResource("page_4.png");
        File testPdfFile = resource.getFile();
        //
        pdfVLInstructService.ocrImageFile(testPdfFile.getAbsolutePath());
    }
}