package com.yummie.service;

import com.yummie.converter.DeliveryInfoConverter;
import com.yummie.entity.DeliveryInfoEntity;
import com.yummie.entity.UserEntity;
import com.yummie.model.request.DeliveryInfoRequest;
import com.yummie.model.response.DeliveryInfoResponse;
import com.yummie.repository.DeliveryInfoRepository;
import com.yummie.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryInfoService {

    private final DeliveryInfoRepository deliveryInfoRepository;
    private final DeliveryInfoConverter deliveryInfoConverter;
    private final UserRepository userRepository;

    public DeliveryInfoEntity findByIdAndUserEntityEmail(long id, String email) {
        DeliveryInfoEntity deliveryInfoEntity = deliveryInfoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Delivery information not found"));
        if (!email.equals(deliveryInfoEntity.getUserEntity().getEmail())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to access this delivery information.");
        }
        return deliveryInfoEntity;
    }

    public ResponseEntity<?> getDeliveryInfoResponseList(String email) {
        List<DeliveryInfoEntity> deliveryInfoEntities = deliveryInfoRepository.findByUserEntityEmailOrderByIsDefaultDesc(email);
        List<DeliveryInfoResponse> responseList = deliveryInfoConverter.toDeliveryInfoResponseList(deliveryInfoEntities);
        return ResponseEntity.ok(responseList);
    }

    public ResponseEntity<?> getDefaultDeliveryInfo(String email) {
        DeliveryInfoEntity deliveryInfoEntity = deliveryInfoRepository.findByUserEntityEmailAndIsDefaultTrue(email);
        if (deliveryInfoEntity == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Default delivery information not found.");
        }
        DeliveryInfoResponse response = deliveryInfoConverter.toDeliveryInfoResponse(deliveryInfoEntity);
        return ResponseEntity.ok(response);
    }

    @Transactional
    public ResponseEntity<?> createDeliveryInfo(String email, DeliveryInfoRequest request) {
        DeliveryInfoEntity deliveryInfoEntity = deliveryInfoConverter.toDeliveryInfoEntity(request);
        DeliveryInfoEntity defaultDeliveryInfoEntity = deliveryInfoRepository.findByUserEntityEmailAndIsDefaultTrue(email);
        if (request.isDefault() && defaultDeliveryInfoEntity != null) {
            defaultDeliveryInfoEntity.setDefault(false);
            deliveryInfoRepository.save(defaultDeliveryInfoEntity);
        } else if (!request.isDefault() && defaultDeliveryInfoEntity == null) {
            deliveryInfoEntity.setDefault(true);
        }
        UserEntity userEntity = userRepository.findByEmail(email);
        deliveryInfoEntity.setUserEntity(userEntity);
        deliveryInfoEntity = deliveryInfoRepository.save(deliveryInfoEntity);
        DeliveryInfoResponse response = deliveryInfoConverter.toDeliveryInfoResponse(deliveryInfoEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Transactional
    public ResponseEntity<?> updateDeliveryInfo(String email, long id, DeliveryInfoRequest request) {
        DeliveryInfoEntity deliveryInfoEntity = findByIdAndUserEntityEmail(id, email);
        if (deliveryInfoEntity.isDefault() && !request.isDefault()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Cannot unset the default delivery information. Please set another delivery information as default first.");
        }
        if (!deliveryInfoEntity.isDefault() && request.isDefault()) {
            DeliveryInfoEntity defaultDeliveryEntity = deliveryInfoRepository.findByUserEntityEmailAndIsDefaultTrue(email);
            //always true
            if (defaultDeliveryEntity != null) {
                defaultDeliveryEntity.setDefault(false);
                deliveryInfoRepository.save(defaultDeliveryEntity);
            }
        }
        deliveryInfoConverter.toDeliveryInfoEntity(request, deliveryInfoEntity);
        deliveryInfoRepository.save(deliveryInfoEntity);
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<?> deleteDeliveryInfo(String email, long id) {
        DeliveryInfoEntity deliveryInfoEntity = findByIdAndUserEntityEmail(id, email);
        if (deliveryInfoEntity.isDefault()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Cannot delete default delivery information.");
        }
        deliveryInfoRepository.delete(deliveryInfoEntity);
        return ResponseEntity.noContent().build();
    }
}
