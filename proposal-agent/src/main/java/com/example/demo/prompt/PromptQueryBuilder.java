package com.example.demo.prompt;

public class PromptQueryBuilder {

    // retrieve plain text of PDF
    public static String buildPlainTextPrompt(String pdfText) {
        StringBuilder prompt = new StringBuilder(6000);
        prompt.append("以下是PDF文档中某页的图片内容\n");
        prompt.append("你需从视觉上读出图片上的文本、文本的结构\n");
        prompt.append("不需要理解或猜测文本的含义或意义.\n");
        prompt.append("RAW_TEXT_START\n");
        prompt.append(pdfText);
        prompt.append("\nRAW_TEXT_END");
        return prompt.toString();
    }

}
