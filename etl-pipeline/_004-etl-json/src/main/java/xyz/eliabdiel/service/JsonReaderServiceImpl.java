package xyz.eliabdiel.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.MetadataMode;
import org.springframework.ai.reader.JsonReader;
import org.springframework.ai.transformer.KeywordMetadataEnricher;
import org.springframework.ai.transformer.SummaryMetadataEnricher;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.writer.FileDocumentWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class JsonReaderServiceImpl implements JsonReaderService {

    Logger logger = LoggerFactory.getLogger(JsonReaderServiceImpl.class);

    @Value("classpath:products.json")
    private Resource resource;

    private final ChatModel chatModel;

    @Override
    public List<Document> loadJsonAsDocuments() {
        JsonReader jsonReader = new JsonReader(resource, "brand", "description");
        return jsonReader.get();
    }

    @Override
    public List<Document> transformJsonAsDocuments(List<Document> documents) {
        // split the document into tokens
        TextSplitter splitter = new TokenTextSplitter();

//        The KeywordMetadataEnricher is a DocumentTransformer
//        that uses a generative AI model to extract keywords from document
//        KeywordMetadataEnricher keywordMetadataEnricher = new KeywordMetadataEnricher(chatModel, 5);

        return this.apply(splitter.split(documents));

//        The SummaryMetadataEnricher is a DocumentTransformer that uses a generative AI model
//        to create summaries for documents and add them as metadata
//        SummaryMetadataEnricher summaryMetadataEnricher = new SummaryMetadataEnricher(
//                chatModel,
//                List.of(
//                        SummaryMetadataEnricher.SummaryType.CURRENT,
//                        SummaryMetadataEnricher.SummaryType.NEXT,
//                        SummaryMetadataEnricher.SummaryType.PREVIOUS
//                )
//        );
//        return summaryMetadataEnricher.apply(splitter.split(documents));
    }

    @Override
    public void writeJsonAsDocuments(List<Document> documents) {
        FileDocumentWriter writer = new FileDocumentWriter(
                "json-output.txt",
                false,
                MetadataMode.ALL,
                false
        );
        writer.accept(documents);
    }

    private List<Document> apply(List<Document> documents) {
        for(Document document : documents) {
            PromptTemplate template = new PromptTemplate(String.format(KeywordMetadataEnricher.KEYWORDS_TEMPLATE, 5));
            Prompt prompt = template.create(Map.of(KeywordMetadataEnricher.CONTEXT_STR_PLACEHOLDER, document.getText()));
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            String keywords = this.chatModel.call(prompt).getResult().getOutput().getText();
            logger.info("Extracted keywords: {}", keywords);
            document.getMetadata().putAll(Map.of("excerpt_keywords", keywords));
        }
        return documents;
    }
}
