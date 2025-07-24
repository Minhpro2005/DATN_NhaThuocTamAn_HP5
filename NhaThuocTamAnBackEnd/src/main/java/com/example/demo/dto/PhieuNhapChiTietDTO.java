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
    
	public PhieuNhapChiTietDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PhieuNhapChiTietDTO(Integer maPNCT, Integer maPN, Integer soLuong, Double donGiaNhap, Integer maThuoc,
			String tenThuoc, Integer maBienThe, String tenBienThe) {
		super();
		this.maPNCT = maPNCT;
		this.maPN = maPN;
		this.soLuong = soLuong;
		this.donGiaNhap = donGiaNhap;
		this.maThuoc = maThuoc;
		this.tenThuoc = tenThuoc;
		this.maBienThe = maBienThe;
		this.tenBienThe = tenBienThe;
	}

	public Integer getMaPNCT() {
		return maPNCT;
	}

	public void setMaPNCT(Integer maPNCT) {
		this.maPNCT = maPNCT;
	}

	public Integer getMaPN() {
		return maPN;
	}

	public void setMaPN(Integer maPN) {
		this.maPN = maPN;
	}

	public Integer getSoLuong() {
		return soLuong;
	}

	public void setSoLuong(Integer soLuong) {
		this.soLuong = soLuong;
	}

	public Double getDonGiaNhap() {
		return donGiaNhap;
	}

	public void setDonGiaNhap(Double donGiaNhap) {
		this.donGiaNhap = donGiaNhap;
	}

	public Integer getMaThuoc() {
		return maThuoc;
	}

	public void setMaThuoc(Integer maThuoc) {
		this.maThuoc = maThuoc;
	}

	public String getTenThuoc() {
		return tenThuoc;
	}

	public void setTenThuoc(String tenThuoc) {
		this.tenThuoc = tenThuoc;
	}

	public Integer getMaBienThe() {
		return maBienThe;
	}

	public void setMaBienThe(Integer maBienThe) {
		this.maBienThe = maBienThe;
	}

	public String getTenBienThe() {
		return tenBienThe;
	}

	public void setTenBienThe(String tenBienThe) {
		this.tenBienThe = tenBienThe;
	}
    
    
}

