package xyz.eliabdiel.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.MetadataMode;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.SummaryMetadataEnricher;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.writer.FileDocumentWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PdfReaderServiceImpl implements PdfReaderService {

    private final ChatModel chatModel;

    @Value("classpath:data/Network_Security_v1_0_VM_Lab.pdf")
    private Resource resource;

    @Override
    public List<Document> getDocsFromPdf() {
        PagePdfDocumentReader pdfReader = new PagePdfDocumentReader(
                resource,
                PdfDocumentReaderConfig.builder()
                        .withPageBottomMargin(0)
                        .withPageExtractedTextFormatter(ExtractedTextFormatter.builder()
                                .withNumberOfTopTextLinesToDelete(0)
                                .build())
                        .withPagesPerDocument(1)
                        .build()
        );
        return pdfReader.read();
    }

    @Override
    public List<Document> transformPdfDocuments(List<Document> documents) {
        // split the document into tokens
        TextSplitter splitter = new TokenTextSplitter();

//        The KeywordMetadataEnricher is a DocumentTransformer
//        that uses a generative AI model to extract keywords from document

//        KeywordMetadataEnricher keywordMetadataEnricher =
//                new KeywordMetadataEnricher(chatModel, 5);
//        return keywordMetadataEnricher.apply(splitter.split(documents));

//        The SummaryMetadataEnricher is a DocumentTransformer that uses a generative AI model
//        to create summaries for documents and add them as metadata
        SummaryMetadataEnricher summaryMetadataEnricher = new SummaryMetadataEnricher(
                chatModel,
                List.of(
                        SummaryMetadataEnricher.SummaryType.CURRENT,
                        SummaryMetadataEnricher.SummaryType.NEXT,
                        SummaryMetadataEnricher.SummaryType.PREVIOUS
                )
        );
        return summaryMetadataEnricher.apply(splitter.split(documents));
    }

    @Override
    public void writePdfDocuments(List<Document> documents) {
        FileDocumentWriter writer = new FileDocumentWriter(
                "pdf-output.txt",
                false,
                MetadataMode.ALL,
                false
        );
        writer.accept(documents);
    }
}
