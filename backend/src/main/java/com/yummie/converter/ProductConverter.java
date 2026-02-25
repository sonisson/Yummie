package com.yummie.converter;

import com.yummie.entity.ProductEntity;
import com.yummie.entity.ReviewEntity;
import com.yummie.model.response.PageResponse;
import com.yummie.model.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.OptionalDouble;

@Component
@RequiredArgsConstructor
public class ProductConverter {

    private final ModelMapper modelMapper;
    private final AttributeConverter attributeConverter;
    private final VariantConverter variantConverter;

    public ProductResponse toProductResponse(ProductEntity productEntity) {
        ProductResponse productResponse = modelMapper.map(productEntity, ProductResponse.class);
        OptionalDouble avg = productEntity.getReviewEntities()
                .stream()
                .mapToInt(ReviewEntity::getRating)
                .average();
        productResponse.setRating(avg.isPresent() ? avg.getAsDouble() : null);
        productResponse.setAttributes(attributeConverter.toProductAttributeMap(productEntity.getVariantEntities()));
        productResponse.setVariants(variantConverter.toVariantResponseList(productEntity.getVariantEntities()));
        return productResponse;
    }

    public List<ProductResponse> toProductResponseList(List<ProductEntity> productEntities) {
        return productEntities.stream().map(this::toProductResponse).toList();
    }

    public PageResponse<ProductResponse> toProductResponsePage(Page<ProductEntity> productEntityPage) {
        return new PageResponse<>(
                toProductResponseList(productEntityPage.getContent()),
                productEntityPage.getTotalPages(),
                productEntityPage.getNumber() + 1);
    }
}
