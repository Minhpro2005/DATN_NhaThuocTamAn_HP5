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
@Table(name = "TrieuChung")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TrieuChung {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer maTC;
	
	@Column(length=100)
	private String tenTrieuChung;

	public TrieuChung() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TrieuChung(Integer maTC, String tenTrieuChung) {
		super();
		this.maTC = maTC;
		this.tenTrieuChung = tenTrieuChung;
	}

	public Integer getMaTC() {
		return maTC;
	}

	public void setMaTC(Integer maTC) {
		this.maTC = maTC;
	}

	public String getTenTrieuChung() {
		return tenTrieuChung;
	}

	public void setTenTrieuChung(String tenTrieuChung) {
		this.tenTrieuChung = tenTrieuChung;
	}
	
	
}
