package poly.NhaThuocTamAn.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import poly.NhaThuocTamAn.service.MomoService;

@RestController
@RequestMapping("/api/thanh-toan")
@CrossOrigin("*")
public class PaymentController {

    @Autowired
    private MomoService momoService;

    @GetMapping("/momo")
    public Map<String, Object> thanhToanMomo(@RequestParam String orderId, @RequestParam long amount) {
        Map<String, Object> response = new HashMap<>();
        try {
            String payUrl = momoService.createPaymentUrl(orderId, amount);
            response.put("success", true);
            response.put("payUrl", payUrl);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
        }
        return response;
    }
    
    @GetMapping("/momo-return")
    public String momoReturn(@RequestParam Map<String, String> params) {
        // Có thể xử lý đơn hàng ở đây dựa trên orderId và resultCode
        String orderId = params.get("orderId");
        String resultCode = params.get("resultCode");
        return "Thanh toán MOMO " + ("0".equals(resultCode) ? "thành công" : "thất bại");
    }

    @PostMapping("/momo-notify")
    public Map<String, Object> momoNotify(@RequestBody Map<String, Object> payload) {
        // Xác minh và cập nhật trạng thái thanh toán tại đây
        System.out.println("Momo notify: " + payload);

        Map<String, Object> res = new HashMap<>();
        res.put("message", "Notification received");
        res.put("resultCode", 0);
        res.put("orderId", payload.get("orderId"));
        res.put("requestId", payload.get("requestId"));
        return res;
    }

}
