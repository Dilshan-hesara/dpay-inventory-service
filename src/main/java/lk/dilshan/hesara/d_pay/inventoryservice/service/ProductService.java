package lk.dilshan.hesara.d_pay.inventoryservice.service;

import lk.dilshan.hesara.d_pay.inventoryservice.dto.ProductRequestDTO;
import lk.dilshan.hesara.d_pay.inventoryservice.dto.ProductResponseDTO;

import java.util.List;

public interface ProductService {

    ProductResponseDTO createProduct(ProductRequestDTO dto);

    ProductResponseDTO updateProduct(String id, ProductRequestDTO dto);

    ProductResponseDTO getProductById(String id);

    ProductResponseDTO getProductBySku(String sku);

    List<ProductResponseDTO> getAllProducts();

    List<ProductResponseDTO> getProductsByCategory(String category);

    void deleteProduct(String id);

    ProductResponseDTO adjustStock(String id, int delta);
}
