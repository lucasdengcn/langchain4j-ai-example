package com.example.demo.service;

import com.example.demo.pdf.PDFPageReport;
import com.example.demo.pdf.PdfStructureService;
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

    @Autowired
    private PdfStructureService pdfStructureService;

    @Test
    void test_process_proposal_pdf() throws IOException {
        Resource resource = new ClassPathResource("test-proposal.pdf");
        File testPdfFile = resource.getFile();
        //
        pdfVLInstructService.processPdf(testPdfFile.getAbsolutePath(), 5);
    }

    @Test
    void test_anchor_text() throws IOException {
        Resource resource = new ClassPathResource("test-proposal.pdf");
        File testPdfFile = resource.getFile();
        //
        PDFPageReport pdfReport = pdfStructureService.generatePageReport(testPdfFile, 5);
        String s = pdfVLInstructService.buildAnchorText(pdfReport);
        pdfVLInstructService.saveResponseToFile(s);
    }

    @Test
    void test_ocr_image() throws IOException {
        Resource resource = new ClassPathResource("page_4.png");
        File testPdfFile = resource.getFile();
        //
        pdfVLInstructService.ocrImageFile(testPdfFile.getAbsolutePath());
    }
}