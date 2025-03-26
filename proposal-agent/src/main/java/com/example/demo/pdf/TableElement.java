package com.example.demo.pdf;

import lombok.Data;
import lombok.Getter;

import java.util.List;

@Getter
public class TableElement extends PdfElement {
    private final List<List<String>> cells;

    public TableElement(List<List<String>> cells, float x, float y, float width, float height, int pageNumber) {
        super(x, y, width, height, pageNumber, PdfElementType.TABLE);
        this.cells = cells;
    }

}