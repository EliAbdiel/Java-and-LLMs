package xyz.eliabdiel.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.eliabdiel.service.JsonReaderService;

@RestController
@RequiredArgsConstructor
public class JsonReaderController {

    private final JsonReaderService jsonReaderService;

    @GetMapping("/etl-data-process")
    public ResponseEntity<?> etl() {
        try {
            var docs = jsonReaderService.loadJsonAsDocuments();
            var transform = jsonReaderService.transformJsonAsDocuments(docs);
            jsonReaderService.writeJsonAsDocuments(transform);
            return ResponseEntity.ok("ETL JSON completed successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
