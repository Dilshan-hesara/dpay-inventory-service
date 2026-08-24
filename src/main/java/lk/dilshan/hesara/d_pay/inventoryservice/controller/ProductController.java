package lk.dilshan.hesara.d_pay.inventoryservice.controller;

import jakarta.validation.Valid;
import lk.dilshan.hesara.d_pay.inventoryservice.dto.ProductRequestDTO;
import lk.dilshan.hesara.d_pay.inventoryservice.dto.ProductResponseDTO;
import lk.dilshan.hesara.d_pay.inventoryservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductResponseDTO> createProduct(
            @Valid @ModelAttribute ProductRequestDTO dto) {
        log.info("POST /api/v1/inventory - SKU: {}", dto.getSku());
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(dto));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @PathVariable String id,
            @Valid @ModelAttribute ProductRequestDTO dto) {
        log.info("PUT /api/v1/inventory/{}", id);
        return ResponseEntity.ok(productService.updateProduct(id, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable String id) {
        log.info("GET /api/v1/inventory/{}", id);
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping("/sku/{sku}")
    public ResponseEntity<ProductResponseDTO> getProductBySku(@PathVariable String sku) {
        log.info("GET /api/v1/inventory/sku/{}", sku);
        return ResponseEntity.ok(productService.getProductBySku(sku));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts(
            @RequestParam(required = false) String category) {
        log.info("GET /api/v1/inventory - category: {}", category);
        if (category != null && !category.isBlank()) {
            return ResponseEntity.ok(productService.getProductsByCategory(category));
        }
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
        log.info("DELETE /api/v1/inventory/{}", id);
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/stock")
    public ResponseEntity<ProductResponseDTO> adjustStock(
            @PathVariable String id,
            @RequestParam int delta) {
        log.info("PATCH /api/v1/inventory/{}/stock - delta: {}", id, delta);
        return ResponseEntity.ok(productService.adjustStock(id, delta));
    }
}
