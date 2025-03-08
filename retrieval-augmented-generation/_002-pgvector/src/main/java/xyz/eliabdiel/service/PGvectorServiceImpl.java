package xyz.eliabdiel.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PGvectorServiceImpl implements PGvectorService {

    Logger logger = LoggerFactory.getLogger(PGvectorServiceImpl.class);

    @Value("classpath:newProducts.txt")
    private Resource resource;

    private final VectorStore vectorStore;

    @Override
    public void loadDocuments() throws IOException {
        List<Document> documents = Files.lines(resource.getFile().toPath())
                .map(Document::new)
                .toList();
        TextSplitter splitter = new TokenTextSplitter();
        documents.forEach(doc -> {
            List<Document> splittedDocs = splitter.split(doc);
            try {
                TimeUnit.SECONDS.sleep(20);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            vectorStore.add(splittedDocs);
            logger.info("Added document: {}", doc);
        });
    }

    @Override
    public List<Document> search(String query) {
        return vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(query)
                        .topK(3)
                        .build()
        );
    }

    @Override
    public String getContent(String query) {
        List<Document> result = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(query)
                        .topK(10)
                        .build());
        return result.stream()
                .map(Document::getText)
                .limit(10)
                .collect(
                        Collectors.joining(", ")
                );
    }
}