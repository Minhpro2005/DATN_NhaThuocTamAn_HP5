package poly.NhaThuocTamAn.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//File: poly.NhaThuocTamAn.model.KhachHang.java

@Entity
@Table(name = "KhachHang")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KhachHang {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Integer maKH;

 @Column(nullable = false, length = 100)
 private String matKhau;

 @Column(nullable = false)
 private String hoTen;

 private String diaChi;

 @Column(unique = true)
 private String email;

 @Column(length = 15)
 private String soDienThoai;

 private String hinhAnh;

 private boolean trangThai = true;

 private String maXacThuc;

 // ✅ Đổi kiểu boolean -> boolean
 private boolean daXacThuc;

 private LocalDateTime thoiGianHetHanMa;

public KhachHang() {
	super();
	// TODO Auto-generated constructor stub
}

public KhachHang(Integer maKH, String matKhau, String hoTen, String diaChi, String email, String soDienThoai,
		String hinhAnh, boolean trangThai, String maXacThuc, boolean daXacThuc, LocalDateTime thoiGianHetHanMa) {
	super();
	this.maKH = maKH;
	this.matKhau = matKhau;
	this.hoTen = hoTen;
	this.diaChi = diaChi;
	this.email = email;
	this.soDienThoai = soDienThoai;
	this.hinhAnh = hinhAnh;
	this.trangThai = trangThai;
	this.maXacThuc = maXacThuc;
	this.daXacThuc = daXacThuc;
	this.thoiGianHetHanMa = thoiGianHetHanMa;
}

public Integer getMaKH() {
	return maKH;
}

public void setMaKH(Integer maKH) {
	this.maKH = maKH;
}

public String getMatKhau() {
	return matKhau;
}

public void setMatKhau(String matKhau) {
	this.matKhau = matKhau;
}

public String getHoTen() {
	return hoTen;
}

public void setHoTen(String hoTen) {
	this.hoTen = hoTen;
}

public String getDiaChi() {
	return diaChi;
}

public void setDiaChi(String diaChi) {
	this.diaChi = diaChi;
}

public String getEmail() {
	return email;
}

public void setEmail(String email) {
	this.email = email;
}

public String getSoDienThoai() {
	return soDienThoai;
}

public void setSoDienThoai(String soDienThoai) {
	this.soDienThoai = soDienThoai;
}

public String getHinhAnh() {
	return hinhAnh;
}

public void setHinhAnh(String hinhAnh) {
	this.hinhAnh = hinhAnh;
}

public boolean getTrangThai() {
	return trangThai;
}

public void setTrangThai(boolean trangThai) {
	this.trangThai = trangThai;
}

public String getMaXacThuc() {
	return maXacThuc;
}

public void setMaXacThuc(String maXacThuc) {
	this.maXacThuc = maXacThuc;
}

public boolean getDaXacThuc() {
	return daXacThuc;
}

public void setDaXacThuc(boolean daXacThuc) {
	this.daXacThuc = daXacThuc;
}

public LocalDateTime getThoiGianHetHanMa() {
	return thoiGianHetHanMa;
}

public void setThoiGianHetHanMa(LocalDateTime thoiGianHetHanMa) {
	this.thoiGianHetHanMa = thoiGianHetHanMa;
}
 
 
}
