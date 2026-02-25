package com.yummie.converter;

import com.yummie.entity.AttributeEntity;
import com.yummie.entity.VariantEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AttributeConverter {

    public Map<String, String> toVariantAttributeMap(List<AttributeEntity> attributeEntities) {
        return attributeEntities.stream()
                .collect(Collectors.toMap(AttributeEntity::getName, AttributeEntity::getValue));
    }

    public Map<String, Set<String>> toProductAttributeMap(List<VariantEntity> variantEntities) {
        return variantEntities.stream()
                .flatMap(v -> v.getAttributeEntities().stream())
                .collect(Collectors.groupingBy(
                        AttributeEntity::getName,
                        Collectors.mapping(AttributeEntity::getValue, Collectors.toSet())
                ));
    }
}
