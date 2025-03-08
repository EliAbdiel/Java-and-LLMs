package xyz.eliabdiel.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import xyz.eliabdiel.model.Product;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService{

    Logger logger = LoggerFactory.getLogger(ChatServiceImpl.class);

    private final VectorStore vectorStore;

    @Value("classpath:products.json")
    private Resource resource;

    @Override
    public List<Document> loadData() {
        List<Document> documents = readAndPrintJsonFile();
        TextSplitter textSplitter = new TokenTextSplitter();
        for(Document document: documents) {
            List<Document> splittedDocs = textSplitter.split(document);
            try {
                // Sleep for 1 second
                vectorStore.add(splittedDocs);
                logger.info("Added document: {}", document.getText());
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        }
        logger.info("Transformed documents: {}", documents);
        return documents;
    }

    @Override
    public List<Document> search(String query) {
        return vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(query)
                        .topK(3)
                        .build()
        );
    }

    private List<Document> readAndPrintJsonFile() {
        List<Document> documents = new ArrayList<>();
        List<Product> products = getProducts();
        for(Product product: products) {
            Document document = new Document(product.getBrand() + " " + product.getDescription());
            documents.add(document);
        }
        return documents;
    }

    @Override
    public List<Product> getProducts() {
        List<Product> products = new ArrayList<>();
        try {
            InputStream inputStream = resource.getInputStream();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(inputStream);
            for(JsonNode node: jsonNode) {
                var product = objectMapper.treeToValue(node, Product.class);
                products.add(product);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return products;
    }
}
