package poly.NhaThuocTamAn.model;

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
@Table(name = "PhieuNhapChiTiet")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PhieuNhapChiTiet {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer maPNCT;
	
	@ManyToOne
	@JoinColumn(name="maPN", table="PhieuNhap")
	private PhieuNhap maPN;
	
	@ManyToOne
	@JoinColumn(name="maBienThe", table="BienTheThuoc")
	private BienTheThuoc maBienThe;
	
	private Integer soLuong;
	
	private Double donGiaNhap;

	public PhieuNhapChiTiet() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PhieuNhapChiTiet(Integer maPNCT, PhieuNhap maPN, BienTheThuoc maBienThe, Integer soLuong,
			Double donGiaNhap) {
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

	public PhieuNhap getMaPN() {
		return maPN;
	}

	public void setMaPN(PhieuNhap maPN) {
		this.maPN = maPN;
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

	public Double getDonGiaNhap() {
		return donGiaNhap;
	}

	public void setDonGiaNhap(Double donGiaNhap) {
		this.donGiaNhap = donGiaNhap;
	}

}
