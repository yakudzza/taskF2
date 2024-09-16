package com.example.task2;


import com.example.task2.controller.ProductController;
import com.example.task2.model.Product;
import com.example.task2.service.OpenSearchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProductControllerTest {

    @InjectMocks
    private ProductController productController;

    @Mock
    private OpenSearchService openSearchService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    void testAddProduct() throws Exception {
        Product product = new Product();
        String id = "1";

        when(openSearchService.createProduct(eq(id), any(Product.class))).thenReturn(ResponseEntity.ok("Product added"));

        mockMvc.perform(post("/api/products/{id}", id)
                        .contentType("application/json")
                        .content("{\"product_name\":\"Sample Product\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Product added"));

        verify(openSearchService).createProduct(eq(id), any(Product.class));
    }

    @Test
    void testSearchProducts() throws Exception {
        String field = "category";
        String value = "Electronics";
        String response = "{\"hits\": {\"hits\": []}}";

        when(openSearchService.searchProducts(eq(field), eq(value))).thenReturn(response);

        mockMvc.perform(get("/api/products/search")
                        .param("field", field)
                        .param("value", value))
                .andExpect(status().isOk())
                .andExpect(content().json(response));

        verify(openSearchService).searchProducts(eq(field), eq(value));
    }

    @Test
    void testFilterProducts() throws Exception {
        String field = "category";
        String value = "Electronics";
        String response = "{\"hits\": {\"hits\": []}}";

        when(openSearchService.filterProducts(eq(field), eq(value))).thenReturn(response);

        mockMvc.perform(get("/api/products/filter")
                        .param("field", field)
                        .param("value", value))
                .andExpect(status().isOk())
                .andExpect(content().json(response));

        verify(openSearchService).filterProducts(eq(field), eq(value));
    }

    @Test
    void testGetProductById() throws Exception {
        String id = "1";
        String response = "{\"product_name\": \"Sample Product\"}";

        when(openSearchService.getProductById(eq(id))).thenReturn(response);

        mockMvc.perform(get("/api/products/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().json(response));

        verify(openSearchService).getProductById(eq(id));
    }

    @Test
    void testDeleteProductById() throws Exception {
        String id = "1";
        String response = "Product deleted";

        when(openSearchService.deleteProductById(eq(id))).thenReturn(ResponseEntity.ok(response));

        mockMvc.perform(delete("/api/products/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().string(response));

        verify(openSearchService).deleteProductById(eq(id));
    }


    @Test
    void testGetAllProducts() throws Exception {
        int from = 0;
        int size = 10;
        String response = "{\"hits\": {\"hits\": []}}";

        when(openSearchService.getAllProducts(eq(from), eq(size))).thenReturn(response);

        mockMvc.perform(get("/api/products")
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(content().json(response));

        verify(openSearchService).getAllProducts(eq(from), eq(size));
    }

    @Test
    void testGetUniqueFieldValues() throws Exception {
        String field = "tags";
        String response = "{\"aggregations\": {\"unique_values\": {\"buckets\": []}}}";

        when(openSearchService.getUniqueFieldValues(eq(field))).thenReturn(response);

        mockMvc.perform(get("/api/products/unique")
                        .param("field", field))
                .andExpect(status().isOk())
                .andExpect(content().json(response));

        verify(openSearchService).getUniqueFieldValues(eq(field));
    }

    @Test
    void testSearchByTags() throws Exception {
        String tag = "electronics";
        String response = "{\"hits\": {\"hits\": []}}";

        when(openSearchService.searchByTags(eq(tag))).thenReturn(response);

        mockMvc.perform(get("/api/products/searchByTags")
                        .param("tag", tag))
                .andExpect(status().isOk())
                .andExpect(content().json(response));

        verify(openSearchService).searchByTags(eq(tag));
    }
}
