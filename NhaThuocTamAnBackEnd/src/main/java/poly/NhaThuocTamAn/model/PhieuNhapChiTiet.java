package poly.NhaThuocTamAn.model;

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
@Table(name = "PhieuNhapChiTiet")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PhieuNhapChiTiet {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer maPNCT;
	
	private Integer maPN;
	
	private Integer maBienThe;
	
	private Integer soLuong;
	
	private Double donGiaNhap;

	public PhieuNhapChiTiet() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PhieuNhapChiTiet(Integer maPNCT, Integer maPN, Integer maBienThe, Integer soLuong, Double donGiaNhap) {
		super();
		this.maPNCT = maPNCT;
		this.maPN = maPN;
		this.maBienThe = maBienThe;
		this.soLuong = soLuong;
		this.donGiaNhap = donGiaNhap;
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

	public Integer getMaBienThe() {
		return maBienThe;
	}

	public void setMaBienThe(Integer maBienThe) {
		this.maBienThe = maBienThe;
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
	
	
}
