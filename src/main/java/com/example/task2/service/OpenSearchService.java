package com.example.task2.service;

import com.example.task2.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class OpenSearchService {

    @Autowired
    private RestTemplate restTemplate;

    private static final String OPEN_SEARCH_URL = "http://localhost:9200/products/_doc/";

    private final ObjectMapper objectMapper = new ObjectMapper();

    public ResponseEntity<String> createProduct(String id, Product product) {
        String url = OPEN_SEARCH_URL + id;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<Product> request = new HttpEntity<>(product, headers);
        return restTemplate.exchange(url, HttpMethod.PUT, request, String.class);
    }

    public String searchProducts(String field, String value) {
        String url = "http://localhost:9200/products/_search";
        String query = "{ \"query\": { \"match\": { \"" + field + "\": \"" + value + "\" } } }";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<String> request = new HttpEntity<>(query, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

        try {
            JsonNode root = objectMapper.readTree(response.getBody());
            return root.path("hits").path("hits").toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error parsing response";
        }
    }

    public String filterProducts(String field, String value) {
        String url = "http://localhost:9200/products/_search";
        String query = "{ \"query\": { \"term\": { \"" + field + ".keyword\": \"" + value + "\" } } }";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<String> request = new HttpEntity<>(query, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

        try {
            JsonNode root = objectMapper.readTree(response.getBody());
            return root.path("hits").path("hits").toString(); // Возвращаем только результаты поиска
        } catch (Exception e) {
            e.printStackTrace();
            return "Error parsing response";
        }
    }

    public ResponseEntity<String> deleteProductById(String id) {
        String url = OPEN_SEARCH_URL + id;
        return restTemplate.exchange(url, HttpMethod.DELETE, null, String.class);
    }




    public String getProductById(String id) {
        String url = OPEN_SEARCH_URL + id;
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        try {
            JsonNode root = objectMapper.readTree(response.getBody());
            return root.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error parsing response";
        }
    }

    public String getAllProducts(int from, int size) {
        String url = "http://localhost:9200/products/_search";
        String query = "{ \"from\": " + from + ", \"size\": " + size + ", \"query\": { \"match_all\": {} } }";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<String> request = new HttpEntity<>(query, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

        try {
            JsonNode root = objectMapper.readTree(response.getBody());
            return root.path("hits").path("hits").toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error parsing response";
        }
    }

    public String getUniqueFieldValues(String field) {
        String url = "http://localhost:9200/products/_search";
        String query = "{ \"aggs\": { \"unique_values\": { \"terms\": { \"field\": \"" + field + ".keyword\" } } }, \"size\": 0 }";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<String> request = new HttpEntity<>(query, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

        try {
            JsonNode root = objectMapper.readTree(response.getBody());
            return root.path("aggregations").path("unique_values").path("buckets").toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error parsing response";
        }
    }

    public String searchByTags(String tag) {
        String url = "http://localhost:9200/products/_search";
        String query = "{ \"query\": { \"match\": { \"tags\": \"" + tag + "\" } } }";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<String> request = new HttpEntity<>(query, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

        try {
            JsonNode root = objectMapper.readTree(response.getBody());
            return root.path("hits").path("hits").toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error parsing response";
        }
    }


}
