package com.example.demo.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BienTheThuocDTO {
	 private Integer maBienThe;
	    private String tenBienThe;
	    private BigDecimal giaBan;
	    private String hinhAnh;
	    private String moTa;
	    private String donViTinh;
	    private Boolean trangThai;
	    private Integer maThuoc;
	    private String tenThuoc; // chỉ dùng để trả ra, không lưu vào DB
}