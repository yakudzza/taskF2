package com.example.task2.controller;

import com.example.task2.model.Product;
import com.example.task2.service.OpenSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private OpenSearchService openSearchService;

    @PostMapping("/{id}")
    public ResponseEntity<String> addProduct(@PathVariable String id, @RequestBody Product product) {
        return openSearchService.createProduct(id, product);
    }

    @GetMapping("/search")
    public ResponseEntity<String> searchProducts(@RequestParam String field, @RequestParam String value) {
        String result = openSearchService.searchProducts(field, value);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/filter")
    public ResponseEntity<String> filterProducts(@RequestParam String field, @RequestParam String value) {
        String result = openSearchService.filterProducts(field, value);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getProductById(@PathVariable String id) {
        String result = openSearchService.getProductById(id);
        if (result == null || result.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProductById(@PathVariable String id) {
        return openSearchService.deleteProductById(id);
    }


    @GetMapping
    public ResponseEntity<String> getAllProducts(
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(openSearchService.getAllProducts(from, size));
    }


    @GetMapping("/unique")
    public String getUniqueFieldValues(@RequestParam String field) {
        return openSearchService.getUniqueFieldValues(field);
    }

    @GetMapping("/searchByTags")
    public String searchByTags(@RequestParam String tag) {
        return openSearchService.searchByTags(tag);
    }

}
