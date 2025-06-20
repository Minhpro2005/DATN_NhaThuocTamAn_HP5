package poly.NhaThuocTamAn.model;

import jakarta.persistence.Column;
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
@Table(name = "BienTheThuoc")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BienTheThuoc {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer maBienThe;
	
	@ManyToOne
	@JoinColumn(name = "maThuoc", table = "Thuoc")
	private Thuoc maThuoc;
	
	@Column(length = 100)
	private String tenBienThe;
	
	private Double giaBan;
	
	private String hinhAnh;

	public BienTheThuoc() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BienTheThuoc(Integer maBienThe, Thuoc maThuoc, String tenBienThe, Double giaBan, String hinhAnh) {
		super();
		this.maBienThe = maBienThe;
		this.maThuoc = maThuoc;
		this.tenBienThe = tenBienThe;
		this.giaBan = giaBan;
		this.hinhAnh = hinhAnh;
	}

	public Integer getMaBienThe() {
		return maBienThe;
	}

	public void setMaBienThe(Integer maBienThe) {
		this.maBienThe = maBienThe;
	}

	public Thuoc getMaThuoc() {
		return maThuoc;
	}

	public void setMaThuoc(Thuoc maThuoc) {
		this.maThuoc = maThuoc;
	}

	public String getTenBienThe() {
		return tenBienThe;
	}

	public void setTenBienThe(String tenBienThe) {
		this.tenBienThe = tenBienThe;
	}

	public Double getGiaBan() {
		return giaBan;
	}

	public void setGiaBan(Double giaBan) {
		this.giaBan = giaBan;
	}

	public String getHinhAnh() {
		return hinhAnh;
	}

	public void setHinhAnh(String hinhAnh) {
		this.hinhAnh = hinhAnh;
	}
	
	
}
