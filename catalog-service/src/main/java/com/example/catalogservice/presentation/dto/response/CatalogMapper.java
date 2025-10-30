package com.example.catalogservice.presentation.dto.response;

import com.example.catalogservice.domain.entity.CatalogEntity;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CatalogMapper {

    CatalogMapper INSTANCE = Mappers.getMapper(CatalogMapper.class);

    CatalogResponse toResponse(CatalogEntity entity);

    List<CatalogResponse> toResponseList(List<CatalogEntity> entities);

}