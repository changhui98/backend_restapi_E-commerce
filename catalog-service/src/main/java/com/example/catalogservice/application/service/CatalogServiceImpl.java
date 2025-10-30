package com.example.catalogservice.application.service;

import com.example.catalogservice.domain.entity.CatalogEntity;
import com.example.catalogservice.infrastructure.repository.CatalogJpaRepository;
import com.example.catalogservice.presentation.dto.request.CatalogRequest;
import com.example.catalogservice.presentation.dto.response.CatalogMapper;
import com.example.catalogservice.presentation.dto.response.CatalogResponse;
import java.util.List;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Data
@Slf4j
@Service
@RequiredArgsConstructor
public class CatalogServiceImpl implements CatalogService {

    private final CatalogJpaRepository catalogJpaRepository;

    private final Environment env;

    private final CatalogMapper catalogMapper;


    @Override
    public List<CatalogResponse> getAllCatalogs() {
        List<CatalogEntity> catalogList = catalogJpaRepository.findAll();

        return catalogMapper.toResponseList(catalogList);
    }

    @Override
    public void createCatalog(CatalogRequest catalogRequest) {

        catalogJpaRepository.save(CatalogEntity.of(
            catalogRequest.getProductId(),
            catalogRequest.getProductName(),
            catalogRequest.getStock(),
            catalogRequest.getUnitPrice()
        ));
    }
}