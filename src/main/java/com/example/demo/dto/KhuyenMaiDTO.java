package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KhuyenMaiDTO {
    private Integer maKM;
    private String tenKM;
    private String maCode;
    private String loaiKM;
    private BigDecimal giaTri;
    private BigDecimal giaTriToiDa;
    private BigDecimal donHangToiThieu;
    private Integer soLuong;
    private Integer daSuDung;
    private LocalDate ngayBatDau;
    private LocalDate ngayKetThuc;
    private String moTa;
    private Boolean trangThai;
}

