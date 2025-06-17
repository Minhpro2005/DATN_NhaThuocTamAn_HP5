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
@Table(name = "DanhGia")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DanhGia {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer maDG;
	
	private Integer maKH;
	
	private Integer maBienThe;
	
	private String hinhAnh;
	
	private Integer soSao;
	
	private String binhLuan;
	
	private LocalDateTime ngayDanhGia;

	public DanhGia(Integer maDG, Integer maKH, Integer maBienThe, String hinhAnh, Integer soSao, String binhLuan,
			LocalDateTime ngayDanhGia) {
		super();
		this.maDG = maDG;
		this.maKH = maKH;
		this.maBienThe = maBienThe;
		this.hinhAnh = hinhAnh;
		this.soSao = soSao;
		this.binhLuan = binhLuan;
		this.ngayDanhGia = ngayDanhGia;
	}

	public DanhGia() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getMaDG() {
		return maDG;
	}

	public void setMaDG(Integer maDG) {
		this.maDG = maDG;
	}

	public Integer getMaKH() {
		return maKH;
	}

	public void setMaKH(Integer maKH) {
		this.maKH = maKH;
	}

	public Integer getMaBienThe() {
		return maBienThe;
	}

	public void setMaBienThe(Integer maBienThe) {
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

	public LocalDateTime getNgayDanhGia() {
		return ngayDanhGia;
	}

	public void setNgayDanhGia(LocalDateTime ngayDanhGia) {
		this.ngayDanhGia = ngayDanhGia;
	}
	
	
}
