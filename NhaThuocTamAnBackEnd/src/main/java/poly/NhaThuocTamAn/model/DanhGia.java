package poly.NhaThuocTamAn.model;

import java.util.Date;

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
@Table(name = "DanhGia")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DanhGia {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer maDG;
	
	@ManyToOne
	@JoinColumn(name="maKH", table="KhachHang")
	private KhachHang maKH;
	
	@ManyToOne
	@JoinColumn(name="maBienThe", table="BienTheThuoc")
	private BienTheThuoc maBienThe;
	
	private String hinhAnh;
	
	private Integer soSao;
	
	private String binhLuan;
	
	@Temporal(TemporalType.DATE)
	private Date ngayDanhGia;
	
	public DanhGia() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DanhGia(Integer maDG, KhachHang maKH, BienTheThuoc maBienThe, String hinhAnh, Integer soSao, String binhLuan,
			Date ngayDanhGia) {
		super();
		this.maDG = maDG;
		this.maKH = maKH;
		this.maBienThe = maBienThe;
		this.hinhAnh = hinhAnh;
		this.soSao = soSao;
		this.binhLuan = binhLuan;
		this.ngayDanhGia = ngayDanhGia;
	}

	public Integer getMaDG() {
		return maDG;
	}

	public void setMaDG(Integer maDG) {
		this.maDG = maDG;
	}

	public KhachHang getMaKH() {
		return maKH;
	}

	public void setMaKH(KhachHang maKH) {
		this.maKH = maKH;
	}

	public BienTheThuoc getMaBienThe() {
		return maBienThe;
	}

	public void setMaBienThe(BienTheThuoc maBienThe) {
		this.maBienThe = maBienThe;
	}

	public String getHinhAnh() {
		return hinhAnh;
	}

	public void setHinhAnh(String hinhAnh) {
		this.hinhAnh = hinhAnh;
	}

	public Integer getSoSao() {
		return soSao;
	}

	public void setSoSao(Integer soSao) {
		this.soSao = soSao;
	}

	public String getBinhLuan() {
		return binhLuan;
	}

	public void setBinhLuan(String binhLuan) {
		this.binhLuan = binhLuan;
	}

	public Date getNgayDanhGia() {
		return ngayDanhGia;
	}

	public void setNgayDanhGia(Date ngayDanhGia) {
		this.ngayDanhGia = ngayDanhGia;
	}
	
	
}
