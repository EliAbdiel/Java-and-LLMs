package xyz.eliabdiel.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import xyz.eliabdiel.request.ChatRequest;
import xyz.eliabdiel.service.LmStudioService;

@RestController
@RequiredArgsConstructor
public class LmStudioController {

    private final LmStudioService lmStudioService;

    // GET /v1/models
    @GetMapping("/models")
    public ResponseEntity<String> getModels() {
        return lmStudioService.getModels();
    }

    // POST /v1/chat/completions
    @PostMapping("/chat")
    public ResponseEntity<String> sendChatRequest(@RequestBody ChatRequest chatRequest) throws JsonProcessingException {
        return lmStudioService.sendChatRequest(chatRequest);
    }
}
