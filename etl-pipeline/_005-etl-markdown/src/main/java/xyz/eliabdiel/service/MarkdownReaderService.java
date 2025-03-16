package xyz.eliabdiel.service;

import org.springframework.ai.document.Document;

import java.util.List;

public interface MarkdownReaderService {

    List<Document> loadMarkdown();
    List<Document> transformMarkdownAsDocuments(List<Document> documents);
    void writeMarkdownAsDocuments(List<Document> documents);
}
