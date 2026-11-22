package org.example.Models;

public class Product {

    private final String name;
    private final double price;
    private final String description;
    private final int stock;
    private final String category;

    public Product(String name, double price, String description, int stock, String category) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.stock = stock;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public int getStock() {
        return stock;
    }

    public String getCategory() {
        return category;
    }
}
