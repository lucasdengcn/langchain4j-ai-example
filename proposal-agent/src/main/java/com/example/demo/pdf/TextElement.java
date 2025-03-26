package com.example.demo.pdf;

import lombok.Getter;

@Getter
public class TextElement extends PdfElement {
    private final String text;

    public TextElement(String text, float x, float y, float width, float height, int pageNumber) {
        super(x, y, width, height, pageNumber, PdfElementType.TEXT);
        this.text = text;
    }

    @Override
    public String toString() {
        return String.format("[%.0fx%.0f]%s", this.x, this.y, this.text);
    }
}