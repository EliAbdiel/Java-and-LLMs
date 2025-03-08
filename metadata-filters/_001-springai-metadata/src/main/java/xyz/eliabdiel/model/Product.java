package xyz.eliabdiel.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Product {

    private int id;

    @JsonProperty("product_name")
    private String productName;
    private String description;
    private String brand;
}
