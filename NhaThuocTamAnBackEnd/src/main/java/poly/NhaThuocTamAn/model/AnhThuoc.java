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
@Table(name = "AnhThuoc")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnhThuoc {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer maAnhThuoc;
	
	@ManyToOne
	@JoinColumn(name = "maThuoc", table = "Thuoc")
	private Thuoc maThuoc;
	
	private String hinhAnh;

	public AnhThuoc() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AnhThuoc(Integer maAnhThuoc, Thuoc maThuoc, String hinhAnh) {
		super();
		this.maAnhThuoc = maAnhThuoc;
		this.maThuoc = maThuoc;
		this.hinhAnh = hinhAnh;
	}

	public Integer getMaAnhThuoc() {
		return maAnhThuoc;
	}

	public void setMaAnhThuoc(Integer maAnhThuoc) {
		this.maAnhThuoc = maAnhThuoc;
	}

	public Thuoc getMaThuoc() {
		return maThuoc;
	}

	public void setMaThuoc(Thuoc maThuoc) {
		this.maThuoc = maThuoc;
	}

	public String getHinhAnh() {
		return hinhAnh;
	}

	public void setHinhAnh(String hinhAnh) {
		this.hinhAnh = hinhAnh;
	}
	
	
}
