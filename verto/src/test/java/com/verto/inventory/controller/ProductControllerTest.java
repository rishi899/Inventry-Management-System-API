package com.verto.inventory.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.verto.inventory.dto.StockUpdateRequest;
import com.verto.inventory.entity.Product;
import com.verto.inventory.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private Product testProduct;
    private StockUpdateRequest stockUpdateRequest;

    @BeforeEach
    void setUp() {
        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Test Product");
        testProduct.setDescription("Test Description");
        testProduct.setStockQuantity(10);
        testProduct.setLowStockThreshold(5);

        stockUpdateRequest = new StockUpdateRequest();
        stockUpdateRequest.setQuantity(5);
    }

    @Test
    void createProduct_ShouldReturnCreatedProduct() throws Exception {
        // Given
        when(productService.createProduct(any(Product.class))).thenReturn(testProduct);

        // When & Then
        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testProduct)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test Product"))
                .andExpect(jsonPath("$.stockQuantity").value(10));

        verify(productService).createProduct(any(Product.class));
    }

    @Test
    void getAllProducts_ShouldReturnAllProducts() throws Exception {
        // Given
        List<Product> products = Arrays.asList(testProduct);
        when(productService.getAllProducts()).thenReturn(products);

        // When & Then
        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("Test Product"));

        verify(productService).getAllProducts();
    }

    @Test
    void getProductById_WhenProductExists_ShouldReturnProduct() throws Exception {
        // Given
        when(productService.getProductById(1L)).thenReturn(Optional.of(testProduct));

        // When & Then
        mockMvc.perform(get("/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Product"));

        verify(productService).getProductById(1L);
    }

    @Test
    void getProductById_WhenProductDoesNotExist_ShouldReturnNotFound() throws Exception {
        // Given
        when(productService.getProductById(1L)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/products/1"))
                .andExpect(status().isNotFound());

        verify(productService).getProductById(1L);
    }

    @Test
    void updateProduct_WhenProductExists_ShouldReturnUpdatedProduct() throws Exception {
        // Given
        when(productService.updateProduct(anyLong(), any(Product.class))).thenReturn(testProduct);

        // When & Then
        mockMvc.perform(put("/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Product"));

        verify(productService).updateProduct(anyLong(), any(Product.class));
    }

    @Test
    void updateProduct_WhenProductDoesNotExist_ShouldReturnNotFound() throws Exception {
        // Given
        when(productService.updateProduct(anyLong(), any(Product.class)))
                .thenThrow(new RuntimeException("Product not found"));

        // When & Then
        mockMvc.perform(put("/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testProduct)))
                .andExpect(status().isNotFound());

        verify(productService).updateProduct(anyLong(), any(Product.class));
    }

    @Test
    void deleteProduct_WhenProductExists_ShouldReturnNoContent() throws Exception {
        // Given
        doNothing().when(productService).deleteProduct(1L);

        // When & Then
        mockMvc.perform(delete("/products/1"))
                .andExpect(status().isNoContent());

        verify(productService).deleteProduct(1L);
    }

    @Test
    void deleteProduct_WhenProductDoesNotExist_ShouldReturnNotFound() throws Exception {
        // Given
        doThrow(new RuntimeException("Product not found")).when(productService).deleteProduct(1L);

        // When & Then
        mockMvc.perform(delete("/products/1"))
                .andExpect(status().isNotFound());

        verify(productService).deleteProduct(1L);
    }

    @Test
    void increaseStock_WhenValid_ShouldReturnUpdatedProduct() throws Exception {
        // Given
        when(productService.increaseStock(anyLong(), any(StockUpdateRequest.class))).thenReturn(testProduct);

        // When & Then
        mockMvc.perform(post("/products/1/increase")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(stockUpdateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Product"));

        verify(productService).increaseStock(anyLong(), any(StockUpdateRequest.class));
    }

    @Test
    void increaseStock_WhenProductDoesNotExist_ShouldReturnNotFound() throws Exception {
        // Given
        when(productService.increaseStock(anyLong(), any(StockUpdateRequest.class)))
                .thenThrow(new RuntimeException("Product not found"));

        // When & Then
        mockMvc.perform(post("/products/1/increase")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(stockUpdateRequest)))
                .andExpect(status().isNotFound());

        verify(productService).increaseStock(anyLong(), any(StockUpdateRequest.class));
    }

    @Test
    void decreaseStock_WhenValid_ShouldReturnUpdatedProduct() throws Exception {
        // Given
        when(productService.decreaseStock(anyLong(), any(StockUpdateRequest.class))).thenReturn(testProduct);

        // When & Then
        mockMvc.perform(post("/products/1/decrease")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(stockUpdateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Product"));

        verify(productService).decreaseStock(anyLong(), any(StockUpdateRequest.class));
    }

    @Test
    void decreaseStock_WhenInsufficientStock_ShouldReturnBadRequest() throws Exception {
        // Given
        when(productService.decreaseStock(anyLong(), any(StockUpdateRequest.class)))
                .thenThrow(new IllegalArgumentException("Cannot decrease stock below zero"));

        // When & Then
        mockMvc.perform(post("/products/1/decrease")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(stockUpdateRequest)))
                .andExpect(status().isBadRequest());

        verify(productService).decreaseStock(anyLong(), any(StockUpdateRequest.class));
    }

    @Test
    void decreaseStock_WhenProductDoesNotExist_ShouldReturnNotFound() throws Exception {
        // Given
        when(productService.decreaseStock(anyLong(), any(StockUpdateRequest.class)))
                .thenThrow(new RuntimeException("Product not found"));

        // When & Then
        mockMvc.perform(post("/products/1/decrease")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(stockUpdateRequest)))
                .andExpect(status().isNotFound());

        verify(productService).decreaseStock(anyLong(), any(StockUpdateRequest.class));
    }

    @Test
    void getLowStockProducts_ShouldReturnLowStockProducts() throws Exception {
        // Given
        List<Product> lowStockProducts = Arrays.asList(testProduct);
        when(productService.getLowStockProducts()).thenReturn(lowStockProducts);

        // When & Then
        mockMvc.perform(get("/products/low-stock"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("Test Product"));

        verify(productService).getLowStockProducts();
    }
}
