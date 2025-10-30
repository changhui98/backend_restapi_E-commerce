package com.example.catalogservice.presentation.dto.response;

import com.example.catalogservice.domain.entity.CatalogEntity;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CatalogMapper {

    CatalogResponse toResponse(CatalogEntity entity);

    List<CatalogResponse> toResponseList(List<CatalogEntity> entities);

}