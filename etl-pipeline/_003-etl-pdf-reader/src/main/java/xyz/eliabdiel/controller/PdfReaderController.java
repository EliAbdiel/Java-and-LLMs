package xyz.eliabdiel.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.eliabdiel.service.PdfReaderService;

@RestController
@RequiredArgsConstructor
public class PdfReaderController {

    private final PdfReaderService pdfReaderService;

    @GetMapping("/etl-data-process")
    public ResponseEntity<?> etl() {
        try {
            var docs = pdfReaderService.getDocsFromPdf();
            var transform = pdfReaderService.transformPdfDocuments(docs);
            pdfReaderService.writePdfDocuments(transform);
            return ResponseEntity.ok("ETL PDF completed successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
