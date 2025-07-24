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

	public PhieuNhapDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PhieuNhapDTO(Integer maNV, Integer maNCC, List<ChiTietDTO> chiTiet) {
		super();
		this.maNV = maNV;
		this.maNCC = maNCC;
		this.chiTiet = chiTiet;
	}

	public Integer getMaNV() {
		return maNV;
	}

	public void setMaNV(Integer maNV) {
		this.maNV = maNV;
	}

	public Integer getMaNCC() {
		return maNCC;
	}

	public void setMaNCC(Integer maNCC) {
		this.maNCC = maNCC;
	}

	public List<ChiTietDTO> getChiTiet() {
		return chiTiet;
	}

	public void setChiTiet(List<ChiTietDTO> chiTiet) {
		this.chiTiet = chiTiet;
	}
    
    
}

