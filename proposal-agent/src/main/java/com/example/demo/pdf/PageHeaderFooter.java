package com.example.demo.pdf;

import lombok.Getter;

@Getter
public class PageHeaderFooter {
    private final String header;
    private final String footer;
    private final int pageNumber;

    public PageHeaderFooter(String header, String footer, int pageNumber) {
        this.header = header;
        this.footer = footer;
        this.pageNumber = pageNumber;
    }

}