package xyz.eliabdiel.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xyz.eliabdiel.service.ChatService;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @GetMapping("/generate-response")
    public ResponseEntity<String> generateResponse(@RequestParam String message) {
        return chatService.getResponse(message);
    }
}
