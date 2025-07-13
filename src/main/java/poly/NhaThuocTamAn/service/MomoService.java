package poly.NhaThuocTamAn.service;

import java.util.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.UUID;

@Service
public class MomoService {

    @Value("${momo.partnerCode}")
    private String partnerCode;

    @Value("${momo.accessKey}")
    private String accessKey;

    @Value("${momo.secretKey}")
    private String secretKey;

    @Value("${momo.returnUrl}")
    private String returnUrl;

    @Value("${momo.notifyUrl}")
    private String notifyUrl;

    public String createPaymentUrl(String orderId, long amount) throws Exception {
        String requestId = UUID.randomUUID().toString();
        String uniqueOrderId = orderId + "_" + System.currentTimeMillis(); // Đảm bảo không trùng
        String orderInfo = "Thanh toán đơn hàng #" + orderId;

        String rawHash = "accessKey=" + accessKey +
                "&amount=" + amount +
                "&extraData=" +
                "&ipnUrl=" + notifyUrl +
                "&orderId=" + uniqueOrderId +
                "&orderInfo=" + orderInfo +
                "&partnerCode=" + partnerCode +
                "&redirectUrl=" + returnUrl +
                "&requestId=" + requestId +
                "&requestType=captureWallet";

        String signature = hmacSHA256(secretKey, rawHash);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("partnerCode", partnerCode);
        body.put("accessKey", accessKey);
        body.put("requestId", requestId);
        body.put("amount", String.valueOf(amount));
        body.put("orderId", uniqueOrderId); // dùng uniqueOrderId
        body.put("orderInfo", orderInfo);
        body.put("redirectUrl", returnUrl);
        body.put("ipnUrl", notifyUrl);
        body.put("extraData", "");
        body.put("requestType", "captureWallet");
        body.put("signature", signature);
        body.put("lang", "vi");

        String response = sendHttpPost("https://test-payment.momo.vn/v2/gateway/api/create", body);
        return extractPayUrl(response);
    }


    private String hmacSHA256(String key, String data) throws Exception {
        Mac hmac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
        hmac.init(secretKeySpec);
        byte[] hash = hmac.doFinal(data.getBytes("UTF-8"));
        return bytesToHex(hash);
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b)); // convert to lowercase HEX
        }
        return sb.toString();
    }

    private String sendHttpPost(String url, Map<String, Object> body) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        return response.getBody();
    }

    private String extractPayUrl(String json) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(json);

        if (node.has("payUrl")) {
            return node.get("payUrl").asText();
        } else if (node.has("message")) {
            throw new RuntimeException("MoMo lỗi: " + node.get("message").asText());
        } else {
            throw new RuntimeException("Lỗi không xác định từ MoMo");
        }
    }

}
