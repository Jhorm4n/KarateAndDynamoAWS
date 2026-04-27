package models;

import lombok.Data;

@Data
public class Item {
    private String productId;
    private String name;
    private String category;
    private int quantity;
    private double price;
}
