package org.jeecg.entity;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Document(indexName = "prod")
public class Product {

    private int id;
    private String name;
    private String category;
    private float price;
    private String place;
    private String code;
}