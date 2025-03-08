package xyz.eliabdiel.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    private long price;
    private int year;
    private String country;
    @JsonProperty("isAvailable")
    private boolean isAvailable;
}
