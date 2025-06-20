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
@Table(name = "TrieuChung_BienTheThuoc")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TrieuChung_BienTheThuoc {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer maTCBTT;
	
	@ManyToOne
	@JoinColumn(name="maTC", table="TrieuChung")
	private TrieuChung maTC;
	
	@ManyToOne
	@JoinColumn(name="maBienThe", table="BienTheThuoc")
	private BienTheThuoc maBienThe;

	public TrieuChung_BienTheThuoc() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TrieuChung_BienTheThuoc(Integer maTCBTT, TrieuChung maTC, BienTheThuoc maBienThe) {
		super();
		this.maTCBTT = maTCBTT;
		this.maTC = maTC;
		this.maBienThe = maBienThe;
	}

	public Integer getMaTCBTT() {
		return maTCBTT;
	}

	public void setMaTCBTT(Integer maTCBTT) {
		this.maTCBTT = maTCBTT;
	}

	public TrieuChung getMaTC() {
		return maTC;
	}

	public void setMaTC(TrieuChung maTC) {
		this.maTC = maTC;
	}

	public BienTheThuoc getMaBienThe() {
		return maBienThe;
	}

	public void setMaBienThe(BienTheThuoc maBienThe) {
		this.maBienThe = maBienThe;
	}
	
	
}
