package xyz.eliabdiel.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.MetadataMode;
import org.springframework.ai.reader.markdown.MarkdownDocumentReader;
import org.springframework.ai.reader.markdown.config.MarkdownDocumentReaderConfig;
import org.springframework.ai.transformer.KeywordMetadataEnricher;
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
public class MarkdownReaderServiceImpl implements MarkdownReaderService{

    Logger logger = LoggerFactory.getLogger(MarkdownReaderServiceImpl.class);

    @Value("classpath:code.md")
    private Resource resource;

    private final ChatModel chatModel;

    @Override
    public List<Document> loadMarkdown() {
        MarkdownDocumentReaderConfig config = MarkdownDocumentReaderConfig.builder()
                .withHorizontalRuleCreateDocument(true)
                .withIncludeCodeBlock(false)
                .withIncludeBlockquote(false)
                .withAdditionalMetadata("filename", "code.md")
                .build();

        MarkdownDocumentReader reader = new MarkdownDocumentReader(this.resource, config);
        return reader.get();
    }

    @Override
    public List<Document> transformMarkdownAsDocuments(List<Document> documents) {
        // split the document into tokens
        TextSplitter splitter = new TokenTextSplitter();
        return this.apply(splitter.split(documents));
    }

    @Override
    public void writeMarkdownAsDocuments(List<Document> documents) {
        FileDocumentWriter writer = new FileDocumentWriter(
                "md-output.txt",
                false,
                MetadataMode.ALL,
                false
        );
        writer.accept(documents);
    }

    private List<Document> apply(List<Document> documents) {
        logger.info("size of documents: {}", documents.size());
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
