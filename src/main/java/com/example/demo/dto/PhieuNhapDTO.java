package com.example.demo.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhieuNhapDTO {
    private Integer maNV;
    private Integer maNCC;
    private List<ChiTietDTO> chiTiet;

    @Data
    public static class ChiTietDTO {
        private Integer maBienThe;
        private Integer maThuoc;
        private Integer soLuong;
        private BigDecimal donGiaNhap;
    }
}

