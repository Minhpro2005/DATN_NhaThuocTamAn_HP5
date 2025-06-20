package poly.NhaThuocTamAn.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
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
@Table(name = "DonHang")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DonHang {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer maDonHang;
	
	@ManyToOne
	@JoinColumn(name="maKH", table="KhachHang")
	private KhachHang maKH;
	
	@ManyToOne
	@JoinColumn(name="maNV", table="NhanVien")
	private NhanVien maNV;
	
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime ngayDat;
	
	private Integer trangThai;
	
	private Double tongTien;
	
	private Double giamGia;
	
	private Double thanhToan;
	
	@Column(length=50)
	private String hinhThucThanhToan;
	
	private Boolean daThanhToan;
	
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime ngayThanhToan;
	
	private String diaChiNhan;
	
	@Column(length=20)
	private String soDienThoaiNhan;
	
	private String ghiChu;

	public DonHang() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DonHang(Integer maDonHang, KhachHang maKH, NhanVien maNV, LocalDateTime ngayDat, Integer trangThai,
			Double tongTien, Double giamGia, Double thanhToan, String hinhThucThanhToan, Boolean daThanhToan,
			LocalDateTime ngayThanhToan, String diaChiNhan, String soDienThoaiNhan, String ghiChu) {
		super();
		this.maDonHang = maDonHang;
		this.maKH = maKH;
		this.maNV = maNV;
		this.ngayDat = ngayDat;
		this.trangThai = trangThai;
		this.tongTien = tongTien;
		this.giamGia = giamGia;
		this.thanhToan = thanhToan;
		this.hinhThucThanhToan = hinhThucThanhToan;
		this.daThanhToan = daThanhToan;
		this.ngayThanhToan = ngayThanhToan;
		this.diaChiNhan = diaChiNhan;
		this.soDienThoaiNhan = soDienThoaiNhan;
		this.ghiChu = ghiChu;
	}

	public Integer getMaDonHang() {
		return maDonHang;
	}

	public void setMaDonHang(Integer maDonHang) {
		this.maDonHang = maDonHang;
	}

	public KhachHang getMaKH() {
		return maKH;
	}

	public void setMaKH(KhachHang maKH) {
		this.maKH = maKH;
	}

	public NhanVien getMaNV() {
		return maNV;
	}

	public void setMaNV(NhanVien maNV) {
		this.maNV = maNV;
	}

	public LocalDateTime getNgayDat() {
		return ngayDat;
	}

	public void setNgayDat(LocalDateTime ngayDat) {
		this.ngayDat = ngayDat;
	}

	public Integer getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(Integer trangThai) {
		this.trangThai = trangThai;
	}

	public Double getTongTien() {
		return tongTien;
	}

	public void setTongTien(Double tongTien) {
		this.tongTien = tongTien;
	}

	public Double getGiamGia() {
		return giamGia;
	}

	public void setGiamGia(Double giamGia) {
		this.giamGia = giamGia;
	}

	public Double getThanhToan() {
		return thanhToan;
	}

	public void setThanhToan(Double thanhToan) {
		this.thanhToan = thanhToan;
	}

	public String getHinhThucThanhToan() {
		return hinhThucThanhToan;
	}

	public void setHinhThucThanhToan(String hinhThucThanhToan) {
		this.hinhThucThanhToan = hinhThucThanhToan;
	}

	public Boolean getDaThanhToan() {
		return daThanhToan;
	}

	public void setDaThanhToan(Boolean daThanhToan) {
		this.daThanhToan = daThanhToan;
	}

	public LocalDateTime getNgayThanhToan() {
		return ngayThanhToan;
	}

	public void setNgayThanhToan(LocalDateTime ngayThanhToan) {
		this.ngayThanhToan = ngayThanhToan;
	}

	public String getDiaChiNhan() {
		return diaChiNhan;
	}

	public void setDiaChiNhan(String diaChiNhan) {
		this.diaChiNhan = diaChiNhan;
	}

	public String getSoDienThoaiNhan() {
		return soDienThoaiNhan;
	}

	public void setSoDienThoaiNhan(String soDienThoaiNhan) {
		this.soDienThoaiNhan = soDienThoaiNhan;
	}

	public String getGhiChu() {
		return ghiChu;
	}

	public void setGhiChu(String ghiChu) {
		this.ghiChu = ghiChu;
	}
	
	
}
