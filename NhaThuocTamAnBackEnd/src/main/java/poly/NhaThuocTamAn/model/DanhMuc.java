package poly.NhaThuocTamAn.model;

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
@Table(name = "DanhMuc")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DanhMuc {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer maDM;
	
	@Column(length = 100)
	private String tenDanhMuc;
	
	private String moTa;

	public DanhMuc() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DanhMuc(Integer maDM, String tenDanhMuc, String moTa) {
		super();
		this.maDM = maDM;
		this.tenDanhMuc = tenDanhMuc;
		this.moTa = moTa;
	}

	public Integer getMaDM() {
		return maDM;
	}

	public void setMaDM(Integer maDM) {
		this.maDM = maDM;
	}

	public String getTenDanhMuc() {
		return tenDanhMuc;
	}

	public void setTenDanhMuc(String tenDanhMuc) {
		this.tenDanhMuc = tenDanhMuc;
	}

	public String getMoTa() {
		return moTa;
	}

	public void setMoTa(String moTa) {
		this.moTa = moTa;
	}
	
	
}
