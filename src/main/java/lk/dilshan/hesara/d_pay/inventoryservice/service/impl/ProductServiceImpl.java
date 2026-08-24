package lk.dilshan.hesara.d_pay.inventoryservice.service.impl;

import lk.dilshan.hesara.d_pay.inventoryservice.dto.ProductRequestDTO;
import lk.dilshan.hesara.d_pay.inventoryservice.dto.ProductResponseDTO;
import lk.dilshan.hesara.d_pay.inventoryservice.entity.Product;
import lk.dilshan.hesara.d_pay.inventoryservice.exception.DuplicateProductException;
import lk.dilshan.hesara.d_pay.inventoryservice.exception.ProductNotFoundException;
import lk.dilshan.hesara.d_pay.inventoryservice.mapper.ProductMapper;
import lk.dilshan.hesara.d_pay.inventoryservice.repository.ProductRepository;
import lk.dilshan.hesara.d_pay.inventoryservice.service.ProductService;
import lk.dilshan.hesara.d_pay.inventoryservice.storage.GcsStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final GcsStorageService gcsStorageService;

    @Override
    public ProductResponseDTO createProduct(ProductRequestDTO dto) {
        log.info("Creating product: {}", dto.getSku());
        if (productRepository.existsBySku(dto.getSku())) {
            throw new DuplicateProductException("Product SKU already exists: " + dto.getSku());
        }
        Product product = productMapper.toEntity(dto);
        if (dto.getImage() != null && !dto.getImage().isEmpty()) {
            String imageUrl = gcsStorageService.uploadProductImage(dto.getImage());
            product.setImageUrl(imageUrl);
        }
        return productMapper.toDto(productRepository.save(product));
    }

    @Override
    public ProductResponseDTO updateProduct(String id, ProductRequestDTO dto) {
        log.info("Updating product: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found: " + id));
        productMapper.updateEntityFromDto(dto, product);
        if (dto.getImage() != null && !dto.getImage().isEmpty()) {
            if (product.getImageUrl() != null) {
                gcsStorageService.deleteProductImage(product.getImageUrl());
            }
            product.setImageUrl(gcsStorageService.uploadProductImage(dto.getImage()));
        }
        return productMapper.toDto(productRepository.save(product));
    }

    @Override
    public ProductResponseDTO getProductById(String id) {
        return productMapper.toDto(productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found: " + id)));
    }

    @Override
    public ProductResponseDTO getProductBySku(String sku) {
        return productMapper.toDto(productRepository.findBySku(sku)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with SKU: " + sku)));
    }

    @Override
    public List<ProductResponseDTO> getAllProducts() {
        return productRepository.findAll().stream().map(productMapper::toDto).toList();
    }

    @Override
    public List<ProductResponseDTO> getProductsByCategory(String category) {
        return productRepository.findAll().stream()
                .filter(p -> category.equalsIgnoreCase(p.getCategory()))
                .map(productMapper::toDto)
                .toList();
    }

    @Override
    public void deleteProduct(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found: " + id));
        if (product.getImageUrl() != null) {
            gcsStorageService.deleteProductImage(product.getImageUrl());
        }
        productRepository.deleteById(id);
    }

    @Override
    public ProductResponseDTO adjustStock(String id, int delta) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found: " + id));
        int newQty = product.getStockQty() + delta;
        if (newQty < 0) throw new IllegalArgumentException("Insufficient stock for product: " + id);
        product.setStockQty(newQty);
        return productMapper.toDto(productRepository.save(product));
    }
}
