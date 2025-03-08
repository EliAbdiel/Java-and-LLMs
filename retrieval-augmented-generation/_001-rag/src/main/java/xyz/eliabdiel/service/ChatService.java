package xyz.eliabdiel.service;

import org.springframework.http.ResponseEntity;

public interface ChatService {

    ResponseEntity<String> getResponse(String message);
}
