package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DonHangDTO {
    // Dùng khi tạo đơn
    private Integer maKH;
    private Integer maKM;
    private String diaChiNhan;
    private String soDienThoaiNhan;
    private String hinhThucThanhToan;
    private String ghiChu;
    private String hoTenNguoiNhan;
    private String tenKH;
    private BigDecimal giamGia; // ✅ Thêm dòng này
    private List<ChiTietDTO> chiTiet;

    // Dùng khi hiển thị đơn hàng
    private Integer maDonHang;
    private LocalDateTime ngayDat;
    private Integer trangThai;
    private BigDecimal thanhToan;
    private List<DonHangChiTietDTO> chiTietHienThi;
}
