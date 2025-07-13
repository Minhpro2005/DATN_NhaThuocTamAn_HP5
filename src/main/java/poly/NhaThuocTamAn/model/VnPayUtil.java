package poly.NhaThuocTamAn.model;

import java.util.Map;

public class VnPayUtil {

    public static String createPaymentUrl(DonHang donHang) {
        String vnp_TxnRef = donHang.getMaDonHang().toString();
        String vnp_Amount = String.valueOf(donHang.getThanhToan().intValue() * 100); // x100 vì VNĐ

        String returnUrl = "http://localhost:8080/api/thanh-toan/vnpay-return";

        // Tạo URL với các tham số cần thiết (bỏ qua mã hóa ở đây)
        String paymentUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html?" +
                "vnp_TxnRef=" + vnp_TxnRef +
                "&vnp_Amount=" + vnp_Amount +
                "&vnp_ReturnUrl=" + returnUrl;

        return paymentUrl;
    }

    public static boolean verifyPaymentResult(Map<String, String[]> params) {
        // TODO: kiểm tra checksum, trạng thái mã trả về từ VNPAY
        return "00".equals(params.get("vnp_ResponseCode")[0]);
    }
}

