package com.example.demo.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ThuocBanChayDTO {
    private Integer maThuoc;
    private String tenThuoc;
    private Long tongSoLuong;
    private String hinhAnhChinh;
    private BigDecimal giaBan;
}
