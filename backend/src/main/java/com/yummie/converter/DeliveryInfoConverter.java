package com.yummie.converter;

import com.yummie.entity.DeliveryInfoEntity;
import com.yummie.model.request.DeliveryInfoRequest;
import com.yummie.model.response.DeliveryInfoResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DeliveryInfoConverter {

    private final ModelMapper modelMapper;

    public DeliveryInfoResponse toDeliveryInfoResponse(DeliveryInfoEntity deliveryInfoEntity) {
        return modelMapper.map(deliveryInfoEntity, DeliveryInfoResponse.class);
    }

    public List<DeliveryInfoResponse> toDeliveryInfoResponseList(List<DeliveryInfoEntity> deliveryInfoEntities) {
        return deliveryInfoEntities.stream().map(this::toDeliveryInfoResponse).toList();
    }

    public DeliveryInfoEntity toDeliveryInfoEntity(DeliveryInfoRequest request) {
        return modelMapper.map(request, DeliveryInfoEntity.class);
    }

    public void toDeliveryInfoEntity(DeliveryInfoRequest request, DeliveryInfoEntity deliveryInfoEntity) {
        modelMapper.map(request, deliveryInfoEntity);
    }
}
