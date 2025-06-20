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
@Table(name = "DonThuoc")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DonThuoc {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer maDonThuoc;
	
	@ManyToOne
	@JoinColumn(name="maKH", table="DonThuoc")
	private KhachHang maKH;
	
	private String hinhAnh;
	
	private String moTa;
	
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime ngayGui;
	
	@Column(length = 50)
	private String trangThai;

	public DonThuoc() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DonThuoc(Integer maDonThuoc, KhachHang maKH, String hinhAnh, String moTa, LocalDateTime ngayGui,
			String trangThai) {
		super();
		this.maDonThuoc = maDonThuoc;
		this.maKH = maKH;
		this.hinhAnh = hinhAnh;
		this.moTa = moTa;
		this.ngayGui = ngayGui;
		this.trangThai = trangThai;
	}

	public Integer getMaDonThuoc() {
		return maDonThuoc;
	}

	public void setMaDonThuoc(Integer maDonThuoc) {
		this.maDonThuoc = maDonThuoc;
	}

	public KhachHang getMaKH() {
		return maKH;
	}

	public void setMaKH(KhachHang maKH) {
		this.maKH = maKH;
	}

	public String getHinhAnh() {
		return hinhAnh;
	}

	public void setHinhAnh(String hinhAnh) {
		this.hinhAnh = hinhAnh;
	}

	public String getMoTa() {
		return moTa;
	}

	public void setMoTa(String moTa) {
		this.moTa = moTa;
	}

	public LocalDateTime getNgayGui() {
		return ngayGui;
	}

	public void setNgayGui(LocalDateTime ngayGui) {
		this.ngayGui = ngayGui;
	}

	public String getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(String trangThai) {
		this.trangThai = trangThai;
	}
	
	
}
