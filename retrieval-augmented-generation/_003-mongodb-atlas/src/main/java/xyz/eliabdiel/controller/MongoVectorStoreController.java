package xyz.eliabdiel.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xyz.eliabdiel.service.MongoVectorStoreService;

@RestController
@RequiredArgsConstructor
public class MongoVectorStoreController {

    private final MongoVectorStoreService mongoVectorStoreService;

    @GetMapping("/load")
    public ResponseEntity<?> load() {
        return mongoVectorStoreService.load();
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam(value = "query", defaultValue = "learn how to grow things") String query) {
        return mongoVectorStoreService.search(query);
    }
}