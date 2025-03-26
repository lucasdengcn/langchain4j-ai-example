package com.example.demo.pdf;

import lombok.Data;
import lombok.Getter;

@Getter
public class PdfElement {
    protected float x;
    protected float y;
    protected float width;
    protected float height;
    protected int pageNumber;
    protected final PdfElementType type;

    public PdfElement(float x, float y, float width, float height, int pageNumber, PdfElementType type) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.pageNumber = pageNumber;
        this.type = type;
    }
}
