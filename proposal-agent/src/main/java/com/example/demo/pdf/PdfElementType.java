package com.example.demo.pdf;

public enum PdfElementType {
    TEXT("text"),
    IMAGE("image"),
    TABLE("table"),
    HEADER("header"),
    FOOTER("footer");

    private final String type;

    PdfElementType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
