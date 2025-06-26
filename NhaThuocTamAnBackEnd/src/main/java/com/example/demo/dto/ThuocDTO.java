package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ThuocDTO {
	 private Integer maThuoc;
	    private String tenThuoc;
	    private String donViTinh;
	    private String congDung;
	    private String moTaThem;
	    private String huongDanSuDung;
	    private String dangBaoChe;
	    private String quyCach;
	    private String xuatXu;
	    private String thanhPhan;
	    private LocalDate hanSuDung;
	    private LocalDate ngaySanXuat;
	    private BigDecimal giaBan;
	    private Boolean trangThai;

	    private Integer maDM;
	    private String tenDanhMuc;

	    private Integer maNCC;
	    private String tenNhaCungCap;

	    private String hinhAnhChinh;
	    private List<String> thumbnails;


}
