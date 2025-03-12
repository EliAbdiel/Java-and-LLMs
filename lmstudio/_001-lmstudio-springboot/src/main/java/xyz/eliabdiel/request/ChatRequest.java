package xyz.eliabdiel.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import xyz.eliabdiel.model.Message;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatRequest {

    @Value("${lmstudio.ai.model}")
    private String model;

    private List<Message> messages;
    private double temperature;
    private int maxTokens;
    private boolean stream;
}
