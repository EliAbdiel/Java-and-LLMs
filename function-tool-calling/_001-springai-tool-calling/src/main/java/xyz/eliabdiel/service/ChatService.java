package xyz.eliabdiel.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    Logger logger = LoggerFactory.getLogger(ChatService.class);

//    private final ChatModel chatModel;
    private final OllamaChatModel chatModel;

    public ResponseEntity<String> chat() {
        try {
            UserMessage userMessage = new UserMessage("What is the status of bookings for H001, H002, and H003");
            ChatResponse chatResponse = chatModel.call(new Prompt(List.of(userMessage), OllamaOptions.builder().function("bookingStatus").build()));
            logger.info("chatResponse: {}", chatResponse);
            return ResponseEntity.ok(chatResponse.getResult().getOutput().getText());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("error: " + e.getMessage());
        }
    }

}
