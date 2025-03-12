package xyz.eliabdiel.service;

import org.springframework.ai.document.Document;

import java.util.List;

public interface PdfReaderService {

    List<Document> getDocsFromPdf();
    List<Document> transformPdfDocuments(List<Document> documents);
    void writePdfDocuments(List<Document> documents);
}
