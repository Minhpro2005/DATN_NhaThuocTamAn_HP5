package poly.NhaThuocTamAn.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "NhanVien")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NhanVien {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer maNV;
	
	@Column(nullable = false, length = 100)
	private String matKhau;
	
	@Column(nullable = false)
	private String hoTen;
	
	private String email;
	
	@Column(length = 15)
	private String soDienThoai;
	
	@Column(length = 20, unique = true)
	private String cccd;
	
	@Temporal(TemporalType.DATE)
	private Date ngaySinh;
	
	private boolean gioiTinh;
	
	private String diaChi;
	
	private String hinhAnh;
	
	@Column(nullable = false)
	private Integer vaiTro;
	
	private boolean trangThai;

	public NhanVien(Integer maNV, String matKhau, String hoTen, String email, String soDienThoai, String cccd,
			Date ngaySinh, boolean gioiTinh, String diaChi, String hinhAnh, Integer vaiTro, boolean trangThai) {
		super();
		this.maNV = maNV;
		this.matKhau = matKhau;
		this.hoTen = hoTen;
		this.email = email;
		this.soDienThoai = soDienThoai;
		this.cccd = cccd;
		this.ngaySinh = ngaySinh;
		this.gioiTinh = gioiTinh;
		this.diaChi = diaChi;
		this.hinhAnh = hinhAnh;
		this.vaiTro = vaiTro;
		this.trangThai = trangThai;
	}

	public NhanVien() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getMaNV() {
		return maNV;
	}

	public void setMaNV(Integer maNV) {
		this.maNV = maNV;
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

	public String getCccd() {
		return cccd;
	}

	public void setCccd(String cccd) {
		this.cccd = cccd;
	}

	public Date getNgaySinh() {
		return ngaySinh;
	}

	public void setNgaySinh(Date ngaySinh) {
		this.ngaySinh = ngaySinh;
	}

	public boolean getGioiTinh() {
		return gioiTinh;
	}

	public void setGioiTinh(boolean gioiTinh) {
		this.gioiTinh = gioiTinh;
	}

	public String getDiaChi() {
		return diaChi;
	}

	public void setDiaChi(String diaChi) {
		this.diaChi = diaChi;
	}

	public String getHinhAnh() {
		return hinhAnh;
	}

	public void setHinhAnh(String hinhAnh) {
		this.hinhAnh = hinhAnh;
	}

	public Integer getVaiTro() {
		return vaiTro;
	}

	public void setVaiTro(Integer vaiTro) {
		this.vaiTro = vaiTro;
	}

	public boolean getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(boolean trangThai) {
		this.trangThai = trangThai;
	}
	
	
}
