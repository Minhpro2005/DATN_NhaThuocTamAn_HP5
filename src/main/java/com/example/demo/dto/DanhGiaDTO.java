package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DanhGiaDTO {
    private Integer maDG;
    private Integer maKH;
    private Integer maBienThe;
    private Integer maThuoc; // ✅ Lấy từ BienThe
    private String hinhAnh;
    private Integer soSao;
    private String binhLuan;
    private String ngayDanhGia;
    private String tenKhachHang;
    private String tenThuoc;
    private String tenBienThe;
    private String hinhAnhKH; // ✅ Thêm avatar khách hàng


}
