package xyz.eliabdiel.service;

import org.springframework.ai.document.Document;

import java.util.List;

public interface JsonReaderService {

    List<Document> loadJsonAsDocuments();
    List<Document> transformJsonAsDocuments(List<Document> documents);
    void writeJsonAsDocuments(List<Document> documents);
}
