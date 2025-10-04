package com.verto.inventory.service;

import com.verto.inventory.dto.StockUpdateRequest;
import com.verto.inventory.entity.Product;
import com.verto.inventory.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

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
    void createProduct_ShouldReturnSavedProduct() {
        // Given
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        // When
        Product result = productService.createProduct(testProduct);

        // Then
        assertNotNull(result);
        assertEquals(testProduct.getName(), result.getName());
        verify(productRepository).save(testProduct);
    }

    @Test
    void getAllProducts_ShouldReturnAllProducts() {
        // Given
        List<Product> products = Arrays.asList(testProduct);
        when(productRepository.findAll()).thenReturn(products);

        // When
        List<Product> result = productService.getAllProducts();

        // Then
        assertEquals(1, result.size());
        assertEquals(testProduct.getName(), result.get(0).getName());
        verify(productRepository).findAll();
    }

    @Test
    void getProductById_WhenProductExists_ShouldReturnProduct() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        // When
        Optional<Product> result = productService.getProductById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals(testProduct.getName(), result.get().getName());
        verify(productRepository).findById(1L);
    }

    @Test
    void getProductById_WhenProductDoesNotExist_ShouldReturnEmpty() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        Optional<Product> result = productService.getProductById(1L);

        // Then
        assertFalse(result.isPresent());
        verify(productRepository).findById(1L);
    }

    @Test
    void updateProduct_WhenProductExists_ShouldReturnUpdatedProduct() {
        // Given
        Product updatedProduct = new Product();
        updatedProduct.setName("Updated Product");
        updatedProduct.setDescription("Updated Description");
        updatedProduct.setLowStockThreshold(3);

        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        // When
        Product result = productService.updateProduct(1L, updatedProduct);

        // Then
        assertNotNull(result);
        verify(productRepository).findById(1L);
        verify(productRepository).save(testProduct);
    }

    @Test
    void updateProduct_WhenProductDoesNotExist_ShouldThrowException() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> 
            productService.updateProduct(1L, testProduct));
        verify(productRepository).findById(1L);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void deleteProduct_WhenProductExists_ShouldDeleteProduct() {
        // Given
        when(productRepository.existsById(1L)).thenReturn(true);

        // When
        productService.deleteProduct(1L);

        // Then
        verify(productRepository).existsById(1L);
        verify(productRepository).deleteById(1L);
    }

    @Test
    void deleteProduct_WhenProductDoesNotExist_ShouldThrowException() {
        // Given
        when(productRepository.existsById(1L)).thenReturn(false);

        // When & Then
        assertThrows(RuntimeException.class, () -> 
            productService.deleteProduct(1L));
        verify(productRepository).existsById(1L);
        verify(productRepository, never()).deleteById(anyLong());
    }

    @Test
    void increaseStock_WhenProductExists_ShouldIncreaseStock() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        // When
        Product result = productService.increaseStock(1L, stockUpdateRequest);

        // Then
        assertNotNull(result);
        verify(productRepository).findById(1L);
        verify(productRepository).save(testProduct);
    }

    @Test
    void increaseStock_WhenProductDoesNotExist_ShouldThrowException() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> 
            productService.increaseStock(1L, stockUpdateRequest));
        verify(productRepository).findById(1L);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void decreaseStock_WhenValidDecrease_ShouldDecreaseStock() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        // When
        Product result = productService.decreaseStock(1L, stockUpdateRequest);

        // Then
        assertNotNull(result);
        verify(productRepository).findById(1L);
        verify(productRepository).save(testProduct);
    }

    @Test
    void decreaseStock_WhenInsufficientStock_ShouldThrowException() {
        // Given
        testProduct.setStockQuantity(3); // Less than requested decrease
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> 
            productService.decreaseStock(1L, stockUpdateRequest));
        verify(productRepository).findById(1L);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void decreaseStock_WhenProductDoesNotExist_ShouldThrowException() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> 
            productService.decreaseStock(1L, stockUpdateRequest));
        verify(productRepository).findById(1L);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void getLowStockProducts_ShouldReturnLowStockProducts() {
        // Given
        List<Product> lowStockProducts = Arrays.asList(testProduct);
        when(productRepository.findLowStockProducts()).thenReturn(lowStockProducts);

        // When
        List<Product> result = productService.getLowStockProducts();

        // Then
        assertEquals(1, result.size());
        assertEquals(testProduct.getName(), result.get(0).getName());
        verify(productRepository).findLowStockProducts();
    }
}
