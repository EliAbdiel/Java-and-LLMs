package xyz.eliabdiel.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xyz.eliabdiel.service.ChatService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

//    @GetMapping("/load")
//    public ResponseEntity<String> loadData() {
//        try {
//            chatService.loadData();
//            return ResponseEntity.ok("Product documents data loaded.");
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("error: " + e.getMessage());
//        }
//    }

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam(defaultValue = "battery") String query) {
        try {
            List<Document> docs = chatService.search(query);
            List<String> docContents = docs.stream()
                    .map(Document::getText)
                    .toList();
            return ResponseEntity.ok(docContents);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("error: " + e.getMessage());
        }
    }

    @GetMapping("/print-products")
    public ResponseEntity<?> printproducts() {
        try {
            return ResponseEntity.ok(chatService.getProducts());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("error: " + e.getMessage());
        }
    }
}