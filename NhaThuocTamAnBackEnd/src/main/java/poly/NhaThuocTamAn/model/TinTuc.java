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
@Table(name = "TinTuc")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TinTuc {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer maTin;
	
	private String tieuDe;
	
	private String moTa;
	
	private String noiDung;
	
	private LocalDateTime ngayDang;
	
	@Column(length = 100)
	private String tacGia;
	
	@Column(length = 100)
	private String hinhAnh;
	
	private Boolean trangThai;

	public TinTuc(Integer maTin, String tieuDe, String moTa, String noiDung, LocalDateTime ngayDang, String tacGia,
			String hinhAnh, Boolean trangThai) {
		super();
		this.maTin = maTin;
		this.tieuDe = tieuDe;
		this.moTa = moTa;
		this.noiDung = noiDung;
		this.ngayDang = ngayDang;
		this.tacGia = tacGia;
		this.hinhAnh = hinhAnh;
		this.trangThai = trangThai;
	}

	public TinTuc() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getMaTin() {
		return maTin;
	}

	public void setMaTin(Integer maTin) {
		this.maTin = maTin;
	}

	public String getTieuDe() {
		return tieuDe;
	}

	public void setTieuDe(String tieuDe) {
		this.tieuDe = tieuDe;
	}

	public String getMoTa() {
		return moTa;
	}

	public void setMoTa(String moTa) {
		this.moTa = moTa;
	}

	public String getNoiDung() {
		return noiDung;
	}

	public void setNoiDung(String noiDung) {
		this.noiDung = noiDung;
	}

	public LocalDateTime getNgayDang() {
		return ngayDang;
	}

	public void setNgayDang(LocalDateTime ngayDang) {
		this.ngayDang = ngayDang;
	}

	public String getTacGia() {
		return tacGia;
	}

	public void setTacGia(String tacGia) {
		this.tacGia = tacGia;
	}

	public String getHinhAnh() {
		return hinhAnh;
	}

	public void setHinhAnh(String hinhAnh) {
		this.hinhAnh = hinhAnh;
	}

	public Boolean getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(Boolean trangThai) {
		this.trangThai = trangThai;
	}
	
	
}
