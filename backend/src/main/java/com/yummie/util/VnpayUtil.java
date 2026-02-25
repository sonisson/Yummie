package com.yummie.util;

import com.yummie.config.VnpayConfig;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.net.URLEncoder;

@Component
@AllArgsConstructor
public class VnpayUtil {

    private final VnpayConfig vnpayConfig;

    public String hmacSHA512(String secretKey, String data) {
        try {
            if (secretKey == null || data == null) throw new NullPointerException("Key hoặc Data không được null");
            Mac hmac512 = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            hmac512.init(secretKeySpec);
            byte[] result = hmac512.doFinal(data.getBytes(StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            for (byte b : result) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi thực hiện băm HMAC SHA512", e);
        }
    }

    public String createPaymentUrl(long orderId, long total, String ipAddr) {
        Map<String, String> vnpParams = new TreeMap<>();
        vnpParams.put("vnp_Version", vnpayConfig.getVersion());
        vnpParams.put("vnp_Command", vnpayConfig.getCommand());
        vnpParams.put("vnp_TmnCode", vnpayConfig.getTmnCode());
        vnpParams.put("vnp_Amount", String.valueOf(total * 100));
        vnpParams.put("vnp_CurrCode", vnpayConfig.getCurrCode());
        vnpParams.put("vnp_TxnRef", String.valueOf(orderId));
        vnpParams.put("vnp_OrderInfo", "Thanh toan don hang #" + orderId);
        vnpParams.put("vnp_OrderType", "other");
        vnpParams.put("vnp_ReturnUrl", vnpayConfig.getReturnUrl());
        vnpParams.put("vnp_Locale", vnpayConfig.getLocale());
        vnpParams.put("vnp_CreateDate", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        vnpParams.put("vnp_IpAddr", ipAddr);
        StringJoiner joiner = new StringJoiner("&");
        vnpParams.forEach((key, value) -> {
            if (value != null && !value.isEmpty()) {
                joiner.add(key + "=" + URLEncoder.encode(value, StandardCharsets.US_ASCII));
            }
        });
        String vnp_SecureHash = hmacSHA512(vnpayConfig.getHashSecret(), joiner.toString());
        return vnpayConfig.getUrl() + "?" + joiner + "&vnp_SecureHash=" + vnp_SecureHash;
    }

    public boolean verifySignature(Map<String, String> params, String secureHash) {
        Map<String, String> filteredParams = new TreeMap<>(params);
        filteredParams.remove("vnp_SecureHash");
        filteredParams.remove("vnp_SecureHashType");
        StringJoiner joiner = new StringJoiner("&");
        filteredParams.forEach((key, value) -> {
            if (value != null && !value.isEmpty()) {
                joiner.add(key + "=" + URLEncoder.encode(value, StandardCharsets.US_ASCII));
            }
        });
        String paramsHash = hmacSHA512(vnpayConfig.getHashSecret(), joiner.toString());
        return paramsHash.equalsIgnoreCase(secureHash);
    }
}