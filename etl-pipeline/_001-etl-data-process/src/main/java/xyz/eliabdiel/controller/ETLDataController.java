package xyz.eliabdiel.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.eliabdiel.service.ETLDataService;

@RestController
@RequiredArgsConstructor
public class ETLDataController {

    private final ETLDataService etlDataService;

    @GetMapping("/etl-data-process")
    public ResponseEntity<?> etl() {
        try {
            var docs = etlDataService.ReadTextDocuments();
            var transform = etlDataService.transformTextDocuments(docs);
            etlDataService.writeTextDocuments(transform);
            return ResponseEntity.ok("ETL Pipeline successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
