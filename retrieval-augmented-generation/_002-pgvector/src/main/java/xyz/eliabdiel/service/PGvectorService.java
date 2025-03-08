package xyz.eliabdiel.service;

import org.springframework.ai.document.Document;

import java.io.IOException;
import java.util.List;

public interface PGvectorService {

    void loadDocuments() throws IOException;
    List<Document> search(String query);
    String getContent(String query);
}
