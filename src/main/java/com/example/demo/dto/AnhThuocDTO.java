package com.example.demo.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnhThuocDTO {
	private Integer maAnhThuoc;
    private String hinhAnh;
    private Boolean anhChinh;
    private Integer maThuoc;
}
