package com.example.demo.pdf;

import lombok.Getter;

@Getter
public class ImageElement extends PdfElement {
    //
    private final byte[] imageData;

    public ImageElement(float x, float y, float width, float height, int pageNumber, byte[] imageData) {
        super(x, y, width, height, pageNumber, PdfElementType.IMAGE);
        this.imageData = imageData;
    }

    @Override
    public String toString() {
        return String.format("[Image %.0fx%.0f to %.0fx%.0f]", x, y, x + width, y + height);
    }
}