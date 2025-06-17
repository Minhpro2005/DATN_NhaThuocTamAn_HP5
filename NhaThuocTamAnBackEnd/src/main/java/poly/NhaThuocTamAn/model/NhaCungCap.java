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
@Table(name = "NhaCungCap")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NhaCungCap {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer maNCC;
	
	private String tenNCC;
	
	private String diaChi;
	
	private String soDienThoai;
	
	private String email;

	public NhaCungCap() {
		super();
		// TODO Auto-generated constructor stub
	}

	public NhaCungCap(Integer maNCC, String tenNCC, String diaChi, String soDienThoai, String email) {
		super();
		this.maNCC = maNCC;
		this.tenNCC = tenNCC;
		this.diaChi = diaChi;
		this.soDienThoai = soDienThoai;
		this.email = email;
	}

	public Integer getMaNCC() {
		return maNCC;
	}

	public void setMaNCC(Integer maNCC) {
		this.maNCC = maNCC;
	}

	public String getTenNCC() {
		return tenNCC;
	}

	public void setTenNCC(String tenNCC) {
		this.tenNCC = tenNCC;
	}

	public String getDiaChi() {
		return diaChi;
	}

	public void setDiaChi(String diaChi) {
		this.diaChi = diaChi;
	}

	public String getSoDienThoai() {
		return soDienThoai;
	}

	public void setSoDienThoai(String soDienThoai) {
		this.soDienThoai = soDienThoai;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
}
