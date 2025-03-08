package xyz.eliabdiel.service;

import org.springframework.ai.document.Document;
import xyz.eliabdiel.model.Product;

import java.util.List;

public interface ChatService {

    List<Document> loadData();
    List<Document> search(String query);
    List<Product> getProducts();
}