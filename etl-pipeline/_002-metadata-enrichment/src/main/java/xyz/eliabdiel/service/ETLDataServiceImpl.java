package xyz.eliabdiel.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.MetadataMode;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.KeywordMetadataEnricher;
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
public class ETLDataServiceImpl implements ETLDataService {

    @Value("classpath:input.txt")
    private Resource resource;

    private final ChatModel chatModel;

    // Extract: provides a source of documents from diverse origins
    @Override
    public List<Document> ReadTextDocuments() {
        TextReader textReader = new TextReader(resource);
        return textReader.get();
    }

    // Transform: transforms a batch of documents as part of the processing workflow
    @Override
    public List<Document> transformTextDocuments(List<Document> documents) {
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

    // Load: manages the final stage of the ETL process, preparing documents for storage
    @Override
    public void writeTextDocuments(List<Document> documents) {
        FileDocumentWriter writer = new FileDocumentWriter(
                "output.txt",
                false,
                MetadataMode.ALL,
                false
        );
        writer.accept(documents);
    }
}
