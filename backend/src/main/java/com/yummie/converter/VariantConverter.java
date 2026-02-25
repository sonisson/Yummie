package com.yummie.converter;

import com.yummie.entity.VariantEntity;
import com.yummie.model.response.VariantResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class VariantConverter {

    private final ModelMapper modelMapper;
    private final AttributeConverter attributeConverter;

    public VariantResponse toVariantResponse(VariantEntity variantEntity) {
        VariantResponse variantResponse = modelMapper.map(variantEntity, VariantResponse.class);
        variantResponse.setAttributes(attributeConverter.toVariantAttributeMap(variantEntity.getAttributeEntities()));
        return variantResponse;
    }

    public List<VariantResponse> toVariantResponseList(List<VariantEntity> variantEntities) {
        return variantEntities.stream().map(this::toVariantResponse).toList();
    }

}
