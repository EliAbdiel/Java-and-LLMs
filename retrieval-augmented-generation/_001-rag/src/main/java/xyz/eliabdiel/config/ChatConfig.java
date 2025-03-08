package xyz.eliabdiel.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.File;
import java.util.List;

@Configuration
public class ChatConfig {

    Logger logger = LoggerFactory.getLogger(ChatConfig.class);

    @Value("classpath:newProducts.txt")
    private Resource resource;

    private String path = "C:\\spring-ai\\retrieval-augmented-generation\\_001-rag\\src\\main\\resources\\newProducts.json";

    @Bean
    ChatClient chatClient(ChatClient.Builder chatClient, VectorStore vectorStore) {
        return chatClient
                .defaultAdvisors(
                        new QuestionAnswerAdvisor(vectorStore, SearchRequest.builder().build())
                )
                .build();
    }

    @Bean
    SimpleVectorStore simpleVectorStore(EmbeddingModel embeddingModel) {
        SimpleVectorStore simpleVectorStore = SimpleVectorStore.builder(embeddingModel).build();
        File file = new File(path);
        if (file.exists()) {
            logger.info("File exists {}", file.getPath());
            simpleVectorStore.load(file);
        } else {
            TextReader textReader = new TextReader(resource);
            textReader.getCustomMetadata().put("filename", "newProducts.txt");
            List<Document> documents = textReader.get();
            TextSplitter textSplitter = new TokenTextSplitter();
            List<Document> splitterDocuments = textSplitter.apply(documents);
            simpleVectorStore.add(splitterDocuments);
            simpleVectorStore.save(file);
        }
        return simpleVectorStore;
    }
}
