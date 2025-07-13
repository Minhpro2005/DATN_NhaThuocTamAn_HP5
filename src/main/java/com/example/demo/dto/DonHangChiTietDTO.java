package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DonHangChiTietDTO {
    private Integer maThuoc;
    private Integer maBienThe;
    private String tenThuoc;
    private String hinhAnh;
    private Integer soLuong;
    private Double donGia;
    private String hinhAnhChinh; // ✅ thêm dòng này
    // getters & setters...
}



