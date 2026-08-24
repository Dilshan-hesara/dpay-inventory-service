package lk.dilshan.hesara.d_pay.inventoryservice.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProductResponseDTO {

    private String id;
    private String sku;
    private String name;
    private String description;
    private String category;
    private BigDecimal price;
    private int stockQty;
    private String imageUrl;
    private LocalDateTime createdAt;
}
