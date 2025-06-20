package poly.NhaThuocTamAn.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "DonHangChiTiet")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DonHangChiTiet {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer maDHCT;
	
	@ManyToOne
	@JoinColumn(name="maDonHang", table="DonHang")
	private DonHang maDonHang;
	
	@ManyToOne
	@JoinColumn(name="maBienThe", table="BienTheThuoc")
	private BienTheThuoc maBienThe;
	
	private Integer soLuong;
	
	private Double donGia;
	
	private Double thanhTien=soLuong*donGia;

	public DonHangChiTiet() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DonHangChiTiet(Integer maDHCT, DonHang maDonHang, BienTheThuoc maBienThe, Integer soLuong, Double donGia,
			Double thanhTien) {
		super();
		this.maDHCT = maDHCT;
		this.maDonHang = maDonHang;
		this.maBienThe = maBienThe;
		this.soLuong = soLuong;
		this.donGia = donGia;
		this.thanhTien = thanhTien;
	}

	public Integer getMaDHCT() {
		return maDHCT;
	}

	public void setMaDHCT(Integer maDHCT) {
		this.maDHCT = maDHCT;
	}

	public DonHang getMaDonHang() {
		return maDonHang;
	}

	public void setMaDonHang(DonHang maDonHang) {
		this.maDonHang = maDonHang;
	}

	public BienTheThuoc getMaBienThe() {
		return maBienThe;
	}

	public void setMaBienThe(BienTheThuoc maBienThe) {
		this.maBienThe = maBienThe;
	}

	public Integer getSoLuong() {
		return soLuong;
	}

	public void setSoLuong(Integer soLuong) {
		this.soLuong = soLuong;
	}

	public Double getDonGia() {
		return donGia;
	}

	public void setDonGia(Double donGia) {
		this.donGia = donGia;
	}

	public Double getThanhTien() {
		return thanhTien;
	}

	public void setThanhTien(Double thanhTien) {
		this.thanhTien = thanhTien;
	}
	
	
}
