package com.example.demo.service;

import com.example.demo.pdf.PDFPageReport;
import com.example.demo.pdf.PdfElement;
import com.example.demo.pdf.PdfStructureService;
import com.example.demo.prompt.PromptQueryBuilder;
import com.openai.client.OpenAIClient;
import com.openai.core.JsonValue;
import com.openai.models.chat.completions.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Slf4j
@Service
public class PdfVLInstructService {

    private final PdfStructureService pdfStructureService;

    private final OpenAIClient openAIClient;

    public PdfVLInstructService(PdfStructureService pdfStructureService, OpenAIClient openAIClient) {
        this.pdfStructureService = pdfStructureService;
        this.openAIClient = openAIClient;
    }

    public void ocrImageFile(String imageFileName) throws IOException {
        //Step1: read image file into byte[] and encode byte[] as bas64
        File imageFile = new File(imageFileName);
        byte[] imageData = Files.readAllBytes(Paths.get(imageFileName));
        //
        ChatCompletionContentPartText chatCompletionContentPartText = ChatCompletionContentPartText.builder()
                .text("Read all the text in the image.")
                .build();
        ChatCompletionContentPartImage completionContentPartImage = ChatCompletionContentPartImage.builder()
                .imageUrl(ChatCompletionContentPartImage.ImageUrl.builder().url(String.format("data:image/png;base64,%s", Base64.getEncoder().encodeToString(imageData))).build())
                .putAdditionalProperty("min_pixels", JsonValue.from(28 * 28 * 4))
                .putAdditionalProperty("max_pixels", JsonValue.from(28 * 28 * 1280))
                .build();
        //
        List<ChatCompletionContentPart> parts = new ArrayList<>();
        parts.add(ChatCompletionContentPart.ofText(chatCompletionContentPartText));
        parts.add(ChatCompletionContentPart.ofImageUrl(completionContentPartImage));
        //
        ChatCompletionUserMessageParam userMessageParam = ChatCompletionUserMessageParam.builder()
                .role(JsonValue.from("user"))
                .contentOfArrayOfContentParts(parts)
                .build();
        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .addMessage(userMessageParam)
                .model("qwen-vl-ocr")
                .build();
        ChatCompletion chatCompletion = openAIClient.chat().completions().create(params);
        log.info("Chat Response: {}", chatCompletion);
    }

    public void processPdf(String pdfFilePath, int pageNumber) throws IOException {
        File pdfFile = new File(pdfFilePath);
        // Step 1: Use PdfStructureService to get PDFPageReport
        PDFPageReport pageReport = pdfStructureService.generatePageReport(pdfFile, pageNumber);

        // Step 2: build PDF anchor text with PDF text elements and image elements
        String anchorText = buildAnchorText(pageReport);

        // Step 3: Build query prompt with PromptQueryBuilder
        String promptText = PromptQueryBuilder.buildPlainTextPrompt(anchorText);
        // log.info("Prompt Text: {}\n {}", promptText.length(), promptText);

        // Step 4: call LLM
        String response = executeWithVLModel(promptText, pageReport.getPageBase64PNG());

        // Step 5: Save response to resource folder
        saveResponseToFile(response);
    }

    private String buildAnchorText(PDFPageReport pageReport) {
        // 实现构建 PDF 锚文本的逻辑
        List<PdfElement> elements = new ArrayList<>();
        elements.addAll(pageReport.getTextElements());
        elements.addAll(pageReport.getImageElements());
        //
        elements.sort((e1, e2) -> {
            if (e1.getY() == e2.getY()) {
                return Float.compare(e1.getX(), e2.getX());
            } else {
                return Float.compare(e2.getY(), e1.getY());
            }
        });
        int maxTokens = 60000;
        StringBuilder sb = new StringBuilder(maxTokens);
        int length = 0;
        for (PdfElement element : elements) {
            String s = element.toString();
            length += s.length() + 2;
            if (length > maxTokens){
                break;
            }
            sb.append(s).append("\n");
        }
        //
        log.info("Anchor Text: {}", sb.length());
        return sb.toString();
    }

    private String executeWithVLModel(String promptText, byte[] imageData) {
        ChatCompletionContentPartText chatCompletionContentPartText = ChatCompletionContentPartText.builder()
                .text(promptText)
                .build();
        ChatCompletionContentPartImage completionContentPartImage = ChatCompletionContentPartImage.builder()
                .imageUrl(ChatCompletionContentPartImage.ImageUrl.builder().url(String.format("data:image/png;base64,%s", Base64.getEncoder().encodeToString(imageData))).build())
                .putAdditionalProperty("min_pixels", JsonValue.from(28 * 28 * 40))
                .putAdditionalProperty("max_pixels", JsonValue.from(12845056))
                .build();
        //
        List<ChatCompletionContentPart> parts = new ArrayList<>();
        parts.add(ChatCompletionContentPart.ofText(chatCompletionContentPartText));
        parts.add(ChatCompletionContentPart.ofImageUrl(completionContentPartImage));
        //
        ChatCompletionUserMessageParam userMessageParam = ChatCompletionUserMessageParam.builder()
                .role(JsonValue.from("user"))
                .contentOfArrayOfContentParts(parts)
                .build();
        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .addMessage(userMessageParam)
                .model("qwen-vl-plus-latest")
                .build();
        ChatCompletion chatCompletion = openAIClient.chat().completions().create(params);
        log.info("Chat Response: {}", chatCompletion);
        return chatCompletion.choices().getFirst().message().content().get();
    }

    private void saveResponseToFile(String response) throws IOException {
        // 实现保存响应到文件的逻辑
        String filePath = Paths.get("src/main/resources/output.txt").toString();
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(response.getBytes());
        }
    }
}