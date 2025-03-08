package xyz.eliabdiel.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xyz.eliabdiel.service.PGvectorService;

@RestController
@RequiredArgsConstructor
public class PGvectorController {

    private final PGvectorService pGvectorService;

    @GetMapping("/load")
    public ResponseEntity<String> load() {
        try {
            pGvectorService.loadDocuments();
            return ResponseEntity.ok("Product documents data loaded.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("error: " + e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam String query) {
        try {
            var result = pGvectorService.search(query);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("error: " + e.getMessage());
        }
    }

    @GetMapping("/generate-content")
    public ResponseEntity<String> generateContent(@RequestParam String query) {
        try {
            var result = pGvectorService.getContent(query);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("error: " + e.getMessage());
        }
    }
}