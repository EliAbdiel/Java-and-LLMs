package xyz.eliabdiel.service;

import org.springframework.http.ResponseEntity;

public interface MongoVectorStoreService {

    ResponseEntity<?> load();
    ResponseEntity<?> search(String query);
}
