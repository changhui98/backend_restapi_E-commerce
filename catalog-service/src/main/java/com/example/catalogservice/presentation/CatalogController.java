package com.example.catalogservice.presentation;

import com.example.catalogservice.application.service.CatalogService;
import com.example.catalogservice.presentation.dto.request.CatalogRequest;
import com.example.catalogservice.presentation.dto.response.CatalogResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/catalog-service")
public class CatalogController {

    private final CatalogService catalogService;

    @GetMapping("/catalogs")
    public ResponseEntity<List<CatalogResponse>> catalogList() {

        return ResponseEntity.status(HttpStatus.OK).body(catalogService.getAllCatalogs());
    }

    @PostMapping("/catalog")
    public ResponseEntity<String> createCatalog(@RequestBody CatalogRequest catalogRequest) {
        catalogService.createCatalog(catalogRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body("상품 등록 완료");
    }
}