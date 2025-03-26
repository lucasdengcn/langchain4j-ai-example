package com.example.demo.pdf;

import lombok.Data;

import java.util.List;

@Data
public class PDFPageReport {
    private int pageNumber;
    private byte[] pageBase64PNG;
    //
    private float x0;
    private float y0;
    private float x1;
    private float y1;
    private float width;
    private float height;
    //
    private List<TextElement> textElements;
    private List<ImageElement> imageElements;
    private List<TableElement> tableElements;
    private String header;
    private String footer;

}