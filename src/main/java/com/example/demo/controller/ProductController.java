package com.example.demo.controller;

import com.example.demo.entity.Product;
import com.example.demo.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping
    public List<Product> getAllProducts(@RequestParam(required = false) String categoryName) {
        return categoryName != null ? productService.getProductsByCategoryName(categoryName) : productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Optional<Product> product = productService.getProductById(id);
        return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/images")
    public ResponseEntity<String[]> getProductImages(@PathVariable Long id) {
        String[] images = productService.getProductImages(id);
        return images.length > 0 ? ResponseEntity.ok(images) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        productService.saveProduct(product);
        return ResponseEntity.ok(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product productDetails) {
        Optional<Product> product = productService.getProductById(id);
        if (product.isPresent()) {
            Product updatedProduct = product.get();
            updatedProduct.setCategoryId(productDetails.getCategoryId());
            updatedProduct.setName(productDetails.getName());
            updatedProduct.setDescription(productDetails.getDescription());
            updatedProduct.setPrice(productDetails.getPrice());
            updatedProduct.setImages(productDetails.getImages());
            updatedProduct.setType(productDetails.getType());
            productService.updateProduct(updatedProduct);
            return ResponseEntity.ok(updatedProduct);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    // 上传产品图片
    @PostMapping("/{id}/upload")
    public ResponseEntity<String[]> uploadProductImages(@PathVariable Long id, @RequestParam("files") MultipartFile[] files) {
        try {
            Optional<Product> productOptional = productService.getProductById(id);
            if (productOptional.isPresent()) {
                String[] images = productService.uploadProductImages(files);
                Product product = productOptional.get();
                String[] existingImages = objectMapper.readValue(product.getImages(), String[].class);
                String[] updatedImages = new String[existingImages.length + images.length];
                System.arraycopy(existingImages, 0, updatedImages, 0, existingImages.length);
                System.arraycopy(images, 0, updatedImages, existingImages.length, images.length);
                product.setImages(objectMapper.writeValueAsString(updatedImages));
                productService.updateProduct(product);
                return ResponseEntity.ok(images);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(new String[]{"上传失败"});
        }
    }

    // 更新产品字段和删除图片
    @PostMapping("/{id}/updateFieldsAndImages")
    public ResponseEntity<Product> updateProductFieldsAndImages(
            @PathVariable Long id,
            @RequestParam(required = false) String updates,
            @RequestParam(value = "uploadFiles", required = false) MultipartFile[] uploadFiles,
            @RequestParam(value = "deleteImages", required = false) String[] deleteImages) {
        try {
            Map<String, Object> updatesMap = updates != null ? objectMapper.readValue(updates, Map.class) : null;
            Product updatedProduct = productService.processProductUpdate(id, updatesMap, uploadFiles, deleteImages);
            return ResponseEntity.ok(updatedProduct);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
}
