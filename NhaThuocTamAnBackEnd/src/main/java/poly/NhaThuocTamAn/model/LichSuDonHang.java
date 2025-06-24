package poly.NhaThuocTamAn.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "LichSuDonHang")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LichSuDonHang {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer maLSDH;
	
	@ManyToOne
	@JoinColumn(name="maDonHang", table="DonHang")
	private DonHang maDonHang;
	
	private Integer trangThaiCu;
	
	private Integer trangThaiMoi;
	
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime thoiGian;
	
	private String ghiChu;

	public LichSuDonHang() {
		super();
		// TODO Auto-generated constructor stub
	}

	public LichSuDonHang(Integer maLSDH, DonHang maDonHang, Integer trangThaiCu, Integer trangThaiMoi,
			LocalDateTime thoiGian, String ghiChu) {
		super();
		this.maLSDH = maLSDH;
		this.maDonHang = maDonHang;
		this.trangThaiCu = trangThaiCu;
		this.trangThaiMoi = trangThaiMoi;
		this.thoiGian = thoiGian;
		this.ghiChu = ghiChu;
	}

	public Integer getMaLSDH() {
		return maLSDH;
	}

	public void setMaLSDH(Integer maLSDH) {
		this.maLSDH = maLSDH;
	}

	public DonHang getMaDonHang() {
		return maDonHang;
	}

	public void setMaDonHang(DonHang maDonHang) {
		this.maDonHang = maDonHang;
	}

	public Integer getTrangThaiCu() {
		return trangThaiCu;
	}

	public void setTrangThaiCu(Integer trangThaiCu) {
		this.trangThaiCu = trangThaiCu;
	}

	public Integer getTrangThaiMoi() {
		return trangThaiMoi;
	}

	public void setTrangThaiMoi(Integer trangThaiMoi) {
		this.trangThaiMoi = trangThaiMoi;
	}

	public LocalDateTime getThoiGian() {
		return thoiGian;
	}

	public void setThoiGian(LocalDateTime thoiGian) {
		this.thoiGian = thoiGian;
	}

	public String getGhiChu() {
		return ghiChu;
	}

	public void setGhiChu(String ghiChu) {
		this.ghiChu = ghiChu;
	}
	
	
}
