package xyz.eliabdiel.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatClient chatClient;

    @Override
    public ResponseEntity<String> getResponse(String message) {
        var result = chatClient.prompt()
                .user(message)
                .call()
                .chatResponse()
                .getResult().getOutput().getText();

        return ResponseEntity.ok(result);
    }
}
