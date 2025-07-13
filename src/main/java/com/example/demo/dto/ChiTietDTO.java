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
public class ChiTietDTO {
    private Integer maBienThe; // dùng nếu có biến thể
    private Integer maThuoc;   // dùng nếu không có biến thể
    private int soLuong;
    private BigDecimal donGia;

    // getters & setters
}

