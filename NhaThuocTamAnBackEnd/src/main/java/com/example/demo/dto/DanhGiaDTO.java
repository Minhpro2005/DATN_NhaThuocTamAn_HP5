package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DanhGiaDTO {
    private Integer maDG;
    private Integer maKH;
    private Integer maBienThe;
    private Integer maThuoc; // ✅ Lấy từ BienThe
    private String hinhAnh;
    private Integer soSao;
    private String binhLuan;
    private String ngayDanhGia;
    private String tenKhachHang;
    private String tenThuoc;
    private String tenBienThe;
    private String hinhAnhKH; // ✅ Thêm avatar khách hàng
    
	public DanhGiaDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DanhGiaDTO(Integer maDG, Integer maKH, Integer maBienThe, Integer maThuoc, String hinhAnh, Integer soSao,
			String binhLuan, String ngayDanhGia, String tenKhachHang, String tenThuoc, String tenBienThe,
			String hinhAnhKH) {
		super();
		this.maDG = maDG;
		this.maKH = maKH;
		this.maBienThe = maBienThe;
		this.maThuoc = maThuoc;
		this.hinhAnh = hinhAnh;
		this.soSao = soSao;
		this.binhLuan = binhLuan;
		this.ngayDanhGia = ngayDanhGia;
		this.tenKhachHang = tenKhachHang;
		this.tenThuoc = tenThuoc;
		this.tenBienThe = tenBienThe;
		this.hinhAnhKH = hinhAnhKH;
	}

	public Integer getMaDG() {
		return maDG;
	}

	public void setMaDG(Integer maDG) {
		this.maDG = maDG;
	}

	public Integer getMaKH() {
		return maKH;
	}

	public void setMaKH(Integer maKH) {
		this.maKH = maKH;
	}

	public Integer getMaBienThe() {
		return maBienThe;
	}

	public void setMaBienThe(Integer maBienThe) {
		this.maBienThe = maBienThe;
	}

	public Integer getMaThuoc() {
		return maThuoc;
	}

	public void setMaThuoc(Integer maThuoc) {
		this.maThuoc = maThuoc;
	}

	public String getHinhAnh() {
		return hinhAnh;
	}

	public void setHinhAnh(String hinhAnh) {
		this.hinhAnh = hinhAnh;
	}

	public Integer getSoSao() {
		return soSao;
	}

	public void setSoSao(Integer soSao) {
		this.soSao = soSao;
	}

	public String getBinhLuan() {
		return binhLuan;
	}

	public void setBinhLuan(String binhLuan) {
		this.binhLuan = binhLuan;
	}

	public String getNgayDanhGia() {
		return ngayDanhGia;
	}

	public void setNgayDanhGia(String ngayDanhGia) {
		this.ngayDanhGia = ngayDanhGia;
	}

	public String getTenKhachHang() {
		return tenKhachHang;
	}

	public void setTenKhachHang(String tenKhachHang) {
		this.tenKhachHang = tenKhachHang;
	}

	public String getTenThuoc() {
		return tenThuoc;
	}

	public void setTenThuoc(String tenThuoc) {
		this.tenThuoc = tenThuoc;
	}

	public String getTenBienThe() {
		return tenBienThe;
	}

	public void setTenBienThe(String tenBienThe) {
		this.tenBienThe = tenBienThe;
	}

	public String getHinhAnhKH() {
		return hinhAnhKH;
	}

	public void setHinhAnhKH(String hinhAnhKH) {
		this.hinhAnhKH = hinhAnhKH;
	}

	public String getHoTen() {
		// TODO Auto-generated method stub
		return null;
	}
    
}
