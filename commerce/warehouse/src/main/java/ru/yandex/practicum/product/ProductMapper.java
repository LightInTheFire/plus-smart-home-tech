package ru.yandex.practicum.product;

import ru.yandex.practicum.product.model.Dimension;
import ru.yandex.practicum.product.model.Product;
import ru.yandex.practicum.warehouse.dto.DimensionDto;
import ru.yandex.practicum.warehouse.dto.NewProductInWarehouseRequest;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {

    @Mapping(target = "id", source = "productId")
    @Mapping(target = "quantity", constant = "0L")
    Product toProduct(NewProductInWarehouseRequest request);

    DimensionDto toDimensionDto(Dimension dimension);

    Dimension toDimension(DimensionDto dto);

}
