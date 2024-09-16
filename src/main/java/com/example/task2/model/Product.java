package com.example.task2.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.List;



public class Product {

    private String id;
    private String product_name;
    private String category;
    private double price;
    private int stock_quantity;
    private double rating;
    private boolean available;
    private String brand;
    private List<String> tags;
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String release_date;

}
