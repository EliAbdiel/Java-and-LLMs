package xyz.eliabdiel.service;

import org.springframework.ai.document.Document;

import java.util.List;

public interface ETLDataService {

    List<Document> ReadTextDocuments();
    List<Document> transformTextDocuments(List<Document> documents);
    void writeTextDocuments(List<Document> documents);
}
