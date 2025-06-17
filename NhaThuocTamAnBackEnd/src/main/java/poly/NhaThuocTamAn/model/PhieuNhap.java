package poly.NhaThuocTamAn.model;

import java.time.LocalDateTime;

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
@Table(name = "PhieuNhap")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PhieuNhap {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer maPN;
	
	private LocalDateTime ngayNhap;
	
	private NhanVien maNV;
	
	private Integer maNCC;
	
	private Double tongTien;

	public PhieuNhap() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PhieuNhap(Integer maPN, LocalDateTime ngayNhap, NhanVien maNV, Integer maNCC, Double tongTien) {
		super();
		this.maPN = maPN;
		this.ngayNhap = ngayNhap;
		this.maNV = maNV;
		this.maNCC = maNCC;
		this.tongTien = tongTien;
	}

	public Integer getMaPN() {
		return maPN;
	}

	public void setMaPN(Integer maPN) {
		this.maPN = maPN;
	}

	public LocalDateTime getNgayNhap() {
		return ngayNhap;
	}

	public void setNgayNhap(LocalDateTime ngayNhap) {
		this.ngayNhap = ngayNhap;
	}

	public NhanVien getMaNV() {
		return maNV;
	}

	public void setMaNV(NhanVien maNV) {
		this.maNV = maNV;
	}

	public Integer getMaNCC() {
		return maNCC;
	}

	public void setMaNCC(Integer maNCC) {
		this.maNCC = maNCC;
	}

	public Double getTongTien() {
		return tongTien;
	}

	public void setTongTien(Double tongTien) {
		this.tongTien = tongTien;
	}
	
	
}
