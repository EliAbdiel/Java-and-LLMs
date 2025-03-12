package xyz.eliabdiel.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import xyz.eliabdiel.request.ChatRequest;
import xyz.eliabdiel.response.ChatResponse;

@Service
@RequiredArgsConstructor
public class LmStudioServiceImpl implements LmStudioService{

    Logger logger = LoggerFactory.getLogger(LmStudioServiceImpl.class);

    private final RestTemplate restTemplate;

    @Value("${external.ai.api.models}")
    private String modelUrl;

    @Value("${external.ai.api.completions}")
    private String chatCompletionUrl;

    @Override
    public ResponseEntity<String> getModels() {
        var response = restTemplate.getForEntity(modelUrl, String.class);
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }

    @Override
    public ResponseEntity<String> sendChatRequest(ChatRequest chatRequest) throws JsonProcessingException {

        // set up http headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        // create the request entity
        HttpEntity<ChatRequest> requestEntity = new HttpEntity<>(chatRequest, headers);

        // send the post request to the external api and get the request
        var response = restTemplate.exchange(
                chatCompletionUrl, // external api url
                HttpMethod.POST, // http method
                requestEntity, // request with headers and body
                String.class // response type
        );

        logger.info(response.getBody());

        ObjectMapper objectMapper = new ObjectMapper();
        ChatResponse chatResponse = objectMapper
                .readValue(
                        response.getBody(),
                        ChatResponse.class
                );

        var responseString = objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(
                        chatResponse.getChoices().getFirst().getMessage()
                );

        // return the response to the client
        return ResponseEntity.status(response.getStatusCode()).body(responseString);
    }
}