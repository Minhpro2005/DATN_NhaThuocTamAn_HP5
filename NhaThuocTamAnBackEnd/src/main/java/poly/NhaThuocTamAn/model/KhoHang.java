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

@Entity
@Table(name = "KhoHang")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KhoHang {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer maKhoHang;
	
	@Column(nullable = false, unique = true)
	private Integer maBienThe;
	
	@Column(nullable = false)
	private Integer soLuongTon;
	
	private LocalDateTime ngayCapNhat;
	
	private String ghiChu;
	
	private Integer trangThai;

	public KhoHang(Integer maKhoHang, Integer maBienThe, Integer soLuongTon, LocalDateTime ngayCapNhat, String ghiChu,
			Integer trangThai) {
		super();
		this.maKhoHang = maKhoHang;
		this.maBienThe = maBienThe;
		this.soLuongTon = soLuongTon;
		this.ngayCapNhat = ngayCapNhat;
		this.ghiChu = ghiChu;
		this.trangThai = trangThai;
	}

	public KhoHang() {
		super();
		// TODO Auto-generated constructor stub
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

	public Integer getSoLuongTon() {
		return soLuongTon;
	}

	public void setSoLuongTon(Integer soLuongTon) {
		this.soLuongTon = soLuongTon;
	}

	public LocalDateTime getNgayCapNhat() {
		return ngayCapNhat;
	}

	public void setNgayCapNhat(LocalDateTime ngayCapNhat) {
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
	
	
}
