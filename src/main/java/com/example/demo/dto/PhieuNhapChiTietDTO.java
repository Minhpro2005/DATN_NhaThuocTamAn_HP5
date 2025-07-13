package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PhieuNhapChiTietDTO {
    private Integer maPNCT;
    private Integer maPN;
    private Integer soLuong;
    private Double donGiaNhap;

    private Integer maThuoc;
    private String tenThuoc;

    private Integer maBienThe;
    private String tenBienThe;
}

