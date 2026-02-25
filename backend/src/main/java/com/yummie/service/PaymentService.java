package com.yummie.service;

import com.yummie.entity.OrderEntity;
import com.yummie.model.response.PaymentUrlResponse;
import com.yummie.repository.OrderRepository;
import com.yummie.util.VnpayUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final VnpayUtil vnpayUtil;
    private final OrderRepository orderRepository;
    private final OrderService orderService;

    public ResponseEntity<?> generatePaymentUrl(long orderId, String email, String ipAddress) {
        if (!orderRepository.existsByIdAndUserEntityEmail(orderId, email)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to access this order.");
        }
        long total = orderRepository.getTotalByOrderId(orderId);
        String url = vnpayUtil.createPaymentUrl(orderId, total, ipAddress);
        PaymentUrlResponse paymentUrlResponse = new PaymentUrlResponse();
        paymentUrlResponse.setUrl(url);
        return ResponseEntity.ok(paymentUrlResponse);
    }

    public String processPaymentIpn(Map<String, String> params) {
        String txnRef = params.get("vnp_TxnRef");
        String amount = params.get("vnp_Amount");
        String responseCode = params.get("vnp_ResponseCode");
        String secureHash = params.get("vnp_SecureHash");
        if (!vnpayUtil.verifySignature(params, secureHash)) {
            return "ResponseCode=99";
        }
        OrderEntity orderEntity = orderRepository.findById(Long.parseLong(txnRef)).orElse(null);
        if (orderEntity == null
                || orderEntity.getTotal() * 100 != Long.parseLong(amount)
                || !"PAYMENT_PENDING".equals(orderEntity.getLatestOrderStatusEntity().getStatusEntity().getName())) {
            return "ResponseCode=99";
        }
        if ("00".equals(responseCode)) {
            orderService.addOrderStatus(orderEntity, "PAID", "Thanh toán thành công");
        } else {
            orderService.addOrderStatus(orderEntity, "FAILED", "Thanh toán thất bại");
        }
        orderRepository.save(orderEntity);
        return "ResponseCode=00";
    }
}
