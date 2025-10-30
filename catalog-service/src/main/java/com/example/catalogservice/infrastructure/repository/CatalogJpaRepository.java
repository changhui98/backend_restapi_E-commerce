package com.example.catalogservice.infrastructure.repository;

import com.example.catalogservice.domain.entity.CatalogEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CatalogJpaRepository extends JpaRepository<CatalogEntity, Long> {

    Optional<CatalogEntity> findByProductId(String productId);

}