package com.example.demo.pdf;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Base64;

import lombok.Getter;
import lombok.Setter;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.contentstream.PDFStreamEngine;
import org.apache.pdfbox.contentstream.operator.DrawObject;
import org.apache.pdfbox.contentstream.operator.state.Concatenate;
import org.apache.pdfbox.contentstream.operator.state.Restore;
import org.apache.pdfbox.contentstream.operator.state.Save;
import org.apache.pdfbox.contentstream.operator.state.SetMatrix;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;
import org.apache.pdfbox.util.Matrix;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;

@Service
public class PdfStructureService {

    /**
     * Extracts text elements from a PDF file within the specified page range.
     *
     * @param pdfFile   the PDF file to analyze
     * @param startPage the starting page number (1-based index)
     * @param endPage   the ending page number (1-based index)
     * @return a list of TextElement objects representing the extracted text
     * @throws IOException if an I/O error occurs
     */
    public List<TextElement> extractTextElements(File pdfFile, int startPage, int endPage) throws IOException {
        List<TextElement> textElements = new ArrayList<>();
        try (PDDocument document = loadPdfDocument(pdfFile)) {
            PDFTextStripper stripper = createTextStripper(textElements, startPage, endPage);
            stripper.getText(document);
        }
        return textElements;
    }

    /**
     * Extracts image elements from a PDF file within the specified page range.
     *
     * @param pdfFile   the PDF file to analyze
     * @param startPage the starting page number (1-based index)
     * @param endPage   the ending page number (1-based index)
     * @return a list of ImageElement objects representing the extracted images
     * @throws IOException if an I/O error occurs
     */
    public List<ImageElement> extractImageElements(File pdfFile, int startPage, int endPage) throws IOException {
        List<ImageElement> imageElements = new ArrayList<>();
        try (PDDocument document = loadPdfDocument(pdfFile)) {
            for (int pageIndex = startPage - 1; pageIndex < endPage; pageIndex++) {
                PDPage page = document.getPage(pageIndex);
                ImageExtractor imageExtractor = new ImageExtractor();
                imageExtractor.setCurrentPageNo(pageIndex + 1);
                imageExtractor.processPage(page);
                imageElements.addAll(imageExtractor.getImages());
            }
        }
        return imageElements;
    }

    /**
     * Extracts table elements from a PDF file within the specified page range.
     *
     * @param pdfFile   the PDF file to analyze
     * @param startPage the starting page number (1-based index)
     * @param endPage   the ending page number (1-based index)
     * @return a list of TableElement objects representing the extracted tables
     * @throws IOException if an I/O error occurs
     */
    public List<TableElement> extractTableElements(File pdfFile, int startPage, int endPage) throws IOException {
        List<TableElement> tables = new ArrayList<>();
        try (PDDocument document = loadPdfDocument(pdfFile)) {
            for (int pageIndex = startPage - 1; pageIndex < endPage; pageIndex++) {
                PDPage page = document.getPage(pageIndex);
                List<TextElement> textElements = extractTextElements(pdfFile, startPage, endPage);
                Map<Float, List<String>> rowMap = createRowMap(textElements, pageIndex + 1);
                List<List<String>> cells = new ArrayList<>(rowMap.values());
                if (!cells.isEmpty()) {
                    tables.add(new TableElement(cells, 0, 0, page.getMediaBox().getWidth(), page.getMediaBox().getHeight(), pageIndex + 1));
                }
            }
        }
        return tables;
    }

    /**
     * Extracts header and footer text from a PDF file within the specified page range.
     *
     * @param pdfFile   the PDF file to analyze
     * @param startPage the starting page number (1-based index)
     * @param endPage   the ending page number (1-based index)
     * @return a list of PageHeaderFooter objects representing the extracted headers and footers
     * @throws IOException if an I/O error occurs
     */
    public List<PageHeaderFooter> extractHeaderFooter(File pdfFile, int startPage, int endPage) throws IOException {
        List<PageHeaderFooter> headerFooters = new ArrayList<>();
        try (PDDocument document = loadPdfDocument(pdfFile)) {
            for (int pageIndex = startPage - 1; pageIndex < endPage; pageIndex++) {
                PDPage page = document.getPage(pageIndex);
                PDRectangle mediaBox = page.getMediaBox();
                float pageHeight = mediaBox.getHeight();
                final StringBuilder headerText = new StringBuilder();
                final StringBuilder footerText = new StringBuilder();

                PDFTextStripper stripper = createHeaderFooterStripper(pageHeight, headerText, footerText);
                stripper.setStartPage(pageIndex + 1);
                stripper.setEndPage(pageIndex + 1);
                stripper.getText(document);

                headerFooters.add(new PageHeaderFooter(headerText.toString().trim(), footerText.toString().trim(), pageIndex + 1));
            }
        }
        return headerFooters;
    }

    /**
     * Generates a detailed report for a specific page in a PDF file.
     * The report includes text elements, image elements, table elements, and header/footer information.
     *
     * @param pdfFile    the PDF file to analyze
     * @param pageNumber the page number to generate the report for (1-based index)
     * @return a PDFPageReport object containing the page analysis results
     * @throws IOException if an I/O error occurs
     */
    public PDFPageReport generatePageReport(File pdfFile, int pageNumber) throws IOException {
        PDFPageReport report = new PDFPageReport();
        try (PDDocument document = loadPdfDocument(pdfFile)) {
            PDPage page = document.getPage(pageNumber - 1);
            //
            report.setPageNumber(pageNumber);
            //
            extractPageInfo(page, report);
            // Extract text elements
            List<TextElement> textElements = new ArrayList<>();
            PDFTextStripper stripper = createTextStripper(textElements, pageNumber, pageNumber);
            stripper.getText(document);
            report.setTextElements(textElements);

            // Extract image elements
            List<ImageElement> imageElements = extractImageElements(pdfFile, pageNumber, pageNumber);
            report.setImageElements(imageElements);

            // Extract table elements
            List<TableElement> tables = new ArrayList<>();
            Map<Float, List<String>> rowMap = createRowMap(textElements, pageNumber);
            List<List<String>> cells = new ArrayList<>(rowMap.values());
            if (!cells.isEmpty()) {
                tables.add(new TableElement(cells, 0, 0, page.getMediaBox().getWidth(), page.getMediaBox().getHeight(), pageNumber));
            }
            report.setTableElements(tables);

            // Extract header and footer
            PDRectangle mediaBox = page.getMediaBox();
            float pageHeight = mediaBox.getHeight();
            final StringBuilder headerText = new StringBuilder();
            final StringBuilder footerText = new StringBuilder();
            PDFTextStripper headerFooterStripper = createHeaderFooterStripper(pageHeight, headerText, footerText);
            headerFooterStripper.setStartPage(pageNumber);
            headerFooterStripper.setEndPage(pageNumber);
            headerFooterStripper.getText(document);
            report.setHeader(headerText.toString().trim());
            report.setFooter(footerText.toString().trim());
            //
            report.setPageBase64PNG(renderPageToBase64PNG(pageNumber, document));
        }
        return report;
    }

    /**
     * PDF坐标系原点 (0,0) 通常位于页面左下角
     * X轴向右延伸，Y轴向上延伸
     * 元素的坐标位置（如文本、图片）可通过对应元素的 getX()/getY() 方法获取相对位置
     * @param page
     * @param report
     */
    private void extractPageInfo(PDPage page, PDFPageReport report) {
        PDRectangle mediaBox = page.getMediaBox();
        report.setWidth(mediaBox.getWidth());
        report.setHeight(mediaBox.getHeight());
        // 页面左下角坐标 (x0, y0)
        float x0 = mediaBox.getLowerLeftX();
        float y0 = mediaBox.getLowerLeftY();
        report.setX0(x0);
        report.setY0(y0);
        // 页面右上角坐标 (x1, y1)
        float x1 = mediaBox.getUpperRightX();
        float y1 = mediaBox.getUpperRightY();
        report.setX1(x1);
        report.setY1(y1);
    }

    /**
     * Encodes a specific page of a PDF file as a Base64-encoded PNG image.
     *
     * @param pdfFile    the PDF file to analyze
     * @param pageNumber the page number to encode (1-based index)
     * @return a Base64-encoded string representing the PNG image of the page
     * @throws IOException if an I/O error occurs
     */
    public byte[] encodePageToBase64PNG(File pdfFile, int pageNumber) throws IOException {
        try (PDDocument document = loadPdfDocument(pdfFile)) {
            byte[] bytes = renderPageToBase64PNG(pageNumber, document);
            // save bytes to a file
            Files.write(Paths.get("page_" + pageNumber + ".png"), bytes);
            return bytes;
        }
    }

    private static byte[] renderPageToBase64PNG(int pageNumber, PDDocument document) throws IOException {
        PDFRenderer renderer = new PDFRenderer(document);
        BufferedImage image = renderer.renderImage(pageNumber - 1);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        return baos.toByteArray();
    }

    private PDDocument loadPdfDocument(File pdfFile) throws IOException {
        if (pdfFile.length() > 10 * 1024 * 1024) {
            return Loader.loadPDF(pdfFile, IOUtils.createTempFileOnlyStreamCache());
        } else {
            return Loader.loadPDF(pdfFile);
        }
    }

    private PDFTextStripper createTextStripper(List<TextElement> textElements, int startPage, int endPage) {
        PDFTextStripper stripper = new PDFTextStripper() {
            @Override
            protected void processTextPosition(TextPosition text) {
                textElements.add(new TextElement(
                        text.getUnicode(),
                        text.getXDirAdj(),
                        text.getYDirAdj(),
                        text.getWidth(),
                        text.getHeight(),
                        getCurrentPageNo()));
            }
        };
        stripper.setSortByPosition(true);
        stripper.setStartPage(startPage);
        stripper.setEndPage(endPage);
        return stripper;
    }

    private Map<Float, List<String>> createRowMap(List<TextElement> textElements, int pageNumber) {
        Map<Float, List<String>> rowMap = new TreeMap<>();
        for (TextElement text : textElements) {
            if (text.getPageNumber() == pageNumber) {
                rowMap.computeIfAbsent(text.getY(), k -> new ArrayList<>()).add(text.getText());
            }
        }
        return rowMap;
    }

    private PDFTextStripper createHeaderFooterStripper(float pageHeight, StringBuilder headerText, StringBuilder footerText) {
        float headerRegionHeight = pageHeight * 0.1f;
        float footerRegionHeight = pageHeight * 0.1f;
        return new PDFTextStripper() {
            @Override
            protected void processTextPosition(TextPosition text) {
                float y = text.getYDirAdj();
                if (y <= headerRegionHeight) {
                    headerText.append(text.getUnicode());
                } else if (y >= (pageHeight - footerRegionHeight)) {
                    footerText.append(text.getUnicode());
                }
            }
        };
    }

    private static class ImageExtractor extends PDFStreamEngine {
        @Getter
        private final List<ImageElement> images = new ArrayList<>();

        @Setter
        private int currentPageNo;

        public ImageExtractor() {
            addOperator(new Concatenate(this));
            addOperator(new DrawObject(this));
            addOperator(new Save(this));
            addOperator(new Restore(this));
            addOperator(new SetMatrix(this));
        }

        @Override
        protected void processOperator(org.apache.pdfbox.contentstream.operator.Operator operator,
                List<org.apache.pdfbox.cos.COSBase> operands) throws IOException {
            String operation = operator.getName();
            if ("Do".equals(operation)) {
                COSBase cosBase = operands.getFirst();
                PDXObject xobject = getResources().getXObject((COSName) cosBase);
                if (xobject instanceof PDImageXObject image) {
                    Matrix ctm = getGraphicsState().getCurrentTransformationMatrix();
                    float x = ctm.getTranslateX();
                    float y = ctm.getTranslateY();
                    float width = image.getWidth() * ctm.getScaleX();
                    float height = image.getHeight() * ctm.getScaleY();
                    images.add(new ImageElement(
                            x,
                            y,
                            width,
                            height,
                            currentPageNo,
                            image.getStream().toByteArray()));
                }
            }
            super.processOperator(operator, operands);
        }
    }
}