package com.yummie.service;

import com.yummie.converter.AttributeConverter;
import com.yummie.entity.ProductEntity;
import com.yummie.entity.VariantEntity;
import com.yummie.repository.VariantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class VariantService {

    private final VariantRepository variantRepository;
    private final AttributeConverter attributeConverter;

    public VariantEntity getVariantEntity(ProductEntity productEntity, Map<String, String> attributes) {
        List<VariantEntity> variantEntities = variantRepository.findByProductEntity(productEntity);
        for (VariantEntity variantEntity : variantEntities) {
            if (attributeConverter.toVariantAttributeMap(variantEntity.getAttributeEntities()).equals(attributes)) {
                return variantEntity;
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Variant not found");
    }
}
