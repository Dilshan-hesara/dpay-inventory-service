package lk.dilshan.hesara.d_pay.inventoryservice.mapper;

import lk.dilshan.hesara.d_pay.inventoryservice.dto.ProductRequestDTO;
import lk.dilshan.hesara.d_pay.inventoryservice.dto.ProductResponseDTO;
import lk.dilshan.hesara.d_pay.inventoryservice.entity.Product;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "imageUrl", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Product toEntity(ProductRequestDTO dto);

    ProductResponseDTO toDto(Product product);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "imageUrl", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromDto(ProductRequestDTO dto, @MappingTarget Product entity);
}
