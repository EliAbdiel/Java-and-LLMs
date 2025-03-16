package xyz.eliabdiel.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.eliabdiel.service.MarkdownReaderService;

@RestController
@RequiredArgsConstructor
public class MarkdownReaderController {

    private final MarkdownReaderService markdownReaderService;

    @GetMapping("/etl-data-process")
    public ResponseEntity<?> etl() {
        try {
            var docs = markdownReaderService.loadMarkdown();
            var transform = markdownReaderService.transformMarkdownAsDocuments(docs);
            markdownReaderService.writeMarkdownAsDocuments(transform);
            return ResponseEntity.ok("ETL Markdown completed successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
