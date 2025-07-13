package poly.NhaThuocTamAn.controller;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import poly.NhaThuocTamAn.service.MomoService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/thanh-toan")
public class ThanhToanController {

    @Autowired
    private MomoService momoService;

    @PostMapping("/tao-url")
    public Map<String, Object> taoUrl(@RequestBody ThanhToanRequest req) {
        Map<String, Object> res = new HashMap<>();
        try {
            String url = momoService.createPaymentUrl(String.valueOf(req.getMaDonHang()), req.getTongTien());
            res.put("success", true);
            res.put("paymentUrl", url);
        } catch (Exception e) {
            res.put("success", false);
            res.put("error", e.getMessage());
        }
        return res;
    }

    @Data
    public static class ThanhToanRequest {
        private int maDonHang;
        private long tongTien;
    }
}
