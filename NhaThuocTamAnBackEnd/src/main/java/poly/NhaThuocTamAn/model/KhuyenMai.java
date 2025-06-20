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
@Table(name = "KhuyenMai")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KhuyenMai {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer maKM;
	
	@Column(length = 100)
	private String tenKM;
	
	@Column(length = 50)
	private String loaiKM;
	
	private Double giaTri;
	
	@Temporal(TemporalType.DATE)
	private Date ngayBatDau;
	
	@Temporal(TemporalType.DATE)
	private Date ngayKetThuc;
	
	private String moTa;
	
	private Boolean trangThai;

	public KhuyenMai(Integer maKM, String tenKM, String loaiKM, Double giaTri, Date ngayBatDau, Date ngayKetThuc,
			String moTa, Boolean trangThai) {
		super();
		this.maKM = maKM;
		this.tenKM = tenKM;
		this.loaiKM = loaiKM;
		this.giaTri = giaTri;
		this.ngayBatDau = ngayBatDau;
		this.ngayKetThuc = ngayKetThuc;
		this.moTa = moTa;
		this.trangThai = trangThai;
	}

	public KhuyenMai() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getMaKM() {
		return maKM;
	}

	public void setMaKM(Integer maKM) {
		this.maKM = maKM;
	}

	public String getTenKM() {
		return tenKM;
	}

	public void setTenKM(String tenKM) {
		this.tenKM = tenKM;
	}

	public String getLoaiKM() {
		return loaiKM;
	}

	public void setLoaiKM(String loaiKM) {
		this.loaiKM = loaiKM;
	}

	public Double getGiaTri() {
		return giaTri;
	}

	public void setGiaTri(Double giaTri) {
		this.giaTri = giaTri;
	}

	public Date getNgayBatDau() {
		return ngayBatDau;
	}

	public void setNgayBatDau(Date ngayBatDau) {
		this.ngayBatDau = ngayBatDau;
	}

	public Date getNgayKetThuc() {
		return ngayKetThuc;
	}

	public void setNgayKetThuc(Date ngayKetThuc) {
		this.ngayKetThuc = ngayKetThuc;
	}

	public String getMoTa() {
		return moTa;
	}

	public void setMoTa(String moTa) {
		this.moTa = moTa;
	}

	public Boolean getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(Boolean trangThai) {
		this.trangThai = trangThai;
	}
	
	
}
