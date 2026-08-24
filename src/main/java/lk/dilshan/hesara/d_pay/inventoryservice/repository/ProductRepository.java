package lk.dilshan.hesara.d_pay.inventoryservice.repository;

import lk.dilshan.hesara.d_pay.inventoryservice.entity.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

    boolean existsBySku(String sku);

    Optional<Product> findBySku(String sku);
}
