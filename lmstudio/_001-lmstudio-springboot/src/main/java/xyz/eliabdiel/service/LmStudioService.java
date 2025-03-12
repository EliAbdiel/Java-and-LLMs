package xyz.eliabdiel.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import xyz.eliabdiel.request.ChatRequest;

public interface LmStudioService {

    ResponseEntity<String> getModels();
    ResponseEntity<String> sendChatRequest(ChatRequest chatRequest) throws JsonProcessingException;
}
