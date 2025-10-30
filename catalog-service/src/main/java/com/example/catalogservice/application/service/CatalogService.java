package com.example.catalogservice.application.service;

import com.example.catalogservice.domain.entity.CatalogEntity;
import com.example.catalogservice.presentation.dto.request.CatalogRequest;
import com.example.catalogservice.presentation.dto.response.CatalogResponse;
import java.util.List;

public interface CatalogService {

    List<CatalogResponse> getAllCatalogs();

    void createCatalog(CatalogRequest catalogRequest);
}