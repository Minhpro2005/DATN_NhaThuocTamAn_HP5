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
    
	public KhuyenMaiDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public KhuyenMaiDTO(Integer maKM, String tenKM, String maCode, String loaiKM, BigDecimal giaTri,
			BigDecimal giaTriToiDa, BigDecimal donHangToiThieu, Integer soLuong, Integer daSuDung, LocalDate ngayBatDau,
			LocalDate ngayKetThuc, String moTa, Boolean trangThai) {
		super();
		this.maKM = maKM;
		this.tenKM = tenKM;
		this.maCode = maCode;
		this.loaiKM = loaiKM;
		this.giaTri = giaTri;
		this.giaTriToiDa = giaTriToiDa;
		this.donHangToiThieu = donHangToiThieu;
		this.soLuong = soLuong;
		this.daSuDung = daSuDung;
		this.ngayBatDau = ngayBatDau;
		this.ngayKetThuc = ngayKetThuc;
		this.moTa = moTa;
		this.trangThai = trangThai;
	}

	public Integer getMaKM() {
		return maKM;
	}

	public void setMaKM(Integer maKM) {
		this.maKM = maKM;
	}

	public String getTenKM() {
		return tenKM;
	}

	public void setTenKM(String tenKM) {
		this.tenKM = tenKM;
	}

	public String getMaCode() {
		return maCode;
	}

	public void setMaCode(String maCode) {
		this.maCode = maCode;
	}

	public String getLoaiKM() {
		return loaiKM;
	}

	public void setLoaiKM(String loaiKM) {
		this.loaiKM = loaiKM;
	}

	public BigDecimal getGiaTri() {
		return giaTri;
	}

	public void setGiaTri(BigDecimal giaTri) {
		this.giaTri = giaTri;
	}

	public BigDecimal getGiaTriToiDa() {
		return giaTriToiDa;
	}

	public void setGiaTriToiDa(BigDecimal giaTriToiDa) {
		this.giaTriToiDa = giaTriToiDa;
	}

	public BigDecimal getDonHangToiThieu() {
		return donHangToiThieu;
	}

	public void setDonHangToiThieu(BigDecimal donHangToiThieu) {
		this.donHangToiThieu = donHangToiThieu;
	}

	public Integer getSoLuong() {
		return soLuong;
	}

	public void setSoLuong(Integer soLuong) {
		this.soLuong = soLuong;
	}

	public Integer getDaSuDung() {
		return daSuDung;
	}

	public void setDaSuDung(Integer daSuDung) {
		this.daSuDung = daSuDung;
	}

	public LocalDate getNgayBatDau() {
		return ngayBatDau;
	}

	public void setNgayBatDau(LocalDate ngayBatDau) {
		this.ngayBatDau = ngayBatDau;
	}

	public LocalDate getNgayKetThuc() {
		return ngayKetThuc;
	}

	public void setNgayKetThuc(LocalDate ngayKetThuc) {
		this.ngayKetThuc = ngayKetThuc;
	}

	public String getMoTa() {
		return moTa;
	}

	public void setMoTa(String moTa) {
		this.moTa = moTa;
	}

	public Boolean getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(Boolean trangThai) {
		this.trangThai = trangThai;
	}
    
    
}

