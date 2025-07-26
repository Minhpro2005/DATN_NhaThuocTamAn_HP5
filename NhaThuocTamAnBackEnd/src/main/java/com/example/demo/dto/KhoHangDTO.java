package com.example.demo.dto;

import lombok.*;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KhoHangDTO {
    private Integer maKhoHang;
    private Integer maBienThe;
    private String tenBienThe;
    private Integer maThuoc;
    private String tenThuoc;
    private Integer soLuongTon;
    private Date ngayCapNhat;
    private String ghiChu;
    private Integer trangThai;
    
	public KhoHangDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public KhoHangDTO(Integer maKhoHang, Integer maBienThe, String tenBienThe, Integer maThuoc, String tenThuoc,
			Integer soLuongTon, Date ngayCapNhat, String ghiChu, Integer trangThai) {
		super();
		this.maKhoHang = maKhoHang;
		this.maBienThe = maBienThe;
		this.tenBienThe = tenBienThe;
		this.maThuoc = maThuoc;
		this.tenThuoc = tenThuoc;
		this.soLuongTon = soLuongTon;
		this.ngayCapNhat = ngayCapNhat;
		this.ghiChu = ghiChu;
		this.trangThai = trangThai;
	}

	public Integer getMaKhoHang() {
		return maKhoHang;
	}

	public void setMaKhoHang(Integer maKhoHang) {
		this.maKhoHang = maKhoHang;
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

	public Integer getSoLuongTon() {
		return soLuongTon;
	}

	public void setSoLuongTon(Integer soLuongTon) {
		this.soLuongTon = soLuongTon;
	}

	public Date getNgayCapNhat() {
		return ngayCapNhat;
	}

	public void setNgayCapNhat(Date ngayCapNhat) {
		this.ngayCapNhat = ngayCapNhat;
	}

	public String getGhiChu() {
		return ghiChu;
	}

	public void setGhiChu(String ghiChu) {
		this.ghiChu = ghiChu;
	}

	public Integer getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(Integer trangThai) {
		this.trangThai = trangThai;
	}

	public static Object builder() {
		// TODO Auto-generated method stub
		return null;
	}
    
}


